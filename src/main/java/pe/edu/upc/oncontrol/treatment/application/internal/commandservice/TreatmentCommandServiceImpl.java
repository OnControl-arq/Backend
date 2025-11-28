package pe.edu.upc.oncontrol.treatment.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CancelProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CreateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.StartProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.UpdateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom.CreateSymptomCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.CreateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.UpdateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.*;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentCommandService;
import pe.edu.upc.oncontrol.treatment.application.internal.ProcedureExecutionGenerator;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureExecutionRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.TreatmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TreatmentCommandServiceImpl implements TreatmentCommandService {
    private final TreatmentRepository treatmentRepository;
    private final ProcedureRepository procedureRepository;
    private final ProfileAccessAcl profileAccessAcl;
    private final ProcedureExecutionRepository procedureExecutionRepository;
    private final ProcedureExecutionGenerator procedureExecutionGenerator;

    public TreatmentCommandServiceImpl(TreatmentRepository treatmentRepository, ProcedureRepository procedureRepository, ProfileAccessAcl profileAccessAcl, ProcedureExecutionRepository procedureExecutionRepository, ProcedureExecutionGenerator procedureExecutionGenerator) {
        this.treatmentRepository = treatmentRepository;
        this.procedureRepository = procedureRepository;
        this.profileAccessAcl = profileAccessAcl;
        this.procedureExecutionRepository = procedureExecutionRepository;
        this.procedureExecutionGenerator = procedureExecutionGenerator;
    }

    @Override
    public UUID createTreatment(CreateTreatmentCommand command) {
        UUID doctorUuid = command.doctorProfileUuid();
        UUID patientUuid = command.patientProfileUuid();

        LocalDate today = LocalDate.now();
        if (command.startDate().isBefore(today.minusDays(3))) {
            throw new IllegalStateException("The start date cannot be earlier than 3 days ago.");
        }

        // Validar que la fecha de inicio no sea mayor a la fecha de fin
        if (command.startDate().isAfter(command.endDate())) {
            throw new IllegalStateException("The start date cannot be after the end date.");
        }

        // Validar acceso entre doctor y paciente
        if (!profileAccessAcl.isLinkActive(doctorUuid, patientUuid)) {
            throw new IllegalStateException("Doctor and patient profiles are not linked or the link is inactive.");
        }

//        // Validar que el doctor no tenga ya un tratamiento activo con ese paciente
//        boolean yaExiste = treatmentRepository.existsByDoctorProfileUuidAndPatientProfileUuidAndStatus(
//                doctorUuid, patientUuid, TreatmentStatus.ACTIVE
//        );
//        if (yaExiste) {
//            throw new IllegalStateException("Treatment already exists for this doctor and patient.");
//        }

        // Validar que el paciente no tenga más de 6 tratamientos activos
        long tratamientosActivosPaciente = treatmentRepository.countByPatientProfileUuidAndStatus(
                patientUuid, TreatmentStatus.ACTIVE
        );
        if (tratamientosActivosPaciente >= 6) {
            throw new IllegalStateException("Patient cannot have more than 3 active treatments.");
        }
        // Validar que el doctor no tenga más de 3 tratamientos activos con el mismo paciente
        long tratamientosActivosConPaciente = treatmentRepository.countByDoctorProfileUuidAndPatientProfileUuidAndStatus(
                doctorUuid, patientUuid, TreatmentStatus.ACTIVE
        );
        if (tratamientosActivosConPaciente >= 3) {
            throw new IllegalStateException("Doctor cannot have more than 3 active treatments with the same patient.");
        }

        // Crear Treatment
        TreatmentTitle title = new TreatmentTitle(command.title());
        TreatmentPeriod period = new TreatmentPeriod(command.startDate(), command.endDate());

        Treatment treatment = new Treatment(title, period, doctorUuid, patientUuid);

        Treatment saved = treatmentRepository.save(treatment);
        return saved.getExternalId();
    }

    @Override
    public void startProcedure(StartProcedureCommand command) {
        Procedure procedure = procedureRepository.findById(command.procedureId())
                .orElseThrow(()-> new EntityNotFoundException("Procedure not found."));

        if(!procedure.getTreatment().getPatientProfileUuid().equals(command.patientProfileUuid())){
            throw new AccessDeniedException("Only the doctor who created the treatment can start procedures.");
        }
        if(procedure.getProcedureStatus() != ProcedureStatus.PENDING){
            throw new IllegalStateException("Procedure has already been started or cancelled.");
        }
        procedure.activate(command.startDateTime());
        List<ProcedureExecution> executions = procedureExecutionGenerator.generateInitialExecutions(procedure);
        procedureExecutionRepository.saveAll(executions);
        procedureRepository.save(procedure);
    }

    @Override
    public void addProcedure(CreateProcedureCommand command) {
        UUID treatmentId = command.treatmentExternalId();

        Treatment treatment = treatmentRepository.findByExternalId(treatmentId)
                .orElseThrow(() -> new EntityNotFoundException("Treatment not found."));

        // Validación de propiedad: solo el doctor dueño puede agregar procedimientos
        if (!treatment.getDoctorProfileUuid().equals(command.doctorProfileUuid())) {
            throw new AccessDeniedException("Only the doctor who created the treatment can add procedures.");
        }

        // Construcción del procedimiento
        ProcedureDescription description = new ProcedureDescription(command.description());

        SchedulePattern pattern = new SchedulePattern(
                command.recurrenceType(),
                command.interval(),
                command.totalOccurrences(),
                command.untilDate()
        );

        Procedure procedure = new Procedure(
                description,
                pattern,
                treatment
        );

        treatment.addProcedure(procedure); // asocia al tratamiento

        treatmentRepository.save(treatment); // cascada lo guarda
    }

    @Override
    public void updateTreatment(UpdateTreatmentCommand command) {

    }

    @Override
    public void updateProcedure(UpdateProcedureCommand command) {

    }

    @Override
    public void cancelProcedure(CancelProcedureCommand command) {
        Procedure procedure = procedureRepository.findById(command.procedureId())
                .orElseThrow(()-> new EntityNotFoundException("Procedure not found."));
        if (!procedure.getTreatment().getDoctorProfileUuid().equals(command.donctorProfileUuid())){
            throw new AccessDeniedException("Only the doctor who created the treatment can cancel procedures.");
        }

        if(procedure.getProcedureStatus() == ProcedureStatus.CANCELLED || procedure.getProcedureStatus() == ProcedureStatus.COMPLETED) {
            throw new IllegalStateException("Procedure is already cancelled or completed.");
        }

        procedure.cancel();
        procedureRepository.save(procedure);
    }

    @Override
    public void logSymptom(CreateSymptomCommand command) {
        UUID treatmentExternalId = command.treatmentExternalId();
        UUID patientProfileUuid = command.patientProfileUuid();

        Treatment treatment = treatmentRepository.findByExternalId(treatmentExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Treatment not found."));

        if (!treatment.getPatientProfileUuid().equals(patientProfileUuid)) {
            throw new AccessDeniedException("Treatment does not belong to the patient.");
        }

        if (treatment.getStatus() != TreatmentStatus.ACTIVE) {
            throw new IllegalStateException("Cannot log symptoms for a non-active treatment.");
        }

        SymptomLog log = new SymptomLog(
                command.loggedAt(),
                command.symptomType(),
                command.severity(),
                command.notes(),
                treatment,
                patientProfileUuid
        );

        treatment.logSymptom(log);
        treatmentRepository.save(treatment); // cascada guarda log
    }

}

package pe.edu.upc.oncontrol.treatment.application.internal.queryservice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.treatment.application.internal.ProcedureExecutionGenerator;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ExecutionStatus;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureExecutionForecast;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureStatus;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.TreatmentStatus;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentQueryService;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureExecutionRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.SymptomLogRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.TreatmentRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TreatmentQueryServiceImpl implements TreatmentQueryService {

    private final TreatmentRepository treatmentRepository;
    private final ProcedureRepository procedureRepository;
    private final SymptomLogRepository symptomLogRepository;
    private final ProcedureExecutionRepository procedureExecutionRepository;
    private final ProcedureExecutionGenerator procedureExecutionGenerator;

    public TreatmentQueryServiceImpl(TreatmentRepository treatmentRepository, ProcedureRepository procedureRepository, SymptomLogRepository symptomLogRepository, ProcedureExecutionRepository procedureExecutionRepository, ProcedureExecutionGenerator procedureExecutionGenerator) {
        this.treatmentRepository = treatmentRepository;
        this.procedureRepository = procedureRepository;
        this.symptomLogRepository = symptomLogRepository;
        this.procedureExecutionRepository = procedureExecutionRepository;
        this.procedureExecutionGenerator = procedureExecutionGenerator;
    }

    @Override
    public Optional<Treatment> getTreatmentByExternalId(UUID treatmentExternalId) {
        return treatmentRepository.findByExternalId(treatmentExternalId);
    }

    @Override
    public List<Treatment> getActiveTreatmentsByPatient(UUID patientProfileUuid) {
        return treatmentRepository.findAll().stream()
                .filter(t -> t.getPatientProfileUuid().equals(patientProfileUuid)
                        && t.getStatus() == TreatmentStatus.ACTIVE)
                .toList();
    }

    @Override
    public List<Treatment> getActiveTreatmentsByDoctor(UUID doctorProfileUuid) {
        return treatmentRepository.findAll().stream()
                .filter(t -> t.getDoctorProfileUuid().equals(doctorProfileUuid)
                )
                //&& t.getStatus() == TreatmentStatus.ACTIVE)
                .toList();
    }

    @Override
    public List<Procedure> getPendingProceduresByPatient(UUID patientProfileUuid) {
        return procedureRepository.findByTreatment_PatientProfileUuidAndProcedureStatus(
                patientProfileUuid,
                ProcedureStatus.PENDING
        );
    }

    @Override
    public List<SymptomLog> getSymptomLogsByTreatment(UUID treatmentExternalId) {
        return symptomLogRepository.findByTreatment_ExternalIdOrderByLoggedAtDesc(treatmentExternalId);
    }

    @Override
    public List<SymptomLog> getSymptomLogsByPatientInRange(UUID patientProfileUuid, LocalDateTime start, LocalDateTime end) {
        return symptomLogRepository.findLogsInRangeByPatient(patientProfileUuid, start, end);
    }

    @Override
    public Optional<SymptomLog> getSymptomLogById(Long symptomLogId) {
        return symptomLogRepository.findById(symptomLogId);
    }

    @Override
    public List<ProcedureExecutionForecast> getForecastForTreatment(UUID treatmentExternalId) {
        Treatment treatment = treatmentRepository.findByExternalId(treatmentExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado."));

        List<Procedure> procedures = procedureRepository.findByTreatment_ExternalId(treatment.getExternalId());

        List<ProcedureExecutionForecast> allPredictions = new ArrayList<>();

        for (Procedure procedure : procedures) {
            List<ProcedureExecution> realExecutions = procedureExecutionRepository
                    .findByProcedure_Id(procedure.getId());

            Map<LocalDateTime, ExecutionStatus> executedMap = realExecutions.stream()
                    .collect(Collectors.toMap(
                            e -> e.getWindow().getScheduledAt(),
                            ProcedureExecution::getStatus
                    ));

            List<LocalDateTime> expectedDates = procedureExecutionGenerator.generateScheduledDatesFor(procedure);

            for (LocalDateTime scheduled : expectedDates) {
                ProcedureExecution execution = realExecutions.stream()
                        .filter(e -> e.getWindow().getScheduledAt().equals(scheduled))
                        .findFirst()
                        .orElse(null);

                ExecutionStatus status = execution != null ? execution.getStatus() : ExecutionStatus.PENDING;
                Long executionId = execution != null ? execution.getId() : null;

                allPredictions.add(new ProcedureExecutionForecast(
                        executionId,
                        procedure.getDescription().getValue(),
                        scheduled,
                        status
                ));
            }
        }

        return allPredictions;
    }

    @Override
    public List<Procedure> getProceduresByExternalId(UUID treatmentExternalId) {
        return procedureRepository.findAllByTreatmentExternalId(treatmentExternalId);
    }


}

package pe.edu.upc.oncontrol.treatment.application.internal.queryservice;

import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.procedure.ProcedureExecutionQueryService;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureExecutionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProcedureExecutionQueryServiceImpl implements ProcedureExecutionQueryService {

    private final ProcedureExecutionRepository procedureExecutionRepository;

    public ProcedureExecutionQueryServiceImpl(ProcedureExecutionRepository procedureExecutionRepository) {
        this.procedureExecutionRepository = procedureExecutionRepository;
    }

    @Override
    public List<ProcedureExecution> getByProcedureIdForDoctor(Long procedureId, UUID doctorProfileUuid) {
        return procedureExecutionRepository.findByProcedure_Id(procedureId).stream()
                .filter(e -> e.getProcedure().getTreatment().getDoctorProfileUuid().equals(doctorProfileUuid))
                .toList();
    }

    @Override
    public List<ProcedureExecution> getByProcedureIdForPatient(Long procedureId, UUID patientProfileUuid) {
        return procedureExecutionRepository.findByProcedure_Id(procedureId).stream()
                .filter(e -> e.getProcedure().getTreatment().getDoctorProfileUuid().equals(patientProfileUuid))
                .toList();
    }

    @Override
    public Optional<ProcedureExecution> getByIdAndPatient(Long executionId, UUID patientProfileUuid) {
        return procedureExecutionRepository.findByIdAndProcedure_Treatment_PatientProfileUuid(executionId, patientProfileUuid);
    }

    @Override
    public Optional<ProcedureExecution> getByIdAndDoctor(Long executionId, UUID doctorProfileUuid) {
        return procedureExecutionRepository.findById(executionId)
                .filter(e-> e.getProcedure().getTreatment().getDoctorProfileUuid().equals(doctorProfileUuid));
    }
}

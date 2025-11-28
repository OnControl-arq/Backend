package pe.edu.upc.oncontrol.treatment.domain.services.treatment.procedure;

import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProcedureExecutionQueryService {

    List<ProcedureExecution> getByProcedureIdForDoctor(Long procedureId, UUID doctorProfileUuid);

    List<ProcedureExecution> getByProcedureIdForPatient(Long procedureId, UUID patientProfileUuid);

    Optional<ProcedureExecution> getByIdAndPatient(Long executionId, UUID patientProfileUuid);

    Optional<ProcedureExecution> getByIdAndDoctor(Long executionId, UUID doctorProfileUuid);
}


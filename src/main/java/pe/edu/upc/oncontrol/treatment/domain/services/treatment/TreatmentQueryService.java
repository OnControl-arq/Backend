package pe.edu.upc.oncontrol.treatment.domain.services.treatment;

import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureExecutionForecast;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreatmentQueryService {
    Optional<Treatment> getTreatmentByExternalId(UUID treatmentExternalId);
    List<Treatment> getActiveTreatmentsByPatient(UUID patientProfileUuid);
    List<Treatment> getActiveTreatmentsByDoctor(UUID doctorProfileUuid);
    List<Procedure> getPendingProceduresByPatient(UUID patientProfileUuid);
    List<SymptomLog> getSymptomLogsByTreatment(UUID treatmentExternalId);
    List<SymptomLog> getSymptomLogsByPatientInRange(UUID patientProfileUuid, LocalDateTime start, LocalDateTime end);
    Optional<SymptomLog> getSymptomLogById(Long symptomLogId);
    List<ProcedureExecutionForecast> getForecastForTreatment(UUID treatmentExternalId);
    List<Procedure> getProceduresByExternalId(UUID treatmentExternalId);
}

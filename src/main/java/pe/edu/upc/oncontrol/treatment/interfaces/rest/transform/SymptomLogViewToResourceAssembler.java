package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.SymptomLogViewResource;

import java.util.List;

@Component
public class SymptomLogViewToResourceAssembler {
    public SymptomLogViewResource toResource(SymptomLog symptomLog){
        return new SymptomLogViewResource(
                symptomLog.getId(),
                symptomLog.getLoggedAt(),
                symptomLog.getSymptomType(),
                symptomLog.getSeverity().name(),
                symptomLog.getNotes(),
                symptomLog.getTreatment().getExternalId(),
                symptomLog.getCreatedAt()
        );
    }

    public List<SymptomLogViewResource> toResourcerList(List<SymptomLog> logs){
        return logs.stream().map(this::toResource).toList();
    }

}

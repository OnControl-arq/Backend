package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.ProcedureViewResource;

import java.util.List;

public class ProcedureToResourceAssembler {
    public static ProcedureViewResource toResourceFromEntity(Procedure entity) {
        return new ProcedureViewResource(
                entity.getId(),
                entity.getTreatment().getExternalId(),
                entity.getDescription().getValue(),
                entity.getProcedureStatus().name(),
                entity.getSchedulePattern().getType(),
                entity.getSchedulePattern().getInterval(),
                entity.getSchedulePattern().getTotalOccurrences(),
                entity.getSchedulePattern().getUntilDate(),
                entity.getStartDateTime()
        );
    }

    public static List<ProcedureViewResource> toResourceListFromEntities(List<Procedure> entities) {
        return entities.stream()
                .map(ProcedureToResourceAssembler::toResourceFromEntity)
                .toList();
    }
}

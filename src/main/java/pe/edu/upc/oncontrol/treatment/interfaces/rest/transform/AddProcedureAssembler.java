package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CreateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.AddProcedureResource;

import java.util.UUID;

@Component
public class AddProcedureAssembler {
    public CreateProcedureCommand toCommand(UUID treatmentExternalId, AddProcedureResource resource) {
        return new CreateProcedureCommand(
                treatmentExternalId,
                resource.doctorProfileUuid(),
                resource.description(),
                resource.recurrenceType(),
                resource.interval(),
                resource.totalOccurrences(),
                resource.untilDate()
        );
    }
}

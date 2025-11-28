package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.StartProcedureCommand;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.StartProcedureResource;

@Component
public class StartProcedureCommandAssembler {
    public StartProcedureCommand toCommand(Long procedureId, StartProcedureResource resource) {
        return new StartProcedureCommand(
                procedureId,
                resource.patientProfileUuid(),
                resource.startDateTime()
        );
    }
}

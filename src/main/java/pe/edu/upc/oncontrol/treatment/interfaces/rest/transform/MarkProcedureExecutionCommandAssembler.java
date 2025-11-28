package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureExecutionCommand;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.MarkProcedureExecutionResource;

@Component
public class MarkProcedureExecutionCommandAssembler {
    public MarkProcedureExecutionCommand toCommand(Long executionId, MarkProcedureExecutionResource resource){
        return new MarkProcedureExecutionCommand(
                executionId,
                resource.patientProfileUuid(),
                resource.completionDate()
        );
    }
}

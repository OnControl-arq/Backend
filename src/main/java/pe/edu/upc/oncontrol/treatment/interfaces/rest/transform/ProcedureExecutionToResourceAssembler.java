package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.ProcedureExecutionViewResource;

import java.util.List;

@Component
public class ProcedureExecutionToResourceAssembler {
    public ProcedureExecutionViewResource toResource(ProcedureExecution execution){
        return new ProcedureExecutionViewResource(
                execution.getId(),
                execution.getScheduledAt(),
                execution.getStatus(),
                execution.getCompletedAt()
        );
    }
    public List<ProcedureExecutionViewResource> toResourceList(List<ProcedureExecution> executions){
        return executions.stream()
                .map(this::toResource)
                .toList();
    }
}

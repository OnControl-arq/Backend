package pe.edu.upc.oncontrol.treatment.domain.services.treatment.procedure;

import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureExecutionCommand;


public interface ProcedureExecutionService {
    void markProcedureExecution(MarkProcedureExecutionCommand command);
}

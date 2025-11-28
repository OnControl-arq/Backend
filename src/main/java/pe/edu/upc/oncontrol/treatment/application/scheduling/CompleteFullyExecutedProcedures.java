package pe.edu.upc.oncontrol.treatment.application.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureStatus;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureExecutionRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureRepository;

import java.util.List;

@Component
public class CompleteFullyExecutedProcedures {
    private final ProcedureExecutionRepository executionRepository;
    private final ProcedureRepository procedureRepository;

    public CompleteFullyExecutedProcedures(ProcedureExecutionRepository executionRepository, ProcedureRepository procedureRepository) {
        this.executionRepository = executionRepository;
        this.procedureRepository = procedureRepository;
    }

    
    


}

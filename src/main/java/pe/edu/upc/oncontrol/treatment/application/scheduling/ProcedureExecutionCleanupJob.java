package pe.edu.upc.oncontrol.treatment.application.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureStatus;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureExecutionRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProcedureExecutionCleanupJob {
    private final ProcedureExecutionRepository executionRepository;
    private final ProcedureRepository procedureRepository;

    public ProcedureExecutionCleanupJob(ProcedureExecutionRepository executionRepository, ProcedureRepository procedureRepository) {
        this.executionRepository = executionRepository;
        this.procedureRepository = procedureRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void markExpiredExecutionsAsMissed() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);

        List<ProcedureExecution> expired = executionRepository
                .findPendingExecutionsOutsideGracePeriod(threshold);

        int marked = 0;

        for (ProcedureExecution execution : expired){
            execution.markMissedIfExpired(LocalDateTime.now());
            marked++;
        }

        if(!expired.isEmpty()){
            executionRepository.saveAll(expired);
            System.out.printf("[Scheduled] %d executions marked as missed due to expiration%n", marked);
        }
    }

    @Scheduled(cron ="0 15 0 * * *")
    public void completeFullAndExecutedProcedures(){
        List<Procedure> activeProcedures = procedureRepository.findByProcedureStatus(ProcedureStatus.ACTIVE);

        for (Procedure procedure : activeProcedures){
            if(executionRepository.isProcedureFullyCompleted(procedure.getId())){
                procedure.markAsCompleted();
            }
        }
        procedureRepository.saveAll(activeProcedures);
    }


}

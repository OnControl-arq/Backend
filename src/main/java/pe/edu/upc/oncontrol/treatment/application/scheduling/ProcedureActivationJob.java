package pe.edu.upc.oncontrol.treatment.application.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureStatus;
import pe.edu.upc.oncontrol.treatment.application.internal.ProcedureExecutionGenerator;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureExecutionRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class ProcedureActivationJob {

    private final ProcedureRepository procedureRepository;
    private final ProcedureExecutionRepository procedureExecutionRepository;
    private final ProcedureExecutionGenerator procedureExecutionGenerator;

    public ProcedureActivationJob(ProcedureRepository procedureRepository, ProcedureExecutionRepository procedureExecutionRepository, ProcedureExecutionGenerator procedureExecutionGenerator) {
        this.procedureRepository = procedureRepository;
        this.procedureExecutionRepository = procedureExecutionRepository;
        this.procedureExecutionGenerator = procedureExecutionGenerator;
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void activatePendingProcedures(){
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);
        Date thresholdDate = Date.from(threshold.atZone(ZoneId.systemDefault()).toInstant());

        List<Procedure> pending = procedureRepository.findByProcedureStatusAndStartDateTimeIsNullAndCreatedAtBefore(
                ProcedureStatus.PENDING, thresholdDate
        );

        for (Procedure procedure : pending){
            LocalDateTime now = LocalDateTime.now();

            procedure.activate(now);
            List<ProcedureExecution> executions = procedureExecutionGenerator.generateInitialExecutions(procedure);

            procedureExecutionRepository.saveAll(executions);

        }

        procedureRepository.saveAll(pending);

        if(!pending.isEmpty()){
            System.out.printf("[Scheduled] %d Automatically activated procedures %n", pending.size());
        }

    }

}

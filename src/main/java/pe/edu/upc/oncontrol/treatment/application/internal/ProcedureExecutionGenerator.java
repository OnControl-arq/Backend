package pe.edu.upc.oncontrol.treatment.application.internal;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.SchedulePattern;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProcedureExecutionGenerator {
    public List<ProcedureExecution> generateInitialExecutions(Procedure procedure){
        SchedulePattern pattern = procedure.getSchedulePattern();
        LocalDateTime start = procedure.getStartDateTime();

        List<ProcedureExecution> list = new ArrayList<>();
        int max = pattern.getTotalOccurrences() != null
                ? Math.min(3, pattern.getTotalOccurrences())
                : 3;

        for(int i=0; i< max; i++){
            LocalDateTime scheduled = switch (pattern.getType()){
                case DAILY -> start.plusDays((long) i * pattern.getInterval());
                case WEEKLY -> start.plusWeeks((long) i * pattern.getInterval());
                case EVERY_X_HOURS -> start.plusHours((long) i * pattern.getInterval());
                default -> throw new IllegalStateException("Unexpected value");
            };

            list.add(new ProcedureExecution(scheduled, procedure));
        }
        return list;
    }

    public List<LocalDateTime> generateScheduledDatesFor(Procedure procedure) {
        SchedulePattern pattern = procedure.getSchedulePattern();
        LocalDateTime current = procedure.getStartDateTime();

        if (current == null || pattern == null) return List.of();

        List<LocalDateTime> scheduledDates = new ArrayList<>();

        int count = 0;
        while (true) {

            if (pattern.getTotalOccurrences() != null && count >= pattern.getTotalOccurrences()) break;
            if (pattern.getUntilDate() != null && current.toLocalDate().isAfter(pattern.getUntilDate())) break;

            scheduledDates.add(current);
            count++;


            current = switch (pattern.getType()) {
                case DAILY -> current.plusDays(pattern.getInterval());
                case WEEKLY -> current.plusWeeks(pattern.getInterval());
                case EVERY_X_HOURS -> current.plusHours(pattern.getInterval());
                case CUSTOM -> null;
            };

            if (current == null) break;
        }

        return scheduledDates;
    }


}

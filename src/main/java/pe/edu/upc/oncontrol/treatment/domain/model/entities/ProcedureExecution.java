package pe.edu.upc.oncontrol.treatment.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.oncontrol.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ExecutionStatus;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ScheduledWindow;

import java.time.LocalDateTime;

@Entity
public class ProcedureExecution extends AuditableModel {
    @Getter
    @Embedded
    private ScheduledWindow window;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private ExecutionStatus status;
    @Getter
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id", nullable = false)
    private Procedure procedure;

    protected ProcedureExecution() {}

    public ProcedureExecution(LocalDateTime scheduledAt, Procedure procedure){
        this.window = new ScheduledWindow(scheduledAt);
        this.procedure = procedure;
        this.status = ExecutionStatus.PENDING;
        this.completedAt = null;
    }

    public void markCompleted(LocalDateTime actualCompletionTime) {
        if(status != ExecutionStatus.PENDING){
            throw new IllegalStateException("This procedure execution is already completed or cancelled.");
        }
        if(window.isOnTime(actualCompletionTime)){
            this.status = ExecutionStatus.COMPLETED_ON_TIME;
        } else if(window.isRegularized(actualCompletionTime)){
            this.status = ExecutionStatus.REGULARIZED;
        } else {
            throw new IllegalStateException("The execution is out of the allowed range.");
        }
        this.completedAt = actualCompletionTime;
    }

    public void markMissedIfExpired(LocalDateTime now){
        if(status == ExecutionStatus.PENDING && window.isTooLate(now)) {
            this.status = ExecutionStatus.MISSED;
        }
    }

    public LocalDateTime getScheduledAt(){
        return window.getScheduledAt();
    }
}

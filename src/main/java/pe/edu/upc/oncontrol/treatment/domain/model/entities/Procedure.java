package pe.edu.upc.oncontrol.treatment.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oncontrol.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureDescription;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureStatus;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.SchedulePattern;

import java.time.LocalDateTime;

@Entity
@Table(name = "treatment_procedure")
public class Procedure extends AuditableModel {
    @Getter
    @Embedded
    private ProcedureDescription description;
    @Getter
    @Embedded
    SchedulePattern schedulePattern;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "procedure_status", nullable = false)
    private ProcedureStatus procedureStatus;
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;
    @Getter
    @Column(name="start_dateTime")
    private LocalDateTime startDateTime;
    public Procedure(ProcedureDescription description,
                     SchedulePattern schedulePattern,
                     Treatment treatment) {
        this.description = description;
        this.schedulePattern = schedulePattern;
        this.treatment = treatment;
        this.procedureStatus = ProcedureStatus.PENDING;
        this.startDateTime = null;
    }

    public Procedure() {
    }

    public void activate(LocalDateTime startDateTime){
        if(this.procedureStatus != ProcedureStatus.PENDING)
            throw new IllegalStateException("Procedure already activated");
        this.procedureStatus = ProcedureStatus.ACTIVE;
        this.startDateTime = startDateTime;
    }

    public void cancel(){
        this.procedureStatus = ProcedureStatus.CANCELLED;
    }

    public void markAsCompleted() {
        if (this.procedureStatus == ProcedureStatus.ACTIVE) {
            this.procedureStatus = ProcedureStatus.COMPLETED;
        }
    }
}

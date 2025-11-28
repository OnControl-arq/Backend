package pe.edu.upc.oncontrol.treatment.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.oncontrol.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom.CreateSymptomCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.SymptomSeverity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class SymptomLog extends AuditableModel {
    @Getter
    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt; //cuando ocurrrio el sintoma presente o pasado

    @Getter
    @Column(name = "symptom_type", nullable = false, length = 100)
    private String symptomType;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private SymptomSeverity severity;

    @Getter
    @Column(name = "notes", length = 500)
    private String notes;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;

    @Getter
    @Column(name = "patient_profile_uuid", nullable = false)
    private UUID patientProfileUuid;

    protected SymptomLog() {
    }

//    public SymptomLog(CreateSymptomCommand command){
//        this.loggedAt = command.loggedAt();
//        this.symptomType = command.symptomType().trim();
//        this.severity = command.severity();
//        this.notes = command.notes() != null ? command.notes().trim() : null;
//        this.treatment = command.treatment();
//        this.patientProfileUuid = command.patientProfileUuid();
//    }

    public SymptomLog(LocalDateTime loggedAt,
                      String symptomType,
                      SymptomSeverity severity,
                      String notes,
                      Treatment treatment,
                      UUID patientProfileUuid) {
        this.loggedAt = loggedAt;
        this.symptomType = symptomType.trim();
        this.severity = severity;
        this.notes = notes != null ? notes.trim() : null;
        this.treatment = treatment;
        this.patientProfileUuid = patientProfileUuid;
    }

}

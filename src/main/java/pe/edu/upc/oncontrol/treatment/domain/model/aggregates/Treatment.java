package pe.edu.upc.oncontrol.treatment.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.TreatmentPeriod;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.TreatmentStatus;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.TreatmentTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Treatment extends AuditableAbstractAggregateRoot<Treatment> {

    @Getter
    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId;

    @Getter
    @Embedded
    private TreatmentTitle title;

    @Getter
    @Embedded
    private TreatmentPeriod period;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreatmentStatus status;

    @Getter
    @Column(name = "doctor_profile_uuid", nullable = false)
    private UUID doctorProfileUuid;

    @Getter
    @Column(name = "patient_profile_uuid", nullable = false)
    private UUID patientProfileUuid;

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Procedure> procedures;

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SymptomLog> symptomLogs;

    protected Treatment() {
        // JPA
    }

    public Treatment(TreatmentTitle title,
                     TreatmentPeriod period,
                     UUID doctorProfileUuid,
                     UUID patientProfileUuid) {
        this.externalId = UUID.randomUUID();
        this.title = title;
        this.period = period;
        this.status = TreatmentStatus.ACTIVE;
        this.doctorProfileUuid = doctorProfileUuid;
        this.patientProfileUuid = patientProfileUuid;
        this.procedures = new ArrayList<>();
        this.symptomLogs = new ArrayList<>();
    }

    public void addProcedure(Procedure procedure) {
        procedure.setTreatment(this);
        procedures.add(procedure);
    }

    public void logSymptom(SymptomLog log) {
        symptomLogs.add(log);
    }

    public void suspend() {
        this.status = TreatmentStatus.SUSPENDED;
    }

    public void complete() {
        this.status = TreatmentStatus.COMPLETED;
    }

    public void cancel() {
        this.status = TreatmentStatus.CANCELLED;
    }

}

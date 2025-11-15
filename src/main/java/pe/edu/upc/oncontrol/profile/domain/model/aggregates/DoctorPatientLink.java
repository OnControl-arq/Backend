package pe.edu.upc.oncontrol.profile.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.oncontrol.profile.domain.model.commands.link.CreateLinkCommand;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class DoctorPatientLink extends AuditableAbstractAggregateRoot<DoctorPatientLink> {

    @Getter
    @Column(name="external_id", nullable = false, unique = true, updatable = false)
    private UUID externalId;
    @Getter
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctorProfile;
    @Getter
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patientProfile;
    @Getter
    @Enumerated(EnumType.STRING)
    private LinkStatus status;
    @Getter
    private LocalDateTime disabledAt;

    public DoctorPatientLink() {
    }

    public DoctorPatientLink(DoctorProfile doctor, PatientProfile patient) {
        this.externalId = UUID.randomUUID();
        this.doctorProfile = doctor;
        this.patientProfile = patient;
        this.status = LinkStatus.PENDING;
        this.disabledAt = null;
    }

    public void patientAcceptedLink() {
        this.status = LinkStatus.ACCEPTED;
    }

    public void disableLink(){
        this.status = LinkStatus.DISABLED;
        this.disabledAt = LocalDateTime.now();
    }

    public void activateLink() {
        this.status = LinkStatus.ACTIVE;
        this.disabledAt = null;
    }

    public void rejectLink() {
        this.status = LinkStatus.REJECTED;
    }
    public void deleteLink(){
        this.status = LinkStatus.DELETED;
        this.disabledAt = LocalDateTime.now();
    }
}

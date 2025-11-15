package pe.edu.upc.oncontrol.profile.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.CreatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.UpdatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.ContactInfo;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.FullName;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class PatientProfile extends AuditableAbstractAggregateRoot<PatientProfile> {

    @Getter
    @Column(nullable = false, unique = true)
    private UUID uuid;
    @Getter
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    @Getter
    @Embedded
    private FullName fullName;
    @Getter
    @Embedded
    private ContactInfo contactInfo;
    @Getter
    private LocalDate birthDate;
    @Getter
    private String gender;
    @Getter
    private String photoUrl;
    @Getter
    private boolean active;

    public PatientProfile(){

    }

    public PatientProfile(Long userId, CreatePatientProfileCommand command){
        this.uuid = UUID.randomUUID();
        this.userId = userId;
        this.fullName = new FullName(command.firstName(), command.lastName());
        this.contactInfo = new ContactInfo(command.email(), command.phoneNumber());
        this.birthDate = LocalDate.parse(command.birthDate());
        this.gender = command.gender();
        this.photoUrl = command.photoUrl();
        this.active = true;
    }

    public void updateProfile(UpdatePatientProfileCommand command){
        this.fullName = new FullName(command.firstName(), command.lastName());
        this.contactInfo = new ContactInfo(command.email(), command.phoneNumber());
        this.birthDate = LocalDate.parse(command.birthDate());
        this.photoUrl = command.photoUrl();
    }

    public void deactivateProfile() {
        this.active = false;
    }

}

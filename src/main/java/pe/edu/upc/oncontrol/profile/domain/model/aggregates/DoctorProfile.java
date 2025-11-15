package pe.edu.upc.oncontrol.profile.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.CreateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.ContactInfo;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.DocumentIdent;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.FullName;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.UUID;

@Entity
public class DoctorProfile extends AuditableAbstractAggregateRoot<DoctorProfile> {

    @Getter
    @Column(nullable = false, unique = true)
    private UUID uuid;
    @Getter
    @Column(name="user_id", nullable = false, unique = true)
    private Long userId;
    @Getter
    @Embedded
    private FullName fullName;
    @Getter
    @Embedded
    private ContactInfo contactInfo;
    @Getter
    @Embedded
    private DocumentIdent documentIdent;
    @Getter
    private String specialty;
    @Getter
    private String CMPCode;
    @Getter
    private String photoUrl;
    @Getter
    private boolean active;
    @Getter
    private int cantPatients;

    public DoctorProfile(){
    }

    public DoctorProfile(Long userId, CreateDoctorProfileCommand command){
        this.uuid = UUID.randomUUID();
        this.userId = userId;
        this.fullName = new FullName(command.firstName(), command.lastName());
        this.contactInfo = new ContactInfo(command.email(), command.phoneNumber());
        this.documentIdent = new DocumentIdent(command.documentType(), command.documentNumber());
        this.specialty = command.specialty();
        this.CMPCode = command.CMPCode();
        this.photoUrl = command.photoUrl();
        this.active = true;
        this.cantPatients = 0;
    }

    public void incrementPatientCount() {
        this.cantPatients++;
    }

    public void decrementPatientCount() {
        if (this.cantPatients > 0) {
            this.cantPatients--;
        }
    }
    public void updateProfile(UpdateDoctorProfileCommand command){
        this.fullName = new FullName(command.firstName(), command.lastName());
        this.contactInfo = new ContactInfo(command.email(), command.phoneNumber());
        this.specialty = command.specialty();
        this.CMPCode = command.CMPCode();
        this.photoUrl = command.photoUrl();
    }
    public void deactivate() {
        this.active = false;
    }
}

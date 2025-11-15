package pe.edu.upc.oncontrol.profile.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.billing.application.acl.SubscriptionAcl;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.link.*;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;
import pe.edu.upc.oncontrol.profile.domain.services.link.DoctorPatientLinkCommandService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorPatientLinkRepository;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorProfileRepository;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.PatientProfileRepository;

import java.util.Optional;

@Service
public class DoctorPatientLinkCommandServiceImpl implements DoctorPatientLinkCommandService {
    private final DoctorPatientLinkRepository doctorPatientLinkRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    private final SubscriptionAcl subscriptionAcl;
    private final PatientProfileRepository patientProfileRepository;

    public DoctorPatientLinkCommandServiceImpl(DoctorPatientLinkRepository doctorPatientLinkRepository, DoctorProfileRepository doctorProfileRepository, SubscriptionAcl subscriptionAcl, PatientProfileRepository patientProfileRepository) {
        this.doctorPatientLinkRepository = doctorPatientLinkRepository;
        this.doctorProfileRepository = doctorProfileRepository;
        this.subscriptionAcl = subscriptionAcl;
        this.patientProfileRepository = patientProfileRepository;
    }

    @Override
    public DoctorPatientLink createLink(CreateLinkCommand command) {
        DoctorProfile doctor = doctorProfileRepository.findByUuid(command.doctorProfileUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        PatientProfile patient = patientProfileRepository.findByUuid(command.patientProfileUuid())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));


        Optional<DoctorPatientLink> existing = doctorPatientLinkRepository
                .findByDoctorProfileAndPatientProfile(doctor, patient);

        if (existing.isPresent()) {
            LinkStatus currentStatus = existing.get().getStatus();
            if (currentStatus == LinkStatus.REJECTED) {
                throw new IllegalStateException("This patient has already rejected the link and cannot be relinked.");
            }

            throw new IllegalStateException("Link already exists with status: " + currentStatus);
        }

        DoctorPatientLink newLink = new DoctorPatientLink(doctor, patient);
        return doctorPatientLinkRepository.save(newLink);
    }


    @Override
    public void acceptLink(AcceptLinkCommand command) {
        DoctorPatientLink link = doctorPatientLinkRepository.findByExternalId(command.externalId())
                .orElseThrow(() -> new EntityNotFoundException("Link not found"));

        if (link.getStatus() != LinkStatus.PENDING) {
            throw new IllegalStateException("Only pending links can be accepted");
        }

        link.patientAcceptedLink();
        doctorPatientLinkRepository.save(link);
    }


    @Override
    public void rejectLink(RejectLinkCommand command) {
        DoctorPatientLink link = doctorPatientLinkRepository.findByExternalId(command.externalId())
                .orElseThrow(() -> new EntityNotFoundException("Link not found"));

        if (link.getStatus() != LinkStatus.PENDING) {
            throw new IllegalStateException("Only pending links can be rejected");
        }

        link.rejectLink();
        doctorPatientLinkRepository.save(link);
    }


    @Override
    public void activateLink(ActivateLinkCommand command) {
        DoctorPatientLink link = doctorPatientLinkRepository.findByExternalId(command.externalId())
                .orElseThrow(() -> new EntityNotFoundException("Link not found"));

        DoctorProfile doctor = link.getDoctorProfile();

        Plan plan = subscriptionAcl.getActivePlanByAdminId(doctor.getUserId())
                .orElseThrow(() -> new IllegalStateException("No active plan for doctor"));

        int maxPatientsAllowed = plan.getMaxPatients();

        int activePatients = doctorPatientLinkRepository.countByDoctorProfileAndStatus(doctor, LinkStatus.ACTIVE);

        if (activePatients >= maxPatientsAllowed) {
            throw new IllegalStateException("Doctor has reached the max number of active patients");
        }

        link.activateLink();
        doctorPatientLinkRepository.save(link);
    }


    @Override
    public void disableLink(DisableLinkCommand command) {
        DoctorPatientLink link = doctorPatientLinkRepository.findByExternalId(command.externalId())
                .orElseThrow(() -> new EntityNotFoundException("Link not found"));

        if (link.getStatus() != LinkStatus.ACTIVE) {
            throw new IllegalStateException("Only active links can be disabled");
        }

        link.disableLink();
        doctorPatientLinkRepository.save(link);
    }

    @Override
    public void deleteLink(DeleteLinkCommand command) {
        DoctorPatientLink link = doctorPatientLinkRepository.findByExternalId(command.externalId())
                .orElseThrow(() -> new EntityNotFoundException("Link not found"));
        if(link.getStatus() == LinkStatus.PENDING){
            doctorPatientLinkRepository.delete(link);
        } else  if (link.getStatus() == LinkStatus.DELETED) {
            throw new IllegalStateException("Link is already deleted");
        } else {
            link.deleteLink();
            doctorPatientLinkRepository.save(link);
        }
    }

}

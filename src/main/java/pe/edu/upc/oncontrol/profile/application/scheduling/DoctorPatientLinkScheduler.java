package pe.edu.upc.oncontrol.profile.application.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.billing.application.acl.SubscriptionAcl;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorPatientLinkRepository;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorProfileRepository;

import java.util.List;

@Service
public class DoctorPatientLinkScheduler {
    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorPatientLinkRepository doctorPatientLinkRepository;
    private final SubscriptionAcl subscriptionAcl;

    public DoctorPatientLinkScheduler(DoctorProfileRepository doctorProfileRepository, DoctorPatientLinkRepository doctorPatientLinkRepository, SubscriptionAcl subscriptionAcl) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.doctorPatientLinkRepository = doctorPatientLinkRepository;
        this.subscriptionAcl = subscriptionAcl;
    }

    @Scheduled(cron = " 0 0 1 * * ?")
    public void disableLinksForDoctorsWithoutActiveSubscription(){
        List<DoctorProfile> allDoctors = doctorProfileRepository.findAll();

        for (DoctorProfile doctor : allDoctors) {
            boolean hasActivePlan = subscriptionAcl
                    .getActivePlanByAdminId(doctor.getUserId())
                    .isPresent();

            if (!hasActivePlan) {
                List<DoctorPatientLink> activeLinks =
                        doctorPatientLinkRepository.findAllByDoctorProfileAndStatus(doctor, LinkStatus.ACTIVE);

                for (DoctorPatientLink link : activeLinks) {
                    link.disableLink();
                    doctorPatientLinkRepository.save(link);
                }
            }
        }
    }
}

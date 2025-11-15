package pe.edu.upc.oncontrol.billing.application.scheduling.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PaymentMethodRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeleteOldInactivePaymentMethodsTask {
    private final PaymentMethodRepository paymentMethodRepository;

    public DeleteOldInactivePaymentMethodsTask(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Scheduled(cron = "0 30 0 * * *") // Runs every day at 00:30
    public void deleteInactiveMethods() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        List<PaymentMethod> toDelete = paymentMethodRepository.findByActiveFalseAndDeactivatedAtBefore(cutoff);
        if (!toDelete.isEmpty()) {
            paymentMethodRepository.deleteAll(toDelete);
            System.out.println("Deleted " + toDelete.size() + " old inactive payment methods");
        }
    }

}

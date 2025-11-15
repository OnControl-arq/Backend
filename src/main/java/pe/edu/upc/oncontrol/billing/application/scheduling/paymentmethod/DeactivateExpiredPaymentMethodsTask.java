package pe.edu.upc.oncontrol.billing.application.scheduling.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.DeactivationReason;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PaymentMethodRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;

@Component
public class DeactivateExpiredPaymentMethodsTask {
    private final PaymentMethodRepository paymentMethodRepository;

    public DeactivateExpiredPaymentMethodsTask(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }
    @Scheduled(cron = "0 0 0 1 * *") // Runs on the first day of every month at midnight
    public void deactivateExpiredCards() {
        YearMonth current = YearMonth.now().plusMonths(1);
        int currentYear = current.getYear();
        int currentMonth = current.getMonthValue();

        List<PaymentMethod> expiredMethods = paymentMethodRepository.findExpiredMethods(currentYear, currentMonth);

        for (PaymentMethod method : expiredMethods) {
            method.deactivate(DeactivationReason.EXPIRED_BY_SYSTEM);
        }

        paymentMethodRepository.saveAll(expiredMethods);
    }

}

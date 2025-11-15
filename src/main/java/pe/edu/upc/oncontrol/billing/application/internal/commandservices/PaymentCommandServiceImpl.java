package pe.edu.upc.oncontrol.billing.application.internal.commandservices;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Payment;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsFailedCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsPaidCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.RegisterPaymentCommand;
import pe.edu.upc.oncontrol.billing.domain.services.iam.IAMService;
import pe.edu.upc.oncontrol.billing.domain.services.payment.PaymentCommandService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository, SubscriptionRepository subscriptionRepository, IAMService iamService) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Payment register(RegisterPaymentCommand command) {
        Subscription subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(() ->  new IllegalArgumentException("Subscription not found"));

        Payment payment = Payment.create(command, subscription);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment markAsPaid(MarkPaymentAsPaidCommand command) {
        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.markAsPaid();
        return paymentRepository.save(payment);
    }

    @Override
    public Payment markAsFailed(MarkPaymentAsFailedCommand command) {
        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalThreadStateException("Payment not found"));
        payment.markAsFailed(command.errorMessage());
        return paymentRepository.save(payment);
    }
}

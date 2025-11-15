package pe.edu.upc.oncontrol.billing.interfaces.rest;

import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsFailedCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsPaidCommand;
import pe.edu.upc.oncontrol.billing.domain.services.payment.PaymentCommandService;
import pe.edu.upc.oncontrol.billing.domain.services.payment.PaymentQueryService;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.payment.PaymentResource;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment.MarkPaymentAsFailedCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment.MarkPaymentAsPaidCommandFromResourceAssembler;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment.PaymentResourceFromEntityAssembler;
import pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment.RegisterPaymentCommandFromResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/payments/", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;

    public PaymentController(PaymentCommandService paymentCommandService, PaymentQueryService paymentQueryService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
    }

    @PostMapping
    public ResponseEntity<PaymentResource> register(@RequestBody PaymentResource resource){
        var command = RegisterPaymentCommandFromResourceAssembler.toCommandFromResource(resource);
        var payment = paymentCommandService.register(command);
        var response = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/mark-paid")
    public ResponseEntity<PaymentResource> markAsPaid(@RequestBody MarkPaymentAsPaidCommand resource){
        var command = MarkPaymentAsPaidCommandFromResourceAssembler.toCommandFromResource(resource);
        var payment = paymentCommandService.markAsPaid(command);
        var response = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/mark-failed")
    public ResponseEntity<PaymentResource> markAsFailed(@RequestBody MarkPaymentAsFailedCommand resource){
        var command = MarkPaymentAsFailedCommandFromResourceAssembler.toCommandFromResource(resource);
        var payment = paymentCommandService.markAsFailed(command);
        var response = PaymentResourceFromEntityAssembler.toResourceFromEntity(payment);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResource> getById(@PathVariable Long paymentId){
        return paymentQueryService.getById(paymentId)
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-subscription/{subscriptionId}")
    public ResponseEntity<List<PaymentResource>> getBySubscription(@PathVariable Long subscriptionId){
        var payments = paymentQueryService.getBySubscriptionId(subscriptionId)
                .stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(payments);
    }

}

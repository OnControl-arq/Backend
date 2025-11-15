package pe.edu.upc.oncontrol.billing.domain.model.commands.payment;

public record MarkPaymentAsFailedCommand(Long paymentId, String errorMessage) {}

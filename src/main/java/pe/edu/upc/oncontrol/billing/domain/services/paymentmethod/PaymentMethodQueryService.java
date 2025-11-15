package pe.edu.upc.oncontrol.billing.domain.services.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodQueryService {
    Optional<PaymentMethod> getById(Long methodId, HttpServletRequest request);
    List<PaymentMethod> getAllByAdminId(Long adminId, HttpServletRequest request);
    Optional<PaymentMethod> getDefaultByAdminId(Long adminId,  HttpServletRequest request);
}

package pe.edu.upc.oncontrol.billing.application.internal.queryservices;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import pe.edu.upc.oncontrol.billing.domain.services.paymentmethod.PaymentMethodQueryService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PaymentMethodRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PaymentMethodQueryServiceImpl implements PaymentMethodQueryService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final TokenContextFacade tokenContextFacade;

    public PaymentMethodQueryServiceImpl(PaymentMethodRepository paymentMethodRepository, TokenContextFacade tokenContextFacade) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.tokenContextFacade = tokenContextFacade;
    }

    @Override
    public Optional<PaymentMethod> getById(Long methodId, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long requesterId = tokenContextFacade.extractUserIdFromToken(request);
        return paymentMethodRepository.findById(methodId)
                .filter(method -> "ROLE_SUPER_ADMIN".equals(role) || method.getAdminId().equals(requesterId));
    }

    @Override
    public List<PaymentMethod> getAllByAdminId(Long adminId, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long requesterId = tokenContextFacade.extractUserIdFromToken(request);
        if (!"ROLE_SUPER_ADMIN".equals(role) && !adminId.equals(requesterId)) {
            throw new SecurityException("Access denied: You are not allowed to view payment methods of another admin.");
        }
        return paymentMethodRepository.findAllByAdminId(adminId);
    }

    @Override
    public Optional<PaymentMethod> getDefaultByAdminId(Long adminId, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long requesterId = tokenContextFacade.extractUserIdFromToken(request);
        return paymentMethodRepository.findByAdminIdAndActiveTrueAndDefaultMethodTrue(adminId)
                .filter(method -> "ROLE_SUPER_ADMIN".equals(role) || method.getAdminId().equals(requesterId));
    }
}

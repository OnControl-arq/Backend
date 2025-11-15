package pe.edu.upc.oncontrol.billing.application.internal.commandservices;

import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.ActivePaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.AddPaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.DeactivatePaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.SetDefaultPaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.DeactivationReason;
import pe.edu.upc.oncontrol.billing.domain.services.paymentmethod.PaymentMethodCommandService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PaymentMethodRepository;

import pe.edu.upc.oncontrol.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.oncontrol.iam.interfaces.acl.IamContextFacade;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodCommandServiceImpl implements PaymentMethodCommandService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final TokenContextFacade tokenContextFacade;
    private final IamContextFacade iamContextFacade;

    public PaymentMethodCommandServiceImpl(PaymentMethodRepository paymentMethodRepository, TokenContextFacade tokenContextFacade, IamContextFacade iamContextFacade) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.tokenContextFacade = tokenContextFacade;
        this.iamContextFacade = iamContextFacade;
    }

    private boolean validAdmin(Long userId){
        return iamContextFacade.isValidUserWithRole(userId, Roles.valueOf("ROLE_ADMIN"));
    }

    private Long resolveUserId(HttpServletRequest request, Long adminId) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        if ("ROLE_SUPER_ADMIN".equals(role) && adminId!=null && adminId > 0) {
            if(!validAdmin(adminId)) throw new IllegalArgumentException("Invalid admin ID provided");
            return adminId;
        } else if ("ROLE_ADMIN".equals(role)) {
            return tokenContextFacade.extractUserIdFromToken(request);
        } else {
            throw new IllegalArgumentException("Unauthorized role to add a payment method or resolve user ID");
        }
    }

    @Override
    public PaymentMethod add(AddPaymentMethodCommand command, HttpServletRequest request) {
        Long userId = resolveUserId(request, command.adminId());
        PaymentMethod paymentMethod = new PaymentMethod(userId, command);
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void setDefault(SetDefaultPaymentMethodCommand command, HttpServletRequest request) {
        Long adminId = tokenContextFacade.extractUserIdFromToken(request);

        paymentMethodRepository.findById(command.methodId()).ifPresentOrElse(method -> {
            if(!method.getAdminId().equals(adminId)) {
                throw new SecurityException("Is not allowed to set this payment method as default");
            }
            List<PaymentMethod> methods = paymentMethodRepository.findAllByAdminId(adminId);
            for(PaymentMethod m : methods){
                if(m.getId().equals(command.methodId())){
                    if(!m.isActive()){
                        throw new IllegalStateException("Cannot set an inactive payment method as default");
                    }
                    m.setDefaultMethod(true);
                } else {
                    m.setDefaultMethod(false);
                }
            }
            paymentMethodRepository.saveAll(methods);
        }, () -> {
            throw new IllegalArgumentException("Payment method not found");
        });
    }

    @Override
    public void deactivate(DeactivatePaymentMethodCommand command, HttpServletRequest request) {
        paymentMethodRepository.findById(command.methodId()).ifPresentOrElse(method -> {
            String role = tokenContextFacade.extractUserRoleFromRequest(request);
            Long userId = tokenContextFacade.extractUserIdFromToken(request);

            boolean isSuperAdmin = "ROLE_SUPER_ADMIN".equals(role);
            boolean isOwner = method.getAdminId().equals(userId);

            if (!isSuperAdmin && !isOwner) {
                throw new SecurityException("Is not allowed to deactivate this payment method");
            }

            if(isSuperAdmin){
                method.deactivate(DeactivationReason.REVOKED_BY_SUPER_ADMIN);
            } else {
                method.deactivate(DeactivationReason.DELETED_BY_USER);
            }

            paymentMethodRepository.save(method);
        }, () -> {
            throw new IllegalArgumentException("Payment method not found");
        });
    }

    @Override
    public void active(ActivePaymentMethodCommand command) {
        paymentMethodRepository.findById(command.methodId()).ifPresentOrElse(method -> {
            method.activate();
            paymentMethodRepository.save(method);
        }, () -> {
            throw new IllegalArgumentException("Payment method not found");
        });
    }
}

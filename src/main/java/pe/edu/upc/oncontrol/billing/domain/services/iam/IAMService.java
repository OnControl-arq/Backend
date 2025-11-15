package pe.edu.upc.oncontrol.billing.domain.services.iam;

public interface IAMService {
    boolean isValidUserWithRole(Long userId, String role);
}

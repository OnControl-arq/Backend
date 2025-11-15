package pe.edu.upc.oncontrol.billing.domain.model.valueobjects;

public enum DeactivationReason {
    NONE,
    DELETED_BY_USER,
    EXPIRED_BY_SYSTEM,
    REVOKED_BY_SUPER_ADMIN,
    PAYMENT_FAILED,
    SUSPICIOUS_ACTIVITY,
    DUPLICATE_METHOD,
    INCOMPLETE_SETUP,
}

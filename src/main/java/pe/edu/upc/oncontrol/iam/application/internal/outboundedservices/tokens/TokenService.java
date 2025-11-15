package pe.edu.upc.oncontrol.iam.application.internal.outboundedservices.tokens;

public interface TokenService {
    String generateToken(String username, String role, Long userId);
    String getUsernameFromToken(String token);
    Long getUserIdFromToken(String token);
    String getRoleFromToken(String token);
    boolean validateToken(String token);

}

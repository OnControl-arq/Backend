package pe.edu.upc.oncontrol.iam.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import pe.edu.upc.oncontrol.iam.application.internal.outboundedservices.tokens.TokenService;


public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest token);
//    String generateToken(Authentication authentication);
}
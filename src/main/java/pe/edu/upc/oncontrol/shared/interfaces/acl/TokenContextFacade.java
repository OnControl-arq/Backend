package pe.edu.upc.oncontrol.shared.interfaces.acl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.iam.infrastructure.tokens.jwt.BearerTokenService;

@Service
public class TokenContextFacade {
    private final BearerTokenService bearerTokenService;

    public TokenContextFacade(BearerTokenService bearerTokenService) {
        this.bearerTokenService = bearerTokenService;
    }

    public Long extractUserIdFromToken(HttpServletRequest request){
        String token = bearerTokenService.getBearerTokenFrom(request);
        if(token == null) throw new IllegalArgumentException("JWT token is missing");
        return bearerTokenService.getUserIdFromToken(token);
    }

    public String extractUserRoleFromRequest(HttpServletRequest request){
        String token = bearerTokenService.getBearerTokenFrom(request);
        if(token == null) throw new IllegalArgumentException("JWT token is missing");
        return bearerTokenService.getRoleFromToken(token);
    }
}


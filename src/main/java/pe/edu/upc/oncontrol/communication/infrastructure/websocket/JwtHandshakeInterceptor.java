package pe.edu.upc.oncontrol.communication.infrastructure.websocket;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenContextFacade tokenContextFacade;

    public JwtHandshakeInterceptor(TokenContextFacade tokenContextFacade) {
        this.tokenContextFacade = tokenContextFacade;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            try {
                Long userId = tokenContextFacade.extractUserIdFromToken(httpServletRequest);
                String role = tokenContextFacade.extractUserRoleFromRequest(httpServletRequest);

                attributes.put("userId", userId);
                attributes.put("role", role);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}

package pe.edu.upc.oncontrol.communication.infrastructure.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.iam.infrastructure.tokens.jwt.BearerTokenService;

import java.util.Objects;

@Component
public class JwtStompChannelInterceptor implements ChannelInterceptor {

    private final BearerTokenService bearerTokenService;

    public JwtStompChannelInterceptor(BearerTokenService bearerTokenService) {
        this.bearerTokenService = bearerTokenService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Authorization header is missing or invalid");
            }

            String jwt = authHeader.substring(7);
            if (!bearerTokenService.validateToken(jwt)) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            Long userId = bearerTokenService.getUserIdFromToken(jwt);
            String role = bearerTokenService.getRoleFromToken(jwt);

            Objects.requireNonNull(accessor.getSessionAttributes()).put("userId", userId);
            accessor.getSessionAttributes().put("role", role);
        }

        return message;
    }


}

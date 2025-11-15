package pe.edu.upc.oncontrol.communication.interfaces.rest;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import pe.edu.upc.oncontrol.communication.domain.model.commands.SendChatMessageCommand;
import pe.edu.upc.oncontrol.communication.domain.services.ChatMessageCommandService;
import pe.edu.upc.oncontrol.communication.interfaces.rest.resources.ChatMessageRequest;

import java.util.Objects;
import java.util.UUID;


@Controller
public class ChatWebSocketController {
    private final ChatMessageCommandService chatMessageCommandService;


    public ChatWebSocketController(ChatMessageCommandService chatMessageCommandService) {
        this.chatMessageCommandService = chatMessageCommandService;
    }

    @MessageMapping("/chat/{doctorUuid}/{patientUuid}/send")
    public void receiveMessage(@DestinationVariable UUID doctorUuid,
                               @DestinationVariable UUID patientUuid,
                               ChatMessageRequest request,
                               SimpMessageHeaderAccessor headerAccessor) {

        String role = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("role");


        SendChatMessageCommand command = new SendChatMessageCommand(
                doctorUuid,
                patientUuid,
                role,
                request.getContent(),
                request.getType(),
                request.getFileUrl()
        );

        chatMessageCommandService.handle(command);
    }



}

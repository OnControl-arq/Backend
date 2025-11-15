package pe.edu.upc.oncontrol.communication.domain.services;

import pe.edu.upc.oncontrol.communication.domain.model.commands.SendChatMessageCommand;

public interface ChatMessageCommandService {
    void handle(SendChatMessageCommand command);
}

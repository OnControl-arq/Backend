package pe.edu.upc.oncontrol.communication.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oncontrol.communication.domain.model.valueobjects.MessageType;

@Getter
@Setter
public class ChatMessageRequest {
    @Size(max = 1000, message = "Content must be at most 1000 characters long")
    private String content;
    @NotNull(message = "Message type cannot be null")
    private MessageType type;
    private String fileUrl;
}

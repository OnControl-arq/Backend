package pe.edu.upc.oncontrol.communication.domain.services;

import org.springframework.data.domain.Page;
import pe.edu.upc.oncontrol.communication.domain.model.aggregates.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatMessageQueryService {
    List<ChatMessage> getConversation(UUID doctorUuid, UUID patientUuid);
    Page<ChatMessage> getConversationPaged(UUID doctorUuid, UUID patientUuid, int page, int size);
}

package pe.edu.upc.oncontrol.communication.application.internal.queryserice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.communication.domain.model.aggregates.ChatMessage;
import pe.edu.upc.oncontrol.communication.domain.services.ChatMessageQueryService;
import pe.edu.upc.oncontrol.communication.infrastructure.persisntence.jpa.repositories.ChatMessageRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageQueryServiceImpl implements ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageQueryServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public List<ChatMessage> getConversation(UUID doctorUuid, UUID patientUuid) {
        return chatMessageRepository.findByDoctorUuidAndPatientUuidOrderByCreatedAtAsc(doctorUuid, patientUuid);
    }

    @Override
    public Page<ChatMessage> getConversationPaged(UUID doctorUuid, UUID patientUuid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatMessageRepository.findByDoctorUuidAndPatientUuidOrderByCreatedAtDesc(doctorUuid, patientUuid, pageable);
    }
}

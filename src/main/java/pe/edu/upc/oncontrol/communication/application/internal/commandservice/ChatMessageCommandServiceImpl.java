package pe.edu.upc.oncontrol.communication.application.internal.commandservice;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.billing.application.acl.SubscriptionAcl;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.communication.domain.model.aggregates.ChatMessage;
import pe.edu.upc.oncontrol.communication.domain.model.commands.SendChatMessageCommand;
import pe.edu.upc.oncontrol.communication.domain.services.ChatMessageCommandService;
import pe.edu.upc.oncontrol.communication.infrastructure.persisntence.jpa.repositories.ChatMessageRepository;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;

import java.util.Optional;

@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProfileAccessAcl profileAccessAcl;
    private final SubscriptionAcl subscriptionAcl;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageCommandServiceImpl(ChatMessageRepository chatMessageRepository, ProfileAccessAcl profileAccessAcl, SubscriptionAcl subscriptionAcl, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.profileAccessAcl = profileAccessAcl;
        this.subscriptionAcl = subscriptionAcl;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void handle(SendChatMessageCommand command) {
        boolean linkActive = profileAccessAcl.isLinkActive(command.doctorUuid(), command.patientUuid());
        if (!linkActive) throw new IllegalStateException("Patient and Doctor connection is not active.");

        Optional<Plan> plan = subscriptionAcl.getActivePlanByAdminIdFromUuid(command.doctorUuid());
        if (plan.isEmpty() || !plan.get().isMessagingEnabled()) {
            throw new IllegalStateException("Doctor plan does not allow messaging.");
        }


        ChatMessage message = new ChatMessage();
        message.setDoctorUuid(command.doctorUuid());
        message.setPatientUuid(command.patientUuid());
        message.setSenderRole(command.senderRole());
        message.setContent(command.content());
        message.setType(command.type());
        message.setFileUrl(command.fileUrl());
        message.setSeen(false);

        chatMessageRepository.save(message);

        String destination = "/topic/chat." + command.doctorUuid() + "." + command.patientUuid();
        messagingTemplate.convertAndSend(destination, message);
    }
}

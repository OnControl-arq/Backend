package pe.edu.upc.oncontrol.communication.application.internal.commandservice;

import org.springframework.amqp.rabbit.core.RabbitTemplate; // <--- Importar
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.billing.application.acl.SubscriptionAcl;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.communication.domain.model.aggregates.ChatMessage;
import pe.edu.upc.oncontrol.communication.domain.model.commands.SendChatMessageCommand;
import pe.edu.upc.oncontrol.communication.domain.services.ChatMessageCommandService;
import pe.edu.upc.oncontrol.communication.infrastructure.persisntence.jpa.repositories.ChatMessageRepository;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;
import pe.edu.upc.oncontrol.shared.infraestructure.broker.RabbitMQConfig; // <--- Importar Config

import java.util.Map;
import java.util.Optional;

@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProfileAccessAcl profileAccessAcl;
    private final SubscriptionAcl subscriptionAcl;
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitTemplate rabbitTemplate; // <--- Inyección del Broker

    public ChatMessageCommandServiceImpl(ChatMessageRepository chatMessageRepository,
                                         ProfileAccessAcl profileAccessAcl,
                                         SubscriptionAcl subscriptionAcl,
                                         SimpMessagingTemplate messagingTemplate,
                                         RabbitTemplate rabbitTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.profileAccessAcl = profileAccessAcl;
        this.subscriptionAcl = subscriptionAcl;
        this.messagingTemplate = messagingTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void handle(SendChatMessageCommand command) {
        // 1. Validaciones
        boolean linkActive = profileAccessAcl.isLinkActive(command.doctorUuid(), command.patientUuid());
        if (!linkActive) throw new IllegalStateException("Patient and Doctor connection is not active.");

        Optional<Plan> plan = subscriptionAcl.getActivePlanByAdminIdFromUuid(command.doctorUuid());
        if (plan.isEmpty() || !plan.get().isMessagingEnabled()) {
            throw new IllegalStateException("Doctor plan does not allow messaging.");
        }

        // 2. Guardar en BD (Operación Crítica)
        ChatMessage message = new ChatMessage();
        message.setDoctorUuid(command.doctorUuid());
        message.setPatientUuid(command.patientUuid());
        message.setSenderRole(command.senderRole());
        message.setContent(command.content());
        message.setType(command.type());
        message.setFileUrl(command.fileUrl());
        message.setSeen(false);

        chatMessageRepository.save(message);

        // 3. Enviar por WebSocket (Tiempo Real)
        String destination = "/topic/chat." + command.doctorUuid() + "." + command.patientUuid();
        messagingTemplate.convertAndSend(destination, message);

        // 4. ENVIAR AL BROKER (Disponibilidad)
        // Esta parte es asíncrona. Si falla, el usuario NO recibe error.
        try {
            String recipientUuid = command.senderRole().equals("DOCTOR")
                    ? command.patientUuid().toString()
                    : command.doctorUuid().toString();

            // Datos para la notificación
            var eventData = Map.of(
                    "type", "CHAT_MESSAGE_SENT",
                    "recipientUuid", recipientUuid,
                    "content", command.content() != null ? command.content() : "[Archivo]"
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_MAIN,
                    RabbitMQConfig.ROUTING_KEY_CHAT,
                    eventData
            );
            System.out.println("🐰 [Broker] Mensaje encolado para notificar a: " + recipientUuid);

        } catch (Exception e) {
            System.err.println("⚠️ Broker no disponible. El chat sigue funcionando, pero no hubo notificación.");
        }
    }
}
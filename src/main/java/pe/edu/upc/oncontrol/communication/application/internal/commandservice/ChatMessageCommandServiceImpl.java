package pe.edu.upc.oncontrol.communication.application.internal.commandservice;

import org.springframework.amqp.rabbit.core.RabbitTemplate; // <--- Importar RabbitTemplate
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.billing.application.acl.SubscriptionAcl;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.communication.domain.model.aggregates.ChatMessage;
import pe.edu.upc.oncontrol.communication.domain.model.commands.SendChatMessageCommand;
import pe.edu.upc.oncontrol.communication.domain.services.ChatMessageCommandService;
import pe.edu.upc.oncontrol.communication.infrastructure.persisntence.jpa.repositories.ChatMessageRepository;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;
import pe.edu.upc.oncontrol.shared.infraestructure.broker.RabbitMQConfig; // <--- Importar tu Config

import java.util.Map;
import java.util.Optional;

@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProfileAccessAcl profileAccessAcl;
    private final SubscriptionAcl subscriptionAcl;
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitTemplate rabbitTemplate; // <--- Inyección del Broker

    // Constructor actualizado con RabbitTemplate
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
        // 1. Validaciones (Igual que antes)
        boolean linkActive = profileAccessAcl.isLinkActive(command.doctorUuid(), command.patientUuid());
        if (!linkActive) throw new IllegalStateException("Patient and Doctor connection is not active.");

        Optional<Plan> plan = subscriptionAcl.getActivePlanByAdminIdFromUuid(command.doctorUuid());
        if (plan.isEmpty() || !plan.get().isMessagingEnabled()) {
            throw new IllegalStateException("Doctor plan does not allow messaging.");
        }

        // 2. Guardar mensaje en Base de Datos
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

        // 4. PUBLICAR EVENTO AL BROKER (Disponibilidad/Interoperabilidad)
        // Esto permite enviar notificaciones push/email de forma asíncrona
        try {
            // Determinamos quién debe recibir la notificación (el otro usuario)
            String recipientUuid = command.senderRole().equals("DOCTOR")
                    ? command.patientUuid().toString()
                    : command.doctorUuid().toString();

            // Creamos un resumen del mensaje para la notificación
            String preview = command.content() != null && command.content().length() > 40
                    ? command.content().substring(0, 40) + "..."
                    : command.content();

            // Creamos el objeto del evento
            var eventData = Map.of(
                    "type", "CHAT_MESSAGE_SENT",
                    "recipientUuid", recipientUuid,
                    "preview", preview != null ? preview : "Archivo adjunto"
            );

            // Enviamos al Exchange usando la Routing Key definida en la config
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_MAIN,
                    RabbitMQConfig.ROUTING_KEY_CHAT,
                    eventData
            );

            System.out.println("🐰 [Broker] Notificación de chat enviada para: " + recipientUuid);

        } catch (Exception e) {
            // Si el broker falla, solo mostramos el error. El chat NO se rompe.
            System.err.println("⚠️ Error enviando al broker: " + e.getMessage());
        }
    }
}
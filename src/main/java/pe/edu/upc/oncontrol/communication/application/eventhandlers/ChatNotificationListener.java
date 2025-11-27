package pe.edu.upc.oncontrol.communication.application.eventhandlers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.shared.infraestructure.broker.RabbitMQConfig;

import java.util.Map;

@Component
public class ChatNotificationListener {

    // Escucha la cola "oncontrol.chat.queue"
    @RabbitListener(queues = RabbitMQConfig.QUEUE_CHAT)
    public void handleChatMessage(Map<String, Object> event) {
        System.out.println("📥 [Consumer] Procesando mensaje asíncrono...");

        if ("CHAT_MESSAGE_SENT".equals(event.get("type"))) {
            String recipient = (String) event.get("recipientUuid");

            // Simulamos un proceso pesado (ej. enviar email o push notification)
            try {
                // Simula 2 segundos de espera.
                // Gracias al broker, el usuario NO siente esta espera.
                Thread.sleep(2000);
                System.out.println("✅ [Notificación] Aviso enviado exitosamente al usuario: " + recipient);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
package pe.edu.upc.oncontrol.communication.application.eventhandlers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.shared.infraestructure.broker.RabbitMQConfig;

import java.util.Map;

@Component
public class ChatNotificationListener {

    // Esta anotación hace que este método se ejecute automáticamente
    // cada vez que llega un mensaje a la cola "oncontrol.chat.queue"
    @RabbitListener(queues = RabbitMQConfig.QUEUE_CHAT)
    public void handleChatMessage(Map<String, Object> event) {

        System.out.println("📥 [Consumer] Evento recibido del Broker: " + event);

        if ("CHAT_MESSAGE_SENT".equals(event.get("type"))) {
            String recipient = (String) event.get("recipientUuid");
            String preview = (String) event.get("preview");

            // --- LÓGICA DE INTEROPERABILIDAD SIMULADA ---
            // Aquí conectaríamos con Firebase (FCM) o Amazon SES
            try {
                System.out.println("   ☁️ [Interop] Conectando con Firebase Cloud Messaging...");
                Thread.sleep(1000); // Simulamos 1 segundo de latencia de red
                System.out.println("   📲 [PUSH] Enviado a usuario " + recipient + ": 'Tienes un nuevo mensaje: " + preview + "'");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
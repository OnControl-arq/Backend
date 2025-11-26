package pe.edu.upc.oncontrol.shared.infraestructure.broker;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // --- CONFIGURACIÓN PARA CHAT ---
    // Nombre de la cola donde se guardarán los mensajes
    public static final String QUEUE_CHAT = "oncontrol.chat.queue";

    // Nombre del Exchange (el distribuidor)
    public static final String EXCHANGE_MAIN = "oncontrol.main.exchange";

    // La "etiqueta" o routing key para identificar mensajes de chat
    public static final String ROUTING_KEY_CHAT = "chat.message.sent";

    // 1. Crear la Cola (Durable = true para que no se borre al reiniciar)
    @Bean
    public Queue chatQueue() {
        return new Queue(QUEUE_CHAT, true);
    }

    // 2. Crear el Exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_MAIN);
    }

    // 3. Unir la Cola al Exchange
    @Bean
    public Binding binding(Queue chatQueue, TopicExchange exchange) {
        return BindingBuilder.bind(chatQueue).to(exchange).with(ROUTING_KEY_CHAT);
    }

    // 4. Configurar el convertidor para enviar objetos como JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
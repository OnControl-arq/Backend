package pe.edu.upc.oncontrol.shared.infraestructure.broker;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // --- CONFIGURACIÓN PARA CHAT ---
    public static final String QUEUE_CHAT = "oncontrol.chat.queue";
    public static final String EXCHANGE_MAIN = "oncontrol.main.exchange";
    public static final String ROUTING_KEY_CHAT = "chat.message.sent";

    // 1. Cola Durable (No se pierden mensajes si se va la luz)
    @Bean
    public Queue chatQueue() {
        return new Queue(QUEUE_CHAT, true);
    }

    // 2. Exchange (El distribuidor)
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_MAIN);
    }

    // 3. Binding (Unión de la cola al exchange)
    @Bean
    public Binding binding(Queue chatQueue, TopicExchange exchange) {
        return BindingBuilder.bind(chatQueue).to(exchange).with(ROUTING_KEY_CHAT);
    }

    // 4. Convertidor para enviar JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
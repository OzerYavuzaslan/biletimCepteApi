package com.biletimcepte.configuration;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RabbitMQConfiguration {
    private final String queueName = "notification";
    private final String exchange = "notification.exchange";
    private final String routing="notification.routing";

    @Bean
    public Queue queue() {
        return new Queue(getQueueName(), false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(getExchange());
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(getRouting() + ".#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

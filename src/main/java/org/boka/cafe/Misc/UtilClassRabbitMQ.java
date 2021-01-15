package org.boka.cafe.Misc;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

public class UtilClassRabbitMQ {

    public void sendMessageToRabbit(String exchange, String routingKey, Object message) {
        getRabbitTemplate().convertAndSend(exchange, routingKey, message);
    }

    private RabbitAdmin getRabbitAdmin() {
        return Misc.getApplicationContext().getBean(RabbitAdmin.class);
    }

    private RabbitTemplate getRabbitTemplate() {
        RabbitTemplate rabbitTemplate = this.getRabbitAdmin().getRabbitTemplate();
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        return rabbitTemplate;
    }
}

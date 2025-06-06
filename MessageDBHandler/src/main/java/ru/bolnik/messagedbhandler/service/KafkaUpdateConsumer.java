package ru.bolnik.messagedbhandler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaUpdateConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUpdateConsumer.class);

    @Value("${kafka.topic.telegram-updates}")
    private String telegramUpdatesTopic;

    // Подписываемся на топик из application.properties
    @KafkaListener(topics = "${kafka.topic.telegram-updates}", groupId = "console-group")
    public void consume(String message) {
        logger.info("Получено сообщение из Kafka топика '{}': {}", telegramUpdatesTopic, message);
    }
}

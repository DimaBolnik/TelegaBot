package ru.bolnik.dispatcher.service;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class KafkaUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUpdateService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.telegram-updates}")
    private String telegramUpdatesTopic;

    public KafkaUpdateService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Метод отправляет update в Kafka
     */
    public void sendUpdateToKafka(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            String jsonUpdate = String.format("{\"chatId\": \"%d\", \"text\": \"%s\"}", chatId, messageText);

            kafkaTemplate.send(telegramUpdatesTopic, jsonUpdate);
            logger.info("Отправлено в Kafka topic '{}': {}", telegramUpdatesTopic, jsonUpdate);
        }
    }
}
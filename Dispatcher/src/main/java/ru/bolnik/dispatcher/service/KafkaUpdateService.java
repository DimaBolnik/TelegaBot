package ru.bolnik.dispatcher.service;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bolnik.dispatcher.model.Bolt;

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
    public void sendUpdateToKafka(Bolt bolt, Long chatId) {
        String json = String.format(
                "{\"type\": \"Bolt\", \"chatId\": %d, \"gost\": \"%s\", \"size\": \"%s\", \"length\": %d, \"weight\": %.2f}",
                chatId,
                bolt.getGost(),
                bolt.getSize(),
                bolt.getLength(),
                bolt.getWeight()
        );

        kafkaTemplate.send(telegramUpdatesTopic, json);

            logger.info("Отправлено в Kafka topic '{}': {}", telegramUpdatesTopic, json);

    }
}
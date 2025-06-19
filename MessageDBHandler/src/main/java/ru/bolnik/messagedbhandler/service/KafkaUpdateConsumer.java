package ru.bolnik.messagedbhandler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.repository.BoltRepository;
import ru.bolnik.messagedbhandler.repository.NutRepository;

@Service
@RequiredArgsConstructor
public class KafkaUpdateConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUpdateConsumer.class);

    private final ObjectMapper objectMapper;
    private final BoltRepository boltRepository;
    private final NutRepository nutRepository;

    @Value("${kafka.topic.telegram-updates}")
    private String telegramUpdatesTopic;

    // Подписываемся на топик из application.properties
    @KafkaListener(topics = "${kafka.topic.telegram-updates}", groupId = "console-group")
    public void consume(String message) {
        try {
            ProductDto dto = objectMapper.readValue(message, ProductDto.class);
            logger.info("Получено сообщение из Kafka: {}", message);

            System.out.println(dto);

        } catch (JsonProcessingException e) {
            logger.error("Ошибка десериализации JSON", e);
        }
    }
}

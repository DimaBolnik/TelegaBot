package ru.bolnik.messagedbhandler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;

@Service
public class KafkaUpdateConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUpdateConsumer.class);

    private final ObjectMapper objectMapper;
    private final BoltService boltService;
    private final NutService nutService;

    public KafkaUpdateConsumer(ObjectMapper objectMapper, BoltService boltService, NutService nutService) {
        this.objectMapper = objectMapper;
        this.boltService = boltService;
        this.nutService = nutService;
    }


    // Подписываемся на топик из application.properties
    @KafkaListener(topics = "${kafka.topic.telegram-updates}", groupId = "console-group")
    public void consume(String message) {
        try {
            ProductDto dto = objectMapper.readValue(message, ProductDto.class);
            logger.info("Получено сообщение из Kafka: {}", message);

            System.out.println(dto.toString());

        } catch (JsonProcessingException e) {
            logger.error("Ошибка десериализации JSON", e);
        }
    }
}

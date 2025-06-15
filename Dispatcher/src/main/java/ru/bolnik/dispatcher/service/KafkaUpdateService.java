package ru.bolnik.dispatcher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.bolnik.dispatcher.dto.ProductDto;
import ru.bolnik.dispatcher.model.Product;

@Service
public class KafkaUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUpdateService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.telegram-updates}")
    private String telegramUpdatesTopic;

    public KafkaUpdateService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // отправка в топик kafka
    public void sendProductToKafka(Product product, Long chatId) {
        ProductDto dto = new ProductDto(
                chatId,
                product.getClass().getSimpleName(),
                product.getGost(),
                product.getSize(),
                product.getWeight()
        );

        String json = null;
        try {
            json = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(telegramUpdatesTopic, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось сериализовать DTO в JSON", e);
        }

        logger.info("Отправлено в Kafka topic '{}': {}", telegramUpdatesTopic, json);

    }
}
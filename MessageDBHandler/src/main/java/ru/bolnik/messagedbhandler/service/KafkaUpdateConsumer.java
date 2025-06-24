package ru.bolnik.messagedbhandler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaUpdateConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaUpdateConsumer.class);

    private final ObjectMapper objectMapper;
    private final ProductCalculationService calculationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.telegram-responses}")
    private String responseTopic;

    @KafkaListener(topics = "${kafka.topic.telegram-updates}", groupId = "console-group")
    public void consume(String message) {
        try {
            ProductDto dto = objectMapper.readValue(message, ProductDto.class);
            logger.info("Получено из Kafka: {}", dto);

            // Вызываем общий метод
            Optional<ProductResponseDto> result = calculationService.calculateQuantity(dto);

            if (result.isPresent()) {
                ProductResponseDto responseDto = result.get();
                String jsonResponse = objectMapper.writeValueAsString(responseDto);
                kafkaTemplate.send(responseTopic, jsonResponse);
                logger.info("Отправлено в Kafka: {}", jsonResponse);
            } else {
                logger.warn("Не удалось рассчитать количество для {}", dto);
            }

        } catch (JsonProcessingException e) {
            logger.error("Ошибка десериализации/сериализации JSON", e);
        }
    }
}
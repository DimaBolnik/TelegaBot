package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.entity.Bolt;
import ru.bolnik.messagedbhandler.entity.Nut;
import ru.bolnik.messagedbhandler.repository.BoltRepository;
import ru.bolnik.messagedbhandler.repository.NutRepository;

import java.util.Optional;

@Service
public class ProductCalculationService {

    private final BoltRepository boltRepository;
    private final NutRepository nutRepository;

    public ProductCalculationService(BoltRepository boltRepository, NutRepository nutRepository) {
        this.boltRepository = boltRepository;
        this.nutRepository = nutRepository;
    }

    /**
     * Универсальный метод для расчёта количества изделий
     */
    public Optional<ProductResponseDto> calculateQuantity(String productType, String gost, String size, Integer length, Double totalWeightGrams, Long chatId) {
        if (totalWeightGrams == null || totalWeightGrams <= 0) {
            return Optional.empty();
        }

        Double weightOne = null;

        switch (productType) {
            case "Bolt":
                if (length == null) {
                    return Optional.empty(); // Для болта нужна длина
                }
                Optional<Bolt> boltOpt = boltRepository.findFirstByGostAndSizeAndLength(gost, size, length);
                if (boltOpt.isPresent()) {
                    weightOne = boltOpt.get().getWeight();
                }
                break;

            case "Nut":
                Optional<Nut> nutOpt = nutRepository.findFirstByGostAndSize(gost, size);
                if (nutOpt.isPresent()) {
                    weightOne = nutOpt.get().getWeight();
                }
                break;

            default:
                return Optional.empty(); // Неизвестный тип
        }

        if (weightOne == null || weightOne <= 0) {
            return Optional.empty(); // Вес не найден
        }

        int quantity = (int) (totalWeightGrams / weightOne);

        return Optional.of(new ProductResponseDto(chatId, productType, quantity));
    }
}
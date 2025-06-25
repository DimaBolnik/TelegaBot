package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.entity.Bolt;
import ru.bolnik.messagedbhandler.entity.Nut;

import java.util.Optional;

@Service
public class ProductCalculationService {

    private final BoltService boltService;
    private final NutService nutService;

    public ProductCalculationService(BoltService boltService, NutService nutService) {
        this.boltService = boltService;
        this.nutService = nutService;
    }

    /**
     * Универсальный метод для расчёта количества изделий
     */
    public Optional<ProductResponseDto> calculateQuantity(ProductDto dto) {

        String productType = dto.getType();
        String gost = dto.getGost();
        String size = dto.getSize();
        Integer length = dto.getLength();
        Double totalWeight = dto.getWeight();
        Long chatId = dto.getChatId();

        if (totalWeight == null || totalWeight <= 0) {
            return Optional.empty();
        }

        Double weightOne = null;

        switch (productType) {
            case "Bolt":
                if (length == null) {
                    return Optional.empty(); // Для болта нужна длина
                }
                Optional<Bolt> boltOpt = boltService.findFirstByGostAndSizeAndLength(gost, size, length);
                if (boltOpt.isPresent()) {
                    weightOne = boltOpt.get().getWeight();
                }
                break;

            case "Nut":
                Optional<Nut> nutOpt = nutService.findFirstByGostAndSize(gost, size);
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

        int quantity = (int) (totalWeight / weightOne);

        return Optional.of(new ProductResponseDto(chatId, productType, quantity));
    }
}
package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.entity.Bolt;
import ru.bolnik.messagedbhandler.entity.Nut;
import ru.bolnik.messagedbhandler.repository.WasherRepository;
import ru.bolnik.messagedbhandler.service.data.ProductTypeEnum;

import java.util.Optional;

@Service
public class ProductCalculationService {

    private final BoltService boltService;
    private final NutService nutService;
    private final WasherRepository washerRepository;

    public ProductCalculationService(BoltService boltService, NutService nutService, WasherRepository washerRepository) {
        this.boltService = boltService;
        this.nutService = nutService;
        this.washerRepository = washerRepository;
    }

    /**
     * Универсальный метод для расчёта количества изделий
     */
    public Optional<ProductResponseDto> calculateQuantity(ProductDto dto) {


        ProductTypeEnum productType = dto.getType();
        String gost = dto.getGost();
        String size = dto.getSize();
        Integer length = dto.getLength(); // только для болта
        Double totalWeight = dto.getWeight();
        Long chatId = dto.getChatId();

        if (totalWeight == null || totalWeight <= 0) {
            return Optional.empty();
        }

        Double weightOne = null;

        switch (productType) {
            case BOLT:
                if (length == null) {
                    return Optional.empty(); // Для болта нужна длина
                }
                Optional<Bolt> boltOpt = boltService.findFirstByGostAndSizeAndLength(gost, size, length);
                if (boltOpt.isPresent()) {
                    weightOne = boltOpt.get().getWeight();
                }
                break;

            case NUT:
                Optional<Nut> nutOpt = nutService.findFirstByGostAndSize(gost, size);
                if (nutOpt.isPresent()) {
                    weightOne = nutOpt.get().getWeight();
                }
                break;

            case WASHER:
                var washerOpt = washerRepository.findFirstByGostAndSize(gost, size);
                if (washerOpt.isPresent()) {
                    weightOne = washerOpt.get().getWeight();
                }
                break;

            default:
                return Optional.empty(); // Неизвестный тип
        }

        if (weightOne == null || weightOne <= 0) {
            return Optional.empty(); // Вес не найден
        }

        int quantity = (int) (totalWeight / weightOne);

        return Optional.of(new ProductResponseDto(chatId, productType.toString(), quantity));
    }
}
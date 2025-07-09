package ru.bolnik.messagedbhandler.service.calculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.service.NutService;
import ru.bolnik.messagedbhandler.service.data.ProductTypeEnum;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NutCalculator implements ProductCalculator {

    private final NutService nutService;

    @Override
    public boolean supports(ProductDto dto) {
        return dto.getType() == ProductTypeEnum.NUT;
    }

    @Override
    public Optional<ProductResponseDto> calculate(ProductDto dto) {
        return nutService.findFirstByGostAndSize(dto.getGost(), dto.getSize())
                .map(n -> new ProductResponseDto(
                        dto.getChatId(),
                        dto.getType().toString(),
                        calculateQuantity(dto.getWeight(), n.getWeight())));
    }

    private int calculateQuantity(Double totalWeight, Double weightOne) {
        return weightOne > 0 ? (int) (totalWeight / weightOne) : 0;
    }
}
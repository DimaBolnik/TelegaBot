package ru.bolnik.dispatcher.service.data;

// enum для выбора изделия: Болт или Гайка, Шайба(возможно добавление других наименований)
public enum ProductTypeEnum {
    BOLT("Болт"),
    NUT("Гайка"),
    WASHER("Шайба");

    private final String label;

    ProductTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ProductTypeEnum fromLabel(String label) {
        for (ProductTypeEnum type : values()) {
            if (type.ordinal() + 1 == Integer.parseInt(label)) {
                return type;
            }
        }
        return null;
    }
}

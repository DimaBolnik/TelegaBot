package ru.bolnik.messagedbhandler.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bolts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bolt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gost", nullable = false, length = 50)
    private String gost;

    @Column(name = "size", nullable = false, length = 20)
    private String size;

    @Column(name = "length_mm", nullable = false)
    private Integer length; // длина в миллиметрах

    @Column(name = "weight_gram", nullable = false)
    private Integer weight; // вес одного болта в граммах
}
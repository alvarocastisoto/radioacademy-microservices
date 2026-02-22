package com.alvaro.radiocademy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(name = "course_id", nullable = false)
    private UUID courseId;
    // Datos extra de la compra
    private BigDecimal amountPaid; // Cuánto pagó exactamente
    private String paymentId; // ID de transacción de Stripe

    @CreationTimestamp
    private LocalDateTime enrolledAt; // Fecha y hora de compra
}
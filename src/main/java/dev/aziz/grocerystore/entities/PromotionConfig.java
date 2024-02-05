package dev.aziz.grocerystore.entities;

import dev.aziz.grocerystore.enums.PromotionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Entity
@Table(name = "promotion_config")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PromotionConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Promotion type should not be empty")
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    @NotNull(message = "Minimum amount should not be empty")
    private Integer minimumAmount;

    @NotNull(message = "Free amount should not be empty")
    private Integer freeAmount;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @NotNull(message = "End date should not be empty")
    @Column(name = "end_date")
    private Instant endDate;

}

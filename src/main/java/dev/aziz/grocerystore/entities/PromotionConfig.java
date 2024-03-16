package dev.aziz.grocerystore.entities;

import dev.aziz.grocerystore.enums.PromotionType;
import jakarta.persistence.*;
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

    @OneToOne
    private Item item;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @NotNull(message = "End date should not be empty")
    @Column(name = "end_date")
    private Instant endDate;

}

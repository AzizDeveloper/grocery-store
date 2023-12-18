package dev.aziz.grocerystore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    private String description;

    @NotEmpty(message = "Price should not be empty")
    @Column(precision = 8, scale = 3)
    private BigDecimal price;
//    precision of 8 and a scale of 3
//    private String price;

    @NotEmpty(message = "Category should not be empty")
    private String category;

    @NotEmpty(message = "Picture should not be empty")
    @Column(name = "picture_url")
    private String pictureUrl;

    @NotEmpty(message = "Weight should not be empty")
    private Integer weight;

    @NotEmpty(message = "Stock amount should not be empty")
    @Column(name = "stock_amount")
    private Integer stockAmount;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

}

package com.shopme.common.entity.product;

import com.shopme.common.entity.IdBasedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends IdBasedEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(Integer id, String name, Product product) {
        super(id);
        this.name = name;
        this.product = product;
    }

    @Transient
    public String getImagePath() {
        return name == null ? "/images/image-thumbnail.png" : "/product-images/" + product.getId() + "/extras/" + name;
    }
}

package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 64)
    private String photos;

    private boolean enabled;

    @Column(name = "all_parent_ids", length = 256)
    public String allParentIds;

    @Transient
    private boolean hasChildren;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
        this.alias = generateAlias(name);
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public Category(Integer id) {
        this.id = id;
    }

    @Transient
    public String getImagePath() {
        return photos == null ? "/images/image-thumbnail.png" : "/category-images/" + id + "/" + photos;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public static Category copyFull(Category category) {
        Category categoryCopy = new Category(category.getId());

        categoryCopy.setName(category.getName());
        categoryCopy.setAlias(category.getAlias());
        categoryCopy.setPhotos(category.getPhotos());
        categoryCopy.setEnabled(category.isEnabled());
        categoryCopy.setParent(category.getParent());
        categoryCopy.setChildren(new HashSet<>(category.getChildren()));
        categoryCopy.setHasChildren(!category.getChildren().isEmpty());

        return categoryCopy;
    }

    public static Category copyFull(Category category, String name){
        Category categoryCopy = new Category(category.getId());

        categoryCopy.setName(name);
        categoryCopy.setAlias(category.getAlias());
        categoryCopy.setPhotos(category.getPhotos());
        categoryCopy.setEnabled(category.isEnabled());
        categoryCopy.setParent(category.getParent());
        categoryCopy.setChildren(new HashSet<>(category.getChildren()));
        category.setHasChildren(!category.getChildren().isEmpty());

        return categoryCopy;
    }

    private String generateAlias(String name) {
        // Chuyển sang chữ thường
        String alias = name.toLowerCase();

        // Thay thế dấu tiếng Việt bằng ký tự không dấu
        alias = alias.replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("[đ]", "d");

        // Thay thế khoảng trắng bằng dấu gạch ngang
        alias = alias.replaceAll("\\s+", "-");

        return alias;
    }
}

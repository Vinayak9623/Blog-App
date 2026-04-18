package com.vsd.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "my_basket_article_images")
public class ArticleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Article article;

    private String objectKey;
    private String url;
    private boolean isThumbnail=false;
    private Integer displayOrder;
    private String contentType;
}

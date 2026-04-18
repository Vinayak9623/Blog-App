package com.vsd.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "articles")
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String shortDesc;
    @Lob
    private String content;
    private boolean paid;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private Double rating;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ArticleImage> articleImage;
}

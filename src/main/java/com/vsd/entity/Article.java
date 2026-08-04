package com.vsd.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return paid == article.paid && Objects.equals(id, article.id) && Objects.equals(title, article.title) && Objects.equals(shortDesc, article.shortDesc) && Objects.equals(content, article.content) && status == article.status && Objects.equals(publishedAt, article.publishedAt) && Objects.equals(createdAt, article.createdAt) && Objects.equals(rating, article.rating) && Objects.equals(price, article.price) && Objects.equals(category, article.category) && Objects.equals(user, article.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortDesc, content, paid, status, publishedAt, createdAt, rating, price, category, user);
    }
}

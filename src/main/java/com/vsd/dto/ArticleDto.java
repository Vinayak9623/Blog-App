package com.vsd.dto;

import com.vsd.entity.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    private Long id;
    private String title;
    private String shortDesc;
    private String content;
    private boolean paid;
    private Status status;
    private Double rating;
    private Double price;
    private Long userId;
    private List<ArticleImageDto> articleImage;
}

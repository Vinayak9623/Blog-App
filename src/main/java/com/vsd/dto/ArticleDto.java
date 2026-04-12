package com.vsd.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    private long id;
    private String name;
    private String content;
    private int timeInMinutes;
}

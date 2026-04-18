package com.vsd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleImageDto {
    private Long id;
    private String objectKey;
    private String url;
    private boolean isThumbnail;
    private Integer displayOrder;
    private String contentType;
}

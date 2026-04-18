package com.vsd.service;

import com.vsd.dto.ArticleDto;
import com.vsd.dto.ArticleImageDto;
import com.vsd.entity.ArticleImage;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ArticleService {
    public ArticleDto create(ArticleDto articleDto);
    public ArticleDto update(Long articalId,ArticleDto articleDto);
    public List<ArticleDto> getArticles();
    public ArticleDto getSingleArticle(long articleId);
    public String deleteArticle(long articleId);
    public List<ArticleDto> getArticleOfUser(Long userId);
    public List<ArticleDto> getArticleOfCategory(Long category);

    List<ArticleImageDto> uploadImage(List<MultipartFile> files, Long articleId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}

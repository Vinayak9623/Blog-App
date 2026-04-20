package com.vsd.service;

import com.vsd.dto.ArticleDto;
import com.vsd.dto.ArticleImageDto;
import com.vsd.entity.ArticleImage;
import io.minio.errors.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ArticleService {
    ArticleDto create(ArticleDto articleDto);
   ArticleDto update(Long articalId,ArticleDto articleDto);
   List<ArticleDto> getArticles();
   Page<ArticleDto> getPeginatedArticle(int page, int size);
   ArticleDto getSingleArticle(long articleId);
   String deleteArticle(long articleId);
   List<ArticleDto> getArticleOfUser(Long userId);
   List<ArticleDto> getArticleOfCategory(Long category);
    List<ArticleImageDto> uploadImage(List<MultipartFile> files, Long articleId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}

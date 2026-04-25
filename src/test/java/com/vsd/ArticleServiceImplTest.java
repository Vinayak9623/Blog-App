package com.vsd;

import com.vsd.dto.ArticleDto;
import com.vsd.entity.Article;
import com.vsd.repository.ArticleRepository;
import com.vsd.service.ArticleService;
import com.vsd.service.impl.ArticleServiceImplDb;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImplDb articleServiceImplDb;

    @Test
    void testCreateArticle(){
        ArticleDto inputDto=new ArticleDto();
        inputDto.setId(1l);
        inputDto.setTitle("Book");
        inputDto.setShortDesc("xyz");
        inputDto.setContent("java Object");
        inputDto.setPaid(true);
        inputDto.setPrice(123.89);
        inputDto.setRating(2.5);
        Article article =new Article();
        Article savedArticle=new Article();
        ArticleDto outputDto=new ArticleDto();

        Mockito.when(modelMapper.map(inputDto,Article.class)).thenReturn(article);
        Mockito.when(articleRepository.save(Mockito.any(Article.class))).thenReturn(savedArticle);
        Mockito.when(modelMapper.map(savedArticle,ArticleDto.class)).thenReturn(outputDto);

        ArticleDto result=articleServiceImplDb.create(inputDto);
        Assertions.assertNotNull(result);

        Mockito.verify(modelMapper).map(inputDto,Article.class);
        Mockito.verify(articleRepository).save(article);
        Mockito.verify(modelMapper).map(savedArticle,ArticleDto.class);


    }


}

package com.xiaomou;

import com.xiaomou.dto.ArticleHomeDTO;
import com.xiaomou.mapper.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MouHongDa
 * @date 2022/5/22 11:50
 */
@SpringBootTest
class MyblogMainTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void main() {
        List<ArticleHomeDTO> articleHomeDTOS = articleMapper.listArticles(0L);
        articleHomeDTOS.forEach(System.out::println);
    }
}
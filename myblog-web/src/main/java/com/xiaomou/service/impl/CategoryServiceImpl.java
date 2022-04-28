package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.ArticlePreviewDTO;
import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.CategoryDTO;
import com.xiaomou.entity.Category;
import com.xiaomou.mapper.ArticleMapper;
import com.xiaomou.mapper.CategoryMapper;
import com.xiaomou.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ArticlePreviewListDTO listArticlesByCategoryId(Integer categoryId, Integer current) {
        //转换页码
        Page<ArticlePreviewDTO> page = new Page<>(current, 9);
        IPage<ArticlePreviewDTO> articles = articleMapper.listArticlesByCondition(page, categoryId);
        List<ArticlePreviewDTO> records = articles.getRecords();
        String name = null;
        return new ArticlePreviewListDTO(records, name);
    }

    @Override
    public List<CategoryDTO> listCategories() {
        List<CategoryDTO> categories = this.baseMapper.listCategories();
        return categories;
    }
}

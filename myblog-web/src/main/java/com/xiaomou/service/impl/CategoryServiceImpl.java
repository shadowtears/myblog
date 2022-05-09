package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.ArticlePreviewDTO;
import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.CategoryDTO;
import com.xiaomou.entity.Article;
import com.xiaomou.entity.Category;
import com.xiaomou.handler.exception.MyRuntimeException;
import com.xiaomou.mapper.ArticleMapper;
import com.xiaomou.mapper.CategoryMapper;
import com.xiaomou.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.vo.AddOrEditCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    private CategoryMapper categoryMapper;

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

    @Override
    public boolean addOrEditCategory(AddOrEditCategoryVO addOrEditCategoryVO) {
        if (addOrEditCategoryVO == null) {
            throw new RuntimeException();
        }
        // 判断分类名重复
        Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, addOrEditCategoryVO.getCategoryName()));
        if (count > 0) {
            throw new MyRuntimeException("分类名已存在");
        }
        Integer tagId = addOrEditCategoryVO.getCategoryId();
        Category category = new Category();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.format(date);
        category.setCategoryName(addOrEditCategoryVO.getCategoryName());
        if (tagId != null) {
            //说明这是编辑博客 那么就需要给一个更新时间
            category.setCategoryId(tagId);
            category.setUpdateTime(date);
        } else {
            //说明是新增标签
            category.setCreateTime(date);
        }
        boolean b = this.saveOrUpdate(category);
        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        // 查询分类id下是否有文章
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        if (count > 0) {
            throw new MyRuntimeException("删除失败，该分类下存在文章");
        }
        categoryMapper.deleteBatchIds(categoryIdList);
    }
}

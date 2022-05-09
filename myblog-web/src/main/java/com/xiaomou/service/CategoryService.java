package com.xiaomou.service;

import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.CategoryDTO;
import com.xiaomou.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomou.vo.AddOrEditCategoryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface CategoryService extends IService<Category> {

    ArticlePreviewListDTO listArticlesByCategoryId(Integer categoryId,Integer current);

    List<CategoryDTO> listCategories();

    boolean addOrEditCategory(AddOrEditCategoryVO addOrEditCategoryVO);

    void deleteCategory(List<Integer> categoryIdList);
}

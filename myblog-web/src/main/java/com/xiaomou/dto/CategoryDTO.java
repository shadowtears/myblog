package com.xiaomou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author MouHongDa
 * @date 2022/4/28 15:47
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class CategoryDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 分类下的文章数量
     */
    private Long articleCount;


}


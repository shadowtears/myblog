package com.xiaomou.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author MouHongDa
 * @date 2022/4/28 15:38
 */
@Getter
@Setter
@ToString
public class ArticlePreviewListDTO {
    /**
     * 条件对应的文章列表
     */
    private List<ArticlePreviewDTO> articlePreviewDTOList;

    /**
     * 条件名
     */
    private String name;

    public ArticlePreviewListDTO(List<ArticlePreviewDTO> articlePreviewDTOList, String name) {
        this.articlePreviewDTOList = articlePreviewDTOList;
        this.name = name;
    }

    public ArticlePreviewListDTO() {
    }

}

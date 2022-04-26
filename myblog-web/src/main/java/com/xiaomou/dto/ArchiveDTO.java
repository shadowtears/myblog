package com.xiaomou.dto;

import lombok.Data;

import java.util.Date;

/**
 * 文章文章归档的dto
 * @author MouHongDa
 * @date 2022/4/24 14:41
 */
@Data
public class ArchiveDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 发表时间
     */
    private Date createTime;
}
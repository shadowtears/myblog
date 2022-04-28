package com.xiaomou.dto;

import com.mysql.cj.x.protobuf.MysqlxResultset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author MouHongDa
 * @date 2022/4/28 16:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchDTO {

    /**
     * 文章id
     */
    @Id
    private Integer id;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章状态
     */
    private Integer isDelete;
}

package com.xiaomou.dto;

import lombok.Data;

import java.util.List;

/**
 * @author MouHongDa
 * @date 2022/4/24 15:59
 */
@Data
public class PageDTO<T> {
    /**
     * 分页列表
     */
    private List<T> recordList;

    /**
     * 总数
     */
    private Integer count;

    public PageDTO(List<T> recordList, Integer count) {
        this.recordList = recordList;
        this.count = count;
    }

    public PageDTO() {
    }
}

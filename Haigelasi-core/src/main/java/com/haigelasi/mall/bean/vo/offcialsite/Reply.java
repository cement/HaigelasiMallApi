package com.haigelasi.mall.bean.vo.offcialsite;

import lombok.Data;

import java.util.Date;

@Data
public class Reply {
    private String content;
    private Date createAt;
    private Author author;
}

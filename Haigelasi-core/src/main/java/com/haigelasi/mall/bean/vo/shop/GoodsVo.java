package com.haigelasi.mall.bean.vo.shop;

import com.haigelasi.mall.bean.entity.shop.Goods;
import com.haigelasi.mall.bean.entity.shop.GoodsSku;
import lombok.Data;

import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 12/10/2019 7:57 PM
 */
@Data
public class GoodsVo {
    private Goods goods;
    private List<GoodsSku> skuList;

}

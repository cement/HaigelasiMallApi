package com.haigelasi.mall.service.shop;


import com.haigelasi.mall.bean.entity.shop.Address;
import com.haigelasi.mall.bean.vo.query.SearchFilter;
import com.haigelasi.mall.dao.shop.AddressRepository;
import com.haigelasi.mall.service.BaseService;
import com.haigelasi.mall.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService extends BaseService<Address,Long,AddressRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AddressRepository addressRepository;

    /**
     * 获取用户默认收货地址
     * @param idUser
     * @return
     */
    public Address getDefaultAddr(Long idUser){
        List<SearchFilter> filterList = Lists.newArrayList(
                SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser),
                SearchFilter.build("isDefault", SearchFilter.Operator.EQ,true)
        );
        return get(filterList);
    }

    public Address get(Long idUser,Long id){
        return get(Lists.newArrayList(
                SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser),
                SearchFilter.build("id", SearchFilter.Operator.EQ,id)
        ));
    }
    public void delete(Long idUser, Long id) {
        Address address = get(Lists.newArrayList(
                SearchFilter.build("idUser", SearchFilter.Operator.EQ,idUser),
                SearchFilter.build("id", SearchFilter.Operator.EQ,id)
        ));
        address.setIsDelete(true);
        update(address);
    }
}


package com.haigelasi.mall.bean.entity.system;

import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity(name = "t_wx_user")
@Table(appliesTo = "t_wx_user",comment = "微信用户")
@Data
@EntityListeners(AuditingEntityListener.class)
public class WxUser {

   @Id
   @GeneratedValue
   private  Long id;
  @Column(unique = true)
  private String unionid;
  @Column
  private String openid;
  @Column
  private String nickname;
  @Column
  private String sex;
  @Column
  private String province;
  @Column
  private String city;
  @Column
  private String country;
  @Column
  private String headimgurl;
  @Column
  private String privilege;


}

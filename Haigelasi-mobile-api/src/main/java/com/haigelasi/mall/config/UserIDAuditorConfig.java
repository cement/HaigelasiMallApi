package com.haigelasi.mall.config;

import com.haigelasi.mall.security.JwtUtil;
import com.haigelasi.mall.utils.Constants;
import com.haigelasi.mall.utils.HttpUtil;
import com.haigelasi.mall.utils.StringUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 审计功能配置
 *
 * @author enilu
 * @version 2019/1/8 0008
 */

public class UserIDAuditorConfig implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            String token = HttpUtil.getRequest().getHeader("Authorization");
            if (StringUtil.isNotEmpty(token)) {
                return Optional.of(JwtUtil.getUserId(token));
            }
        }catch (Exception e){
            //返回系统用户id
            return Optional.of(Constants.SYSTEM_USER_ID);
        }
        //返回系统用户id
        return Optional.of(Constants.SYSTEM_USER_ID);
    }
}

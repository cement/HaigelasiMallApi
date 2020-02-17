package com.haigelasi.mall.service.api;

import com.haigelasi.mall.bean.constant.CfgKey;
import com.haigelasi.mall.bean.entity.system.Cfg;
import com.haigelasi.mall.cache.ConfigCache;
import com.haigelasi.mall.service.system.CfgService;
import com.haigelasi.mall.utils.HttpUtil;
import com.haigelasi.mall.utils.StringUtil;
import org.nutz.json.Json;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：enilu
 * @date ：Created in 1/19/2020 4:31 PM
 */
@Service
public class WeixinService {
    @Autowired
    private CfgService cfgService;
    @Autowired
    private ConfigCache configCache;
    private Logger logger = LoggerFactory.getLogger(WeixinService.class);
    public void updateWeixinToken() {
        HttpUtil.trustEveryone();
        String accessToken = getAccessToken();
        String jsapiTicket = getJsapiTicket(accessToken);

        Cfg tokenCfg = cfgService.getByCfgName(CfgKey.WX_ACCESS_TOKEN);
        tokenCfg.setCfgValue(accessToken);
        cfgService.saveOrUpdate(tokenCfg);

        Cfg tidCfg = cfgService.getByCfgName(CfgKey.WX_JS_API_TICKET);
        tidCfg.setCfgValue(jsapiTicket);
        cfgService.saveOrUpdate(tidCfg);
    }

    public String getAccessToken() {
        Cfg appIdCfg = cfgService.getByCfgName(CfgKey.WX_APP_ID);
        Cfg secretCfg = cfgService.getByCfgName(CfgKey.WX_APP_SECRET);
        String accessTokenUrl = configCache.get(CfgKey.WX_ACCESS_TOKEN_URL).toString();
        String url = String.format(accessTokenUrl, appIdCfg.getCfgValue(), secretCfg.getCfgValue());
        String result = HttpUtil.sendGet(url);
        logger.info("获取微信token，url : {} ,\r\n result : {}",url,result);
        Object object = Json.fromJson(StringUtil.sNull(result));
        String access_token = (String) Mapl.cell(object, "access_token");
        return access_token;
    }

    public String getJsapiTicket(String accessToken) {
        String jsapiTicketUrl = configCache.get(CfgKey.WX_JS_API_TICKET_URL).toString();
        String url = String.format(jsapiTicketUrl, accessToken);
        String result = HttpUtil.sendGet(url);
        Object object = Json.fromJson(StringUtil.sNull(result));
        String ticket = (String) Mapl.cell(object, "ticket");
        return ticket;
    }
}

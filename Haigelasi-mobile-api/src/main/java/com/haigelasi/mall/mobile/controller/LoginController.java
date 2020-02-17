package com.haigelasi.mall.mobile.controller;

import com.haigelasi.mall.bean.entity.shop.ShopUser;
import com.haigelasi.mall.bean.vo.JwtUser;
import com.haigelasi.mall.bean.vo.UserInfo;
import com.haigelasi.mall.bean.vo.front.Rets;
import com.haigelasi.mall.security.UserService;
import com.haigelasi.mall.service.shop.ShopUserService;
import com.haigelasi.mall.utils.MD5;
import com.haigelasi.mall.utils.RandomUtil;
import com.haigelasi.mall.web.controller.BaseController;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ：enilu
 * @date ：Created in 11/5/2019 9:01 PM
 */
@RestController
@RequestMapping("/")
public class LoginController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(value = "sendSmsCode",method = RequestMethod.POST)
    public Object sendSmsCode(@RequestParam String mobile){
        String smsCode = shopUserService.sendSmsCode(mobile);
        //todo 测试环境直接返回验证码，生成环境切忌返回该验证码
        return Rets.success(smsCode);
    }
    /**
     * 使用手机号和短信验证码登录或者注册
     * @param mobile
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "loginOrReg",method = RequestMethod.POST)
    public Object loginOrReg(@RequestParam String mobile,@RequestParam String smsCode){
        try {
            logger.info("用户登录:" + mobile + ",短信验证码:" + smsCode);
            //1,
            ShopUser user = shopUserService.findByMobile(mobile);
            Boolean validateRet = shopUserService.validateSmsCode(mobile,smsCode);

            Map<String, String> result = new HashMap<>(6);
            if(validateRet) {
                if(user==null){
                    //初始化6位密码
                    String initPassword = RandomUtil.getRandomString(6);
                    user = shopUserService.register(mobile,initPassword);
                    result.put("initPassword",initPassword);
                }

                String token = userService.loginForToken(new JwtUser(user));
                logger.info("token:{}",token);
                result.put("token", token);
                return Rets.success(result);
            }

            return Rets.failure("短信验证码错误");


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }

    /**
     * 使用手机号和密码登录
     * @param mobile
     * @param password
     * @return
     */
    @RequestMapping(value = "loginByPass",method = RequestMethod.POST)
    public Object loginByPass(@RequestParam String mobile,@RequestParam String password){
        try {
            logger.info("用户登录:" + mobile + ",密码:" + password);
            //1,
            ShopUser user = shopUserService.findByMobile(mobile);
            if (user == null) {
                return Rets.failure("该用户不存在");
            }

            String passwdMd5 = MD5.md5(password, user.getSalt());
            //2,
            if (!user.getPassword().equals(passwdMd5)) {
                return Rets.failure("输入的密码错误");
            }
            String token = userService.loginForToken(new JwtUser(user));
            Map<String, Object> result = new HashMap<>(1);
            user.setLastLoginTime(new Date());
            shopUserService.update(user);
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user,userInfo);
            logger.info("token:{}",token);
            result.put("token", token);
            result.put("user",userInfo);
            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }


    @RequestMapping(value = "wxLogin",method = RequestMethod.POST)
    public void wxLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String appid = "wxc3925a4cc18eb386";
        String redirectUri ="www.baidu.com";
        String scope ="snsapi_userinfo";
        String wxAuthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" +appid+
                "&redirect_uri=" +redirectUri+
                "&response_type="+"code" +
                "&scope=" +scope+
                "&state=STATE" +
                "#wechat_redirect";
        response.sendRedirect(wxAuthUrl);
    }

    @RequestMapping(value = "wxLoginCallback",method = {RequestMethod.GET,RequestMethod.POST})
    public void wxLoginCallback(@RequestParam("code") String code) throws IOException {
        String appid = "wxc3925a4cc18eb386";
        String secret ="71cbd9c1311a8e83f8af54c7f1a39712";
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        LinkedHashMap<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("appid",appid);
        paramsMap.put("secret",secret);
        paramsMap.put("code",code);
        paramsMap.put("grant_type","authorization_code");
        JSONObject resultJson = restTemplate.getForObject(tokenUrl, JSONObject.class, paramsMap);
        String openId = resultJson.getString("openId");
        String unionId = resultJson.getString("unionId");



    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public Object logout(){
        //todo 处理额外的退出登录逻辑
        logger.info("处理额外的退出登录逻辑");
        return Rets.success();
    }
}

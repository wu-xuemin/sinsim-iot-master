package com.eservice.sinsimiot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.sinsimiot.core.ResponseCode;
import com.eservice.sinsimiot.common.ResponseModel;
import com.eservice.sinsimiot.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author HT
 */

@Component
public class TokenService {


    @Value("${park.ip}")
    private String PARK_BASE_URL;
    @Value("${park.username}")
    private String username;
    @Value("${park.password}")
    private String password;
    @Autowired
    private RestTemplate restTemplate;

    private final static Logger logger = LoggerFactory.getLogger(TokenService.class);

    /**
     * 园区登录，成功则返回token，失败返回null
     */
    public String getToken() {
        String token = null;
        HashMap<String, String> postParameters = new HashMap<>();
        postParameters.put("username", username);
        postParameters.put("password", Util.md5(password));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity r = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/user/login", r, String.class);
            if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                String body = responseEntity.getBody();
                if (body != null) {
                    ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                    if (responseModel != null && responseModel.getResult() != null) {
                        token = responseModel.getResult();
                    }
                }
            }
        } catch (Exception exception) {
            return getTokenSKL();
        }
        return token;
    }

    public String getTokenSKL() {
        String token = null;
//        HashMap<String, String> postParameters = new HashMap<>();
//        postParameters.put("username", username);
//        postParameters.put("password", Util.getStringSHA256Value(password));
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//        HttpEntity r = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
//        try {
//            ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/login", r, String.class);
//            if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
//                String body = responseEntity.getBody();
//                if (body != null) {
//                    ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
//                    if (responseModel != null && responseModel.getResult() != null) {
//                        token = responseModel.getResult();
//                    }
//                }
//            }
//        } catch (Exception exception) {
//            logger.error("Token update error ==> " + exception.getMessage());
//        }
        return token;
    }

}

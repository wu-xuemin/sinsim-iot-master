//package com.eservice.sinsimiot.service.park;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.eservice.sinsimiot.common.ResponseModel;
//import com.eservice.sinsimiot.core.Constant;
//import com.eservice.sinsimiot.core.ResponseCode;
//import com.eservice.sinsimiot.model.park.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
/////不需要了
//
///**
// * @author HT
// */
//@Component
//@Slf4j
//public class TagService {
//
//    @Value("${park.ip}")
//    private String PARK_BASE_URL;
//
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private TokenService tokenService;
//
//
//    private ThreadPoolTaskExecutor mExecutor;
//
//    /**
//     * Token
//     */
//    private String token;
//    /**
//     * 全量tag
//     */
//    private List<Tag> allTagList = new ArrayList<>();
//    /**
//     * 访客tag
//     */
//    private List<Tag> visitorTagList = new ArrayList<>();
//
//    /**
//     * 员工tag
//     */
//    private List<Tag> staffTagList = new ArrayList<>();
//
//    /**
//     * 每2分钟更新一次TAG
//     */
//    @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60 * 2)
//    public void fetchTags() {
//        if (token == null) {
//            token = tokenService.getToken();
//        }
//        if (token != null) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.ACCEPT, "application/json");
//            headers.add(HttpHeaders.AUTHORIZATION, token);
//            HttpEntity entity = new HttpEntity(headers);
//            try {
//                ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/tags?size=0", HttpMethod.GET, entity, String.class);
//                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
//                    String body = responseEntity.getBody();
//                    if (body != null) {
//                        processTagResponse(body);
//                    }
//                }
//            } catch (HttpClientErrorException errorException) {
//                if (errorException.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
//                    //token失效,重新获取token后再进行数据请求
//                    token = tokenService.getToken();
//                    if (token != null) {
//                        fetchTags();
//                    }
//                }
//            }
//        }
//    }
//
//    private void processTagResponse(String body) {
//        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
//        if (responseModel != null && responseModel.getResult() != null) {
//            List<Tag> tmpList = JSONArray.parseArray(responseModel.getResult(), Tag.class);
//            if (tmpList != null && tmpList.size() > 0) {
//                ArrayList<Tag> visitorTagList = new ArrayList<>();
//                ArrayList<Tag> staffTagList = new ArrayList<>();
//                for (Tag tag : tmpList) {
//                    for (String str : tag.getVisible_identity()) {
//                        if (Constant.VISITOR.equals(str)) {
//                            visitorTagList.add(tag);
//                        }
//                        if (Constant.STAFF.equals(str)) {
//                            staffTagList.add(tag);
//                        }
//                    }
//                }
//                if (this.allTagList.size() != tmpList.size()) {
//                    log.info("The number of allTagList：    {} ==> {}", this.allTagList.size(), tmpList.size());
//                    log.info("The number of visitorTagList：{} ==> {}", this.visitorTagList.size(), visitorTagList.size());
//                    log.info("The number of staffTagList：  {} ==> {}", this.staffTagList.size(), staffTagList.size());
//                }
//                this.allTagList = tmpList;
//                this.visitorTagList = visitorTagList;
//                this.staffTagList = staffTagList;
//            }
//        }
//    }
//
//    public Tag createTag(Tag tag) {
//        if (token == null) {
//            token = tokenService.getToken();
//        }
//        if (token != null) {
//            HashMap<String, Object> postParameters = new HashMap<>();
//            ArrayList<Tag> tagList = new ArrayList<>();
//            tagList.add(tag);
//            postParameters.put("tag_list", tagList);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//            headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
//            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
//            try {
//                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/tags", httpEntity, String.class);
//                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
//                    ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
//                    if (responseModel != null && responseModel.getRtn() == 0) {
//                        List<ResponseModel> responseModels = JSONObject.parseArray(responseModel.getResult(), ResponseModel.class);
//                        ResponseModel responseModel1 = responseModels.get(0);
//                        if (responseModel1 != null && responseModel1.getRtn() == 0) {
//                            fetchTags();
//                            try {
//                                return JSONObject.parseObject(responseModel1.getResult(), Tag.class);
//                            } catch (Exception e) {
//                                log.warn("JSON to Object failed. ===> {} ", e.getMessage());
//                            }
//                            return new Tag();
//                        } else {
//                            log.warn("createTag failed. ===> {} ", responseModel.getResult());
//                        }
//                    } else {
//                        log.warn("createTag failed. ===> {} ", responseEntity);
//                    }
//                }
//            } catch (HttpClientErrorException exception) {
//                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
//                    token = tokenService.getToken();
//                    if (token != null) {
//                        return createTag(tag);
//                    }
//                }
//            }
//        } else {
//            log.error("Token is null, createTag error!");
//        }
//        return null;
//    }
//
//    public Tag updateTag(Tag tag) {
//        if (token == null) {
//            token = tokenService.getToken();
//        }
//        if (token != null) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//            headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
//            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(tag), headers);
//            try {
//                ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/tags/" + tag.getTag_id(), HttpMethod.PUT, httpEntity, String.class);
//                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
//                    ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
//                    if (responseModel != null && responseModel.getRtn() == 0) {
//                        fetchTags();
//                        try {
//                            return JSONObject.parseObject(responseModel.getResult(), Tag.class);
//                        } catch (Exception e) {
//                            log.warn("JSON to Object failed. ===> {} ", e.getMessage());
//                        }
//                        return new Tag();
//                    } else {
//                        log.warn("updateTag failed. ===> {} ", responseModel.getMessage());
//                    }
//                }
//            } catch (HttpClientErrorException exception) {
//                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
//                    token = tokenService.getToken();
//                    if (token != null) {
//                        return updateTag(tag);
//                    }
//                }
//            }
//        } else {
//            log.error("Token is null, updateTag error!");
//        }
//        return null;
//    }
//
//    /**
//     * 根据tagName获取 TagId
//     *
//     * @param stringList 机构名称集合
//     * @return 机构id集合
//     */
//    public ArrayList<String> getStaffTagId(String[] stringList) {
//        ArrayList<String> idList = new ArrayList<>();
//        for (String str : stringList) {
//            isExistAndAdd(str);
//            for (Tag tag : staffTagList) {
//                if (str.equals(tag.getTag_name())) {
//                    idList.add(tag.getTag_id());
//                }
//            }
//        }
//        return idList;
//    }
//
//    /**
//     * 根据tagId获取tagName
//     *
//     * @param tagIds
//     * @return
//     */
//    public ArrayList<String> getTagName(List<String> tagIds) {
//        ArrayList<String> tagNames = new ArrayList<>();
//        for (String tagId : tagIds) {
//            for (Tag tag : allTagList) {
//                if (tagId.equals(tag.getTag_id())) {
//                    tagNames.add(tag.getTag_name());
//                    break;
//                }
//            }
//        }
//        return tagNames;
//    }
//
//    public ArrayList<String> getTagIds(List<String> tagNames) {
//        ArrayList<String> tagIds = new ArrayList<>();
//        for (String tagName : tagNames) {
//            Tag tag = isExistAndAdd(tagName);
//            if (tag != null) {
//                tagIds.add(tag.getTag_id());
//            }
//        }
//        return tagIds;
//    }
//
//    /**
//     * 判断标签是否存在，不存在则先新增
//     *
//     * @param tagName
//     */
//    public Tag isExistAndAdd(String tagName) {
//        boolean isExist = false;
//        for (Tag tag : allTagList) {
//            if (tagName.equals(tag.getTag_name())) {
//                isExist = true;
//                return tag;
//            }
//        }
//        if (!isExist) {
//            ArrayList<String> identity = new ArrayList<>();
//            identity.add(Constant.STAFF);
//            Tag tag = createTag(new Tag(tagName, identity, null));
//            return tag;
//        }
//        return null;
//    }
//
//
//    /**
//     * 判断标签是否存在，不存在则先新增
//     *
//     * @param tagName
//     */
//    public Tag VisitorIsExistAndAdd(String tagName) {
//        boolean isExist = false;
//        for (Tag tag : allTagList) {
//            if (tagName.equals(tag.getTag_name())) {
//                isExist = true;
//                return tag;
//            }
//        }
//        if (!isExist) {
//            ArrayList<String> identity = new ArrayList<>();
//            identity.add(Constant.VISITOR);
//            Tag tag = createTag(new Tag(tagName, identity, null));
//            return tag;
//        }
//        return null;
//    }
//
//    /**
//     * 判断标签是否存在,
//     *
//     * @param tagName
//     * @return
//     */
//    public Tag isExist(String tagName) {
//        if (!"".equals(tagName)) {
//            for (Tag tag : allTagList) {
//                if (tagName.equals(tag.getTag_name())) {
//                    return tag;
//                }
//            }
//        }
//        return null;
//    }
//
//
//    public List<Tag> getAllTagList() {
//        return allTagList;
//    }
//
//    public List<Tag> getVisitorTagList() {
//        return visitorTagList;
//    }
//
//    public List<Tag> getStaffTagList() {
//        return staffTagList;
//    }
//
//}

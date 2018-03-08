package com.polysoft.testretrofit.bean;

import com.polysoft.testretrofit.base.BaseBean;

import java.util.List;

/**
 * Created by yang on 2018/3/6.
 */

public class LoginBean extends BaseBean{


    /**
     * data : {"appVersion":{"apptype":"string","createDate":"2018-03-06T09:30:39.545Z","createId":0,"descs":"string","downloadUrl":"string","forces":0,"id":0,"needUpdate":false,"version":"string","versionNum":0},"branchId":0,"cityCode":"string","cityName":"string","codes":["string"],"countyCode":"string","countyName":"string","dealerId":0,"dealerName":"string","dept":{"agentId":0,"branchId":0,"carouselIds":"string","cityCode":"string","code":"string","contactAddress":"string","contactName":"string","contactPhone":"string","countyCode":"string","createDate":"2018-03-06T09:30:39.545Z","createId":0,"delFlag":0,"description":"string","id":0,"name":"string","parentId":0,"provinceCode":"string","shopHours":"string","storeAddress":"string","storeIcon":"string","storeLat":0,"storeLevel":0,"storeLon":0,"storeNickName":"string","storeStatus":0,"storeType":0,"type":0,"updateDate":"2018-03-06T09:30:39.546Z","updateId":0},"deptId":0,"deptName":"string","deptType":0,"hqId":0,"iconUrl":"string","labelIds":[0],"labels":["string"],"loginName":"string","onlineStatus":0,"provinceCode":"string","provinceName":"string","ringUsername":"string","roleIds":[0],"roleName":["string"],"romjuName":"string","superman":0,"token":"string","tokenCode":"string","userId":0,"username":"string"}
     * errorCode : 错误码
     * errorMsg : 错误描述
     * page : {"index":0,"size":0,"totalResult":0}
     * paging : false
     * success : false
     */

    public DataBean data;

    public static class DataBean {
        /**
         * appVersion : {"apptype":"string","createDate":"2018-03-06T09:30:39.545Z","createId":0,"descs":"string","downloadUrl":"string","forces":0,"id":0,"needUpdate":false,"version":"string","versionNum":0}
         * branchId : 0
         * cityCode : string
         * cityName : string
         * codes : ["string"]
         * countyCode : string
         * countyName : string
         * dealerId : 0
         * dealerName : string
         * dept : {"agentId":0,"branchId":0,"carouselIds":"string","cityCode":"string","code":"string","contactAddress":"string","contactName":"string","contactPhone":"string","countyCode":"string","createDate":"2018-03-06T09:30:39.545Z","createId":0,"delFlag":0,"description":"string","id":0,"name":"string","parentId":0,"provinceCode":"string","shopHours":"string","storeAddress":"string","storeIcon":"string","storeLat":0,"storeLevel":0,"storeLon":0,"storeNickName":"string","storeStatus":0,"storeType":0,"type":0,"updateDate":"2018-03-06T09:30:39.546Z","updateId":0}
         * deptId : 0
         * deptName : string
         * deptType : 0
         * hqId : 0
         * iconUrl : string
         * labelIds : [0]
         * labels : ["string"]
         * loginName : string
         * onlineStatus : 0
         * provinceCode : string
         * provinceName : string
         * ringUsername : string
         * roleIds : [0]
         * roleName : ["string"]
         * romjuName : string
         * superman : 0
         * token : string
         * tokenCode : string
         * userId : 0
         * username : string
         */

        public Integer branchId;
        public String cityCode;
        public String cityName;
        public String countyCode;
        public String countyName;
        public Integer dealerId;
        public String dealerName;
        public Integer deptId;
        public String deptName;
        public Integer deptType;
        public Integer hqId;
        public String iconUrl;
        public String loginName;
        public Integer onlineStatus;
        public String provinceCode;
        public String provinceName;
        public String ringUsername;
        public String romjuName;
        public Integer superman;
        public String token;
        public String tokenCode;
        public Integer userId;
        public String username;
        public List<String> codes;
        public List<Integer> labelIds;
        public List<String> labels;
        public List<Integer> roleIds;
        public List<String> roleName;

    }



}

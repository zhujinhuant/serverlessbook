package com.serverlessbook.services.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

public class Token {
	//属性名称，类似关系型数据的字段
    @DynamoDBHashKey(attributeName = "Token")
    private String token;

    @DynamoDBAttribute(attributeName = "UserId")
    private String userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
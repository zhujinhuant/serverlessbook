package com.serverlessbook.lambda.authorizer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
/*
API网关收到一个需求调用RESTAPI的请求时，会在请求内容中填充授权信息，即如下格式：    
{
    "type":"TOKEN",
    "methodArn":"arn:aws:execute-api:us-east-1:083845954160:w8d6mxtdx5/ESTestInvoke-stage/GET/",
    "authorizationToken":"Bearer lasdf"
}
*/
public class AuthorizationInput {

    @JsonProperty("authorizationToken")
    private String authorizationToken;

    @JsonProperty("methodArn")
    private String methodArn;

    @JsonProperty("type")
    private String type;

    /**
     * 返回请求中给出的授权令牌
     * @return Authorization token
     */
    public String getAuthorizationToken() {
        //头文件就分割成2份，取第二个
        return authorizationToken.split(" ", 2)[1];
    }

    /**
     * 返回被调用的API网关方法的ARN。
     * @return Method ARN
     */
    public String getMethodArn() {
        return methodArn;
    }

    /**
     * 有效载荷类型。目前唯一的值是TOKEN
     * @return Payload type
     */
    public String getType() {
        return type;
    }
}
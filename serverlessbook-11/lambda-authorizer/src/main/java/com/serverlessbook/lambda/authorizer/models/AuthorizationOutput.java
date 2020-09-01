package com.serverlessbook.lambda.authorizer.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.serverlessbook.lambda.authorizer.models.policy.PolicyDocument;
/*
格式化输入相关的策略信息，描述的是“资源”被“执行操作”是“同意还是拒绝”
以下描述：arn:aws:execute-api:us-east-1:083845954160:w8d6mxtdx5/ESTestInvoke-stage/GET/ 不允许被调用。
{
    "principalId":"1234",
    "policyDocument":{  
        "Statement":[
            {
                "Action":"execute-api:Invoke",  //
                "Effect":"DENY", //
                "Resource":"arn:aws:execute-api:us-east-1:083845954160:w8d6mxtdx5/ESTestInvoke-stage/GET/"  //
            }],
        "Version":"2012-10-17"  //策略最新版本，旧版2008-10-17
    }
}
*/
public class AuthorizationOutput {

    private final String principalId;

    private final PolicyDocument policyDocument;

    public AuthorizationOutput(String principalId, PolicyDocument policyDocument) {
        this.principalId = principalId;
        this.policyDocument = policyDocument;
    }

    @JsonGetter("principalId")
    public String getPrincipalId() {
        return principalId;
    }

    @JsonGetter("policyDocument")
    public PolicyDocument getPolicyDocument() {
        return policyDocument;
    }
}

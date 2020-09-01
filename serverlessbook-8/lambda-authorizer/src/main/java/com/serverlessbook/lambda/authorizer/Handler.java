package com.serverlessbook.lambda.authorizer;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverlessbook.lambda.LambdaHandler;
import com.serverlessbook.lambda.authorizer.models.AuthorizationInput;
import com.serverlessbook.lambda.authorizer.models.AuthorizationOutput;
import com.serverlessbook.lambda.authorizer.models.policy.PolicyDocument;
import com.serverlessbook.lambda.authorizer.models.policy.PolicyStatement;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
/*
认证方法，确认授权信息中是否有“serverless”，如果有返回通过策略，没有无返回拒绝策略
*/
public class Handler extends LambdaHandler<AuthorizationInput, AuthorizationOutput> {

    @Override
    public AuthorizationOutput handleRequest(AuthorizationInput input, Context context) {
        
        LambdaLogger logger = context.getLogger();

        final String authenticationToken = input.getAuthorizationToken();
        logger.log("lambda log User authentication token " + authenticationToken);

        final PolicyDocument policyDocument = new PolicyDocument();
        //授权信息是serverless就ALLOW，否则就DENY
        final PolicyStatement.Effect policyEffect = "serverless".equals(authenticationToken)?PolicyStatement.Effect.ALLOW:PolicyStatement.Effect.DENY;

        logger.log("PolicyStatement Effect: " + policyEffect);

        policyDocument.withPolicyStatement(new PolicyStatement("execute-api:Invoke",policyEffect, input.getMethodArn()));
        return new AuthorizationOutput("1234", policyDocument);
    }
}
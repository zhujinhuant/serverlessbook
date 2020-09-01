package com.serverlessbook.lambda.authorizer;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverlessbook.lambda.LambdaHandler;
import com.serverlessbook.lambda.authorizer.models.AuthorizationInput;
import com.serverlessbook.lambda.authorizer.models.AuthorizationOutput;
import com.serverlessbook.lambda.authorizer.models.policy.PolicyDocument;
import com.serverlessbook.lambda.authorizer.models.policy.PolicyStatement;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.serverlessbook.services.user.UserNotFoundException;
import com.serverlessbook.services.user.UserService;
import com.serverlessbook.services.user.domain.User;
import org.apache.log4j.Logger;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import java.util.Objects;

/*
认证方法，确认授权信息中是否有“serverless”，如果有返回通过策略，没有无返回拒绝策略
*/
public class Handler extends LambdaHandler<AuthorizationInput, AuthorizationOutput> {
    private static final Logger LOGGER = Logger.getLogger(Handler.class);

    //注入器对象，从这个对象中获取实例
    private static final Injector INJECTOR = Guice.createInjector(new DependencyInjectionModule());
    //用户服务接口，需要注入实例
    private UserService userService;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //构造函数，执行以来注入操作
    public Handler() {
        //要求框架执行以来注入操作
        INJECTOR.injectMembers(this);
        //判断一个对象是否为空,确保框架注入有效的依赖对象
        Objects.requireNonNull(userService);
    }

    @Override
    public AuthorizationOutput handleRequest(AuthorizationInput input, Context context) {
        // LambdaLogger logger = context.getLogger();

        final String authenticationToken = input.getAuthorizationToken();
        final PolicyDocument policyDocument = new PolicyDocument();
        //授权信息是serverless就ALLOW，否则就DENY
        PolicyStatement.Effect policyEffect = PolicyStatement.Effect.ALLOW;
        String principalId = null;

        try {
            //根据token获取用户信息
            User authenticatedUser = userService.getUserByToken(authenticationToken);
            principalId = String.valueOf(authenticatedUser.getId());
            LOGGER.info("Lambda User authentication token: " + authenticationToken);
        } catch (UserNotFoundException userNotFoundException) {
            //没有用户，抛出异常，policy设置为Deny
            policyEffect = PolicyStatement.Effect.DENY;
            LOGGER.info("User authentication failed for token " + authenticationToken);
        }
        
        LOGGER.info("PolicyStatement Effect: " + policyEffect);
        policyDocument.withPolicyStatement(new PolicyStatement("execute-api:Invoke",policyEffect, input.getMethodArn()));
        return new AuthorizationOutput(principalId, policyDocument);
    }
}

















package com.serverlessbook.lambda.userregistration;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.serverlessbook.lambda.LambdaHandler;
import com.serverlessbook.services.user.UserService;
import com.serverlessbook.services.user.domain.User;

import javax.inject.Inject;
import java.util.Objects;
import org.apache.log4j.Logger;

public class Handler extends LambdaHandler<Handler.RegistrationInput, Handler.RegistrationOutput> {

    //内部类
    public static class RegistrationInput {
        @JsonProperty("username")
        private String username;

        @JsonProperty("email")
        private String email;

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }

    //注册成功跳转地址
    public static class RegistrationOutput {

        private final String resourceUrl;

        public RegistrationOutput(User user) {
            resourceUrl = "/user/" + user.getId();
        }

        @JsonGetter("resourceUrl")
        public String getResourceUrl() {
            return resourceUrl;
        }
    }
    //注入器
    private static final Injector INJECTOR = Guice.createInjector(new DependencyInjectionModule());
    //用户service
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(Handler.class);

    private AmazonSNSClient amazonSNSClient;
    
    //在构造器中我们使用 INJECTOR.injectMembers(this);
    //这样我们可以通过New方法，将AmazonSNSClient注入进来。不需要在DependencyInjectionModule的config()中配置。
    @Inject
    public Handler setAmazonSNSClient(AmazonSNSClient amazonSNSClient) {
        this.amazonSNSClient = amazonSNSClient;
        return this;
    }
    
    private void notifySnsSubscribers(User user) {
      try {
        amazonSNSClient.publish(System.getenv("UserRegistrationSnsTopic"), user.getEmail());
        LOGGER.info("SNS notification sent for "+user.getEmail());
      } catch (Exception anyException) {
        LOGGER.info("SNS notification failed for "+user.getEmail(), anyException);
      }
    }


    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Handler() {
        INJECTOR.injectMembers(this);
        Objects.requireNonNull(userService);
    }

    @Override
    public RegistrationOutput handleRequest(RegistrationInput input, Context context) {
        User createdUser = userService.registerNewUser(input.username, input.email);
        //发布该主题的消息
        notifySnsSubscribers(createdUser);
        //返回生成user的原始URL
        return new RegistrationOutput(createdUser);
    }
}

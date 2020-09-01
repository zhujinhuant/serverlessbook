package com.serverlessbook.lambda.userregistration;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.serverlessbook.lambda.LambdaHandler;
import com.serverlessbook.services.user.UserService;
import com.serverlessbook.services.user.domain.User;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

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

    private AmazonSimpleEmailServiceClient simpleEmailServiceClient;
    //直接注入new的AmazonSimpleEmailServiceClient对象
    @Inject
    public Handler setSimpleEmailServiceClient(
            AmazonSimpleEmailServiceClient simpleEmailServiceClient) {
        this.simpleEmailServiceClient = simpleEmailServiceClient;
        return this;
    }

    private void sendEmail(final String emailAddress) {
        //收件地址
        Destination destination = new Destination().withToAddresses(emailAddress);

        Message message = new Message()
                .withBody(new Body().withText(new Content("Welcome ！！!")))
                .withSubject(new Content("Welcome!"));

        //发送邮件，发件地址从配置的环境变量中获取System.getenv("SenderEmail")
        try {
            LOGGER.debug("Sending welcome mail to " + emailAddress);
            simpleEmailServiceClient.sendEmail(new SendEmailRequest()
                    .withDestination(destination)
                    .withSource(System.getenv("SenderEmail"))
                    .withMessage(message)
            );
            LOGGER.debug("Sending welcome mail to " + emailAddress + " succeeded");
        } catch (Exception anyException) {
            LOGGER.error("Sending welcome mail to " + emailAddress + " failed: ", anyException);
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
        //用户注册成功，发送邮件
        sendEmail(input.email);
        //返回生成user的原始URL
        return new RegistrationOutput(createdUser);
    }

}

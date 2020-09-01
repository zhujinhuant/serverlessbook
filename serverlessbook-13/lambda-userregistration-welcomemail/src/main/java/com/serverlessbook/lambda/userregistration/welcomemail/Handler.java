package com.serverlessbook.lambda.userregistration.welcomemail;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Objects;
import javax.inject.Inject;
import org.apache.log4j.Logger;

public class Handler implements RequestHandler<SNSEvent, Void> {

  private static final Injector INJECTOR = Guice.createInjector();

  private static final Logger LOGGER = Logger.getLogger(Handler.class);

  private AmazonSimpleEmailServiceClient simpleEmailServiceClient;

  //直接注入new的AmazonSimpleEmailServiceClient对象
  @Inject
  public Handler setSimpleEmailServiceClient(
      AmazonSimpleEmailServiceClient simpleEmailServiceClient) {
    this.simpleEmailServiceClient = simpleEmailServiceClient;
    return this;
  }

  public Handler() {
    INJECTOR.injectMembers(this);
    Objects.nonNull(simpleEmailServiceClient);
  }

  private void sendEmail(final String emailAddress) {
    //收件地址
    Destination destination = new Destination().withToAddresses(emailAddress);

    Message message = new Message()
        .withBody(new Body().withText(new Content("Welcome to our forum!")))
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

  @Override
  public Void handleRequest(SNSEvent input, Context context) {
    //收到的是标准的SNSEvent事件
    //getRecords()返回的是一个列表，表示Lambda可能一次收多条SNS消息。
    input.getRecords().forEach(snsMessage -> sendEmail(snsMessage.getSNS().getMessage()));
    return null;
  }
}

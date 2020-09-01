package com.serverlessbook.lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverlessbook.lambda.LambdaHandler;
import org.apache.log4j.Logger;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Handler extends LambdaHandler<Handler.TestInput, Handler.TestOutput> {

    static final Logger LOGGER = Logger.getLogger(Handler.class);
        
    static class TestInput {
        public String value;
    }

    static class TestOutput {
        public String value;
    }

    @Override
    public TestOutput handleRequest(TestInput input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("lambda zjh : " + input.value);
        LOGGER.debug("Input from Lambda event: " + input.value);

        TestOutput testOutput = new TestOutput();
        testOutput.value = input.value+"6.15";
        return testOutput;
    }
}

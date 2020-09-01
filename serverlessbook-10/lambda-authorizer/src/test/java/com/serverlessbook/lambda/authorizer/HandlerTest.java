package com.serverlessbook.lambda.authorizer;

import com.serverlessbook.lambda.authorizer.models.AuthorizationInput;
import com.serverlessbook.lambda.authorizer.models.AuthorizationOutput;
import com.serverlessbook.lambda.authorizer.models.policy.PolicyStatement;
import org.junit.Test;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
/*
EasyMock无缝创建模拟对象。
*/
public class HandlerTest {

    @Test
    public void testDependencies() throws Exception {
        Handler testHandler = new Handler();
    }

    @Test
    public void testFailingToken() throws Exception {
        Handler testHandler = new Handler();
        //创建一个AuthorizationInput模拟对象
        AuthorizationInput mockAuthorizationInput = createNiceMock(AuthorizationInput.class);
        //模拟getAuthorizationToken对象的行为，并返回"INVALID_TOKEN"
        //即当调用getAuthorizationToken()方法时，返回"INVALID_TOKEN"
        expect(mockAuthorizationInput.getAuthorizationToken()).andReturn("INVALID_TOKEN").anyTimes();
        //使模拟对象备用
        replay(mockAuthorizationInput);

        AuthorizationOutput authorizationOutput = testHandler.handleRequest(mockAuthorizationInput, null);
        assertEquals(PolicyStatement.Effect.DENY, authorizationOutput.getPolicyDocument().getPolicyStatements().get(0).getEffect());
    }
    
}
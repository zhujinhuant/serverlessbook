package com.serverlessbook.lambda.authorizer;

import com.google.inject.AbstractModule;
import com.serverlessbook.services.user.UserService;
import com.serverlessbook.services.user.UserServiceImpl;
import com.serverlessbook.services.user.repository.UserRepository;
import com.serverlessbook.services.user.repository.UserRepositoryDynamoDB;

/*
Guice配置依赖关系，这里需要继承AbstractModule，做模块之间的关联。
*/
public class DependencyInjectionModule extends AbstractModule {

	//configure() 需要实现的配置关联方法
    @Override
    protected void configure() {
    	//将接口与实现类关联，当需要UserServie接口访问用户信息时
    	//创建并返回UserServiceImpl对象实例。（同Spring的Bean注入）
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryDynamoDB.class);
    }
}

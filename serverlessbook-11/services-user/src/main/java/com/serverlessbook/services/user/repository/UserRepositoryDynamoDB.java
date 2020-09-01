package com.serverlessbook.services.user.repository;

import com.serverlessbook.services.user.domain.Token;
import com.serverlessbook.services.user.domain.User;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;

import com.serverlessbook.services.user.repository.UserRepository;
import javax.inject.Inject;
import java.util.Optional;
/*
用户数据操作层
*/
public class UserRepositoryDynamoDB implements UserRepository {

	private final DynamoDBMapper dynamoDBMapper;

	@Inject
    public UserRepositoryDynamoDB(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

	//暂不去数据库取
    public Optional<User> getUserByToken(String token) {
    	//根据分区键获取Token数据
        Token foundTokenInDynamoDB = dynamoDBMapper.load(Token.class, token);
        if (foundTokenInDynamoDB != null) {
            String userId = foundTokenInDynamoDB.getUserId();
            // 找到Token，根据Token中的Userid，在User表中找User信息
            return Optional.ofNullable(dynamoDBMapper.load(User.class, userId));
        }
        // 没找到Token直接返回空
        return Optional.empty();
    }

    @Override
    public void saveUser(User user) {
        dynamoDBMapper.save(user);
    }

    //获取索引名称，并查询对象查询
    public Optional<User> getUserByCriteria(String indexName, User hashKeyValues) {
        //构建查询表达式
        DynamoDBQueryExpression<User> expression = new DynamoDBQueryExpression<User>()
                .withIndexName(indexName)
                .withConsistentRead(false)
                .withHashKeyValues(hashKeyValues);
        //查询结构
        QueryResultPage<User> result = dynamoDBMapper.queryPage(User.class, expression);

        if (result.getCount() > 0) {
            return Optional.of(result.getResults().get(0));
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getUserByCriteria("EmailIndex", new User().setEmail(email));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return getUserByCriteria("UsernameIndex", new User().setUsername(username));
    }

}

package com.serverlessbook.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import javax.inject.Inject;
/*
自定义 DynamoDBMapper
*/
public class DynamoDBMapperWithCustomTableName extends DynamoDBMapper {

    //需要注入dynamodb客户端
    @Inject
    public DynamoDBMapperWithCustomTableName(AmazonDynamoDBClient amazonDynamoDBClient) {
        this(amazonDynamoDBClient, new EnvironmentVariableTableNameResolver());
    }

    //使用dynamodb客户端和表名解析器构建自定义dynamodbmapper
    public DynamoDBMapperWithCustomTableName(AmazonDynamoDBClient amazonDynamoDBClient, DynamoDBMapperConfig.TableNameResolver tableNameResolver) {
        super(amazonDynamoDBClient,
                DynamoDBMapperConfig
                        .builder()
                        .withTableNameResolver(tableNameResolver)
                        .build());
    }
}

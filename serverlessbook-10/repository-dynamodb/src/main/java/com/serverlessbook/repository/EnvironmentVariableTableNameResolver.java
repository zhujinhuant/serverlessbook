package com.serverlessbook.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameResolver;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
/*
表名解析器
*/
public class EnvironmentVariableTableNameResolver implements TableNameResolver {

	//通过获得类名称，拼接成 "DynamoDb+类名+Table“ 名称
    @Override
    public String getTableName(Class<?> clazz, DynamoDBMapperConfig config) {
        String environmentVariableName = "DynamoDb" + clazz.getSimpleName() + "Table";
        String tableName = System.getenv(environmentVariableName);
        if (tableName == null) {
            throw new DynamoDBMappingException("DynamoDB table name for " + clazz + " cannot be determined. " + environmentVariableName + " environment variable should be set.");
        }
        return tableName;
    }
}

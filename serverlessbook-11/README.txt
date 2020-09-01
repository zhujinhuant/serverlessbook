********************
具体操作步骤：
********************
1. 安装gradle
2. 工程根目录运行：gradle wrapper
3. 文件替换操作，见下文具体内容。
4. 工程根目录运行: ./gradlew test  测试工程
5. 工程根目录运行：./gradlew deploy 发布工程

********************
相关文档地址：
********************
【AWS Lambda笔记-准备工作-1】https://www.jianshu.com/p/1dbaca1f11d2
【AWS Lambda笔记-Gradle配置-2】https://www.jianshu.com/p/f86846a9ba76
【AWS Lambda笔记-HelloWorld-3】https://www.jianshu.com/p/cbd253c88152
【AWS Lambda笔记-CloudFormation-4】https://www.jianshu.com/p/e7bcd615e614
【AWS Lambda笔记-自动部署-5】https://www.jianshu.com/p/c59f8bb8a014
【AWS Lambda笔记-创建API网关-6】https://www.jianshu.com/p/5d7c0b93fc23
【AWS Lambda笔记-内容分发(CDN)-7】https://www.jianshu.com/p/897e49728189
【AWS Lambda笔记-简单授权-8】https://www.jianshu.com/p/c610bd8e26c7
【AWS Lambda笔记-简单授权-9】https://www.jianshu.com/p/179da335e6e4
【AWS Lambda笔记-数据持久化DynamoDB-10】https://www.jianshu.com/p/f4c3347b6924
【AWS Lambda笔记-事件触发Lambda函数-11】https://www.jianshu.com/p/3b514e6b2a93

********************
文件替换操作
********************
build.gradle
	搜索关键字:"您的域名"，进行修改。
cloudformation.template
	搜索关键字:"创建授权证书，您证书的ARN"，进行修改。

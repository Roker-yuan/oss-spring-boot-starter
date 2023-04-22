package com.roker.oss;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.roker.oss.core.OssProperties;
import com.roker.oss.core.OssTemplate;
import com.roker.oss.server.OssTemplateImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss自动配置类
 *
 * @author liupengyuan
 * @date 2023/04/22
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

    /**
     * 开源软件客户端
     *
     * @param properties 属性
     * @return {@link AmazonS3}
     */
    @Bean
    @ConditionalOnMissingBean
    public AmazonS3 ossClient(OssProperties properties){
        // 客户端配置，主要是全局的配置信息
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setMaxConnections(properties.getMaxConnections());
        // url以及region配置
        AwsClientBuilder.EndpointConfiguration endpointConfiguration
                = new AwsClientBuilder.EndpointConfiguration(properties.getEndpoint(), properties.getRegion());
        // 凭证配置
        AWSCredentials awsCredentials = new BasicAWSCredentials(properties.getAccessKey(),
                properties.getSecretKey());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        // build amazonS3Client客户端
        return AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(configuration).withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding().withPathStyleAccessEnabled(properties.getPathStyleAccess()).build();
    }

    @Bean
    @ConditionalOnBean(AmazonS3.class)
    public OssTemplate ossTemplate(AmazonS3 amazonS3){
        return new OssTemplateImpl(amazonS3);
    }

}

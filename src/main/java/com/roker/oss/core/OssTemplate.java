package com.roker.oss.core;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.InputStream;
import java.util.List;

/**
 * OSS操作模板
 *
 * @author liupengyuan
 * @date 2023/04/22
 */
public interface OssTemplate {
    /**
     * 创建bucket
     * @param bucketName bucket名称
     */
    void createBucket(String bucketName);

    /**
     * 获取所有的bucket
     * @return
     */
    List<Bucket> getAllBuckets();

    /**
     * 删除桶
     * @param bucketName bucket名称
     */
    void removeBucket(String bucketName);


    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @param contextType 文件类型
     * @throws Exception
     */
    void upload(String bucketName, String objectName, InputStream stream, String contextType) throws Exception;

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @throws Exception
     */
    void upload(String bucketName, String objectName, InputStream stream) throws Exception;

    /**
     * 获取文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return S3Object
     */
    S3Object get(String bucketName, String objectName);

    /**
     * 获取对象的url
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     */
    String getURL(String bucketName, String objectName, Integer expires);

    /**
     * 通过bucketName和objectName删除对象
     * @param bucketName
     * @param objectName
     * @throws Exception
     */
    void remove(String bucketName, String objectName) throws Exception;

    /**
     * 根据文件前置查询文件
     * @param bucketName bucket名称
     * @param prefix 前缀
     * @param recursive 是否递归查询
     * @return S3ObjectSummary 列表
     */
    List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);




}

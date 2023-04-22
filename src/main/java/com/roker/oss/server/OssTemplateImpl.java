package com.roker.oss.server;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.roker.oss.core.OssTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * OssTemplate的实现类
 *
 * @author liupengyuan
 * @date 2023/04/22
 */


@RequiredArgsConstructor
public class OssTemplateImpl implements OssTemplate {
    private final AmazonS3 amazonS3;

    /**
     * 创建bucket
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateBucket.html
     * @param bucketName bucket名称
     */
    @Override
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)){
            amazonS3.createBucket(bucketName);
        }
    }

    /**
     * 获取所有的bucket
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListBuckets.html
     * @return
     */
    @Override
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    /**
     * 通过Bucket名称删除Bucket
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteBucket.html
     * @param bucketName
     */
    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    /**
     * 上传文件
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param contextType 文件类型
     * @throws Exception
     */
    @Override
    @SneakyThrows
    public void upload(String bucketName, String objectName, InputStream stream, String contextType) throws Exception {
        upload(bucketName, objectName, stream, stream.available(), contextType);
    }

    /**
     * 上传文件
     * AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception
     */
    @Override
    @SneakyThrows
    public void upload(String bucketName, String objectName, InputStream stream) throws Exception {
        upload(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return S3Object
     */
    @Override
    @SneakyThrows
    public S3Object get(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    /**
     * 获取对象的url
     *
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     */
    @Override
    @SneakyThrows
    public String getURL(String bucketName, String objectName, Integer expires) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    /**
     * 通过bucketName和objectName删除对象
     *
     * @param bucketName
     * @param objectName
     * @throws Exception
     */
    @Override
    @SneakyThrows
    public void remove(String bucketName, String objectName) throws Exception {
        amazonS3.deleteObject(bucketName, objectName);
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return S3ObjectSummary 列表
     */
    @Override
    @SneakyThrows
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return objectListing.getObjectSummaries();
    }

    /**
     *
     * @param bucketName
     * @param objectName
     * @param stream
     * @param size
     * @param contextType
     * @return
     */
    @SneakyThrows
    private PutObjectResult upload(String bucketName, String objectName, InputStream stream, long size, String contextType)  {
        byte[] bytes = IOUtils.toByteArray(stream);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contextType);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
    }
}

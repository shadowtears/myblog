package com.xiaomou.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.xiaomou.service.AliOssService;
import com.xiaomou.util.OssUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.xiaomou.constant.CommonConst.DEFAULT_DOMAINS;
import static com.xiaomou.constant.CommonConst.MY_DOMAINS;

/**
 * @author MouHongDa
 * @date 2022/5/4 22:26
 */
@Service
public class AliOssServiceImpl implements AliOssService, InitializingBean {
    @Autowired
    private OssUtil ossUtil;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String folderName;

    @Override
    public void afterPropertiesSet() throws Exception {
        endpoint = ossUtil.getEndpoint();
        accessKeyId = ossUtil.getAccessKeyId();
        accessKeySecret = ossUtil.getAccessKeySecret();
        bucketName = ossUtil.getBucketName();
        folderName = ossUtil.getFolderName();
    }

    @Override
    public void createBucket() {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if (ossClient.doesBucketExist(bucketName)) {
            throw new RuntimeException(bucketName + "对象存储已经存在了");
        }
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public String upload(MultipartFile file) {
        String uploadUrl = "";
        try (InputStream fileInputStream = file.getInputStream()) {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                //设置属性
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }
//          获取当前日期作为文件价格式yyyy/MM/dd
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String dataPath = simpleDateFormat.format(date);
//          获取原文件文件名
            String originalFilename = file.getOriginalFilename();
//          获取 UUID
            String filename = UUID.randomUUID().toString().replaceAll("-", "");
//          获取文件后缀拓展名
            String fileType = originalFilename.substring(originalFilename.lastIndexOf('.'));
//          拼接文件名称
            String newName = filename + fileType;
//          拼接完整文件夹+文件名称  myblog/2020/11/24/UUID.png
            filename = folderName + "/" + dataPath + "/" + newName;
            //实现图片的预览效果
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置公共读权限
            objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);
            // 设置类型
            objectMetadata.setContentType(getcontentType(fileType));
            // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, filename, fileInputStream, objectMetadata);
            //关闭oss
            ossClient.shutdown();
            //默认十年不过期
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            //获取url地址
            uploadUrl = ossClient.generatePresignedUrl(bucketName, filename, expiration).toString();
            uploadUrl = uploadUrl.replace(DEFAULT_DOMAINS, MY_DOMAINS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadUrl;
    }

    /* Description: 判断OSS服务文件上传时文件的contentType */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if
        (FilenameExtension.equalsIgnoreCase(".jpeg")
                || FilenameExtension.equalsIgnoreCase(".jpg")
                || FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if
        (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx")
                || FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if
        (FilenameExtension.equalsIgnoreCase(".docx")
                || FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        } else if
        (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }

    @Override
    public void download(String filename) throws IOException {

    }

    @Override
    public void listFile() {

    }

    @Override
    public void deleteFile(String filename) {

    }
}

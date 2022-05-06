package com.xiaomou.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author MouHongDa
 * @date 2022/5/4 22:25
 */
public interface AliOssService {
    /**
     * //创建存储空间
     */

    void createBucket();

    /**
     * //上传文件
     *
     * @param file
     * @return
     */

    String upload(MultipartFile file);

    /**
     * //下载文件
     *
     * @param filename
     * @throws IOException
     */

    void download(String filename) throws IOException;

    /**
     * 列举文件
     */
    void listFile();

    /**
     * 删除文件
     */
    void deleteFile(String filename);
}

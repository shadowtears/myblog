package com.xiaomou.service.impl;

import com.xiaomou.service.AliOssService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MouHongDa
 * @date 2022/5/19 20:30
 */
@SpringBootTest
class AliOssServiceImplTest {

    @Autowired
    private AliOssService aliOssService;

    @Test
    void upload() throws IOException {
        File file = new File("C:\\Users\\MHD\\Downloads\\cfc2a5cf4e784bf3a4109b7bc9e70875.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",file.getName(),"jpg",inputStream);
        aliOssService.upload(mockMultipartFile);
    }
}
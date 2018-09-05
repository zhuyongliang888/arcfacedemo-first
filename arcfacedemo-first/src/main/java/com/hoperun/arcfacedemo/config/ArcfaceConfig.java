package com.hoperun.arcfacedemo.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArcfaceConfig
{

    // 设置单个文件大小
    @Value("${server.servlet.multipart.singlefilemaxsize}")
    private String singleFileSize;

    @Value("${server.servlet.multipart.totalfilemaxsize}")
    private String totalFileSize;

    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
	MultipartConfigFactory factory = new MultipartConfigFactory();
	// 单个文件最大 KB,MB
	factory.setMaxFileSize(singleFileSize);
	/// 设置总上传数据总大小
	factory.setMaxRequestSize(totalFileSize);
	return factory.createMultipartConfig();
    }
}

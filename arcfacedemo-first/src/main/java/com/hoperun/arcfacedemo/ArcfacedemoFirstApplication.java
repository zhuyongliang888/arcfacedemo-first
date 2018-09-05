package com.hoperun.arcfacedemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoperun.arcfacedemo.utils.RedisUtils;

@RestController
@SpringBootApplication
public class ArcfacedemoFirstApplication
{
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "/redistest")
    public String printHello()
    {
	// 存放字节数组到redis中
	byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };
	List<Map<String, byte[]>> testList = new ArrayList<>();
	Map<String, byte[]> tempMap = new HashMap<>();
	tempMap.put("bytes", bytes);
	testList.add(tempMap);
	redisUtils.saveObject("tempList", testList);
	List<Map<String, byte[]>> resultList = (List<Map<String, byte[]>>) redisUtils.getObject("tempList");
	System.out.println(resultList);
	Map<String, byte[]> resultMap = (Map<String, byte[]>) resultList.get(0);
	System.out.println(resultMap);
	byte[] resultByte = resultMap.get("bytes");
	System.out.println(resultByte[0]);

	// 以下代码会报错 java.lang.String cannot be cast to [B",
	// redisUtils.saveObject("tempbytes",bytes);
	// byte[] resultByteArray =(byte[]) redisUtils.getObject("tempbytes");
	// System.out.println(resultByteArray[2]);

	return "hello world";
    }

    public static void main(String[] args)
    {
	System.out.println("SprigBoot start------------------------------");
	SpringApplication.run(ArcfacedemoFirstApplication.class, args);
	System.out.println("SpringBoot end-------------------------------");
    }
}

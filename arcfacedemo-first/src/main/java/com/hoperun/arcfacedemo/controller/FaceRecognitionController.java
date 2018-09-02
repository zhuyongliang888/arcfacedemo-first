package com.hoperun.arcfacedemo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoperun.arcfacedemo.arcsoft.cls.AFR_FSDK_FACEMODEL;
import com.hoperun.arcfacedemo.arcsoft.cls.TestUser;
import com.hoperun.arcfacedemo.arcsoft.utils.ArcfaceUtils;
import com.hoperun.arcfacedemo.common.ErrorCode;
import com.hoperun.arcfacedemo.response.entity.CommonResponseBody;
import com.hoperun.arcfacedemo.utils.RedisUtils;
import com.hoperun.arcfacedemo.utils.StringUtils;

@RestController
public class FaceRecognitionController {

	private final static Logger LOGGER = LoggerFactory.getLogger(FaceRecognitionController.class);
	
	@Autowired
	private RedisUtils redisUtils;
	// 保存图片的路径
	private final String filePath = "D:\\data_p";
	private final String P_NAME = "name";
	private final String P_FILE = "file";

	private final String[] fileTypes = new String[]{"YUV","JPG","PNG","BMP"};
	
	@RequestMapping(value = "api/arcface/userFaceRegister", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String userFaceRegister(@RequestParam(P_FILE) MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		CommonResponseBody commonResponseBody = new CommonResponseBody();
		String name = request.getParameter(P_NAME);
		if (StringUtils.isEmpty(name)) {

			commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_NOTNULL);
			commonResponseBody.setMessage(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_NAME));
			response.setStatus(commonResponseBody.getCode() / 10000);
			LOGGER.error(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_NAME));
			return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
		}

		if (file == null) {
			commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_NOTNULL);
			commonResponseBody.setMessage(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_FILE));
			response.setStatus(commonResponseBody.getCode() / 10000);
			LOGGER.error(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_FILE));
			return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
		}

		// 获取原始文件名
		String fileName = file.getOriginalFilename();
		if(!detectionFileType(fileName)) {
			commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
			commonResponseBody.setMessage("file type not support");
			response.setStatus(commonResponseBody.getCode() / 10000);
			LOGGER.error("file type not support");
			return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
		}
		
		// 获取文件后缀
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		// 重新生成唯一文件名
		String newFileName = UUID.randomUUID().toString() + suffixName;
		LOGGER.debug("new file name is "+newFileName);
		// 创建文件
		File dest = new File(filePath + newFileName);
		LOGGER.debug("new file name is "+newFileName);
		
		try {
			file.transferTo(dest);
		} catch (Exception e) {

			commonResponseBody.setCode(5030000);
			commonResponseBody.setMessage("save file fail");
			response.setStatus(commonResponseBody.getCode() / 10000);
			LOGGER.error(" save file fail ",e);
			return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
		}
		/*//获取file图片的特征值,并以字节的方式存放到redis中
		List<TestUser> saveList = new ArrayList<>();
		TestUser user1 = new TestUser();
		TestUser user2 = new TestUser();
		user1.setAge(10);
		user1.setName("zhuzhu");
		user2.setAge(20);
		user2.setName("liangliang");
		saveList.add(user1);
		saveList.add(user2);
		redisUtils.saveObject("testkey", saveList);
		
		List<TestUser> getList = (List<TestUser>)redisUtils.getObject("testkey");
		TestUser user3 = getList.get(0);
		TestUser user4 = getList.get(1);
		if(user3.getAge()==10) {
			System.out.println("perfect!!!!");
		}*/
		
		List<String> aTestList = new ArrayList<>();
		AFR_FSDK_FACEMODEL faceModel = ArcfaceUtils.extractFRFeature(dest);
		try {
			byte[] faceModelbyte = faceModel.toByteArray();
			System.out.println("从faceModel中转化后的字节数组打印:\n"+faceModelbyte);
			//字节数组转换成16进制表示的字符串
			String tempStr = StringUtils.bytes2Hex(faceModelbyte);
			System.out.println("转换成string："+tempStr);
			aTestList.add(tempStr);
			redisUtils.saveObject("faceModelKey1", aTestList);
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		List<String> resultList = (List<String>)redisUtils.getObject("faceModelKey");
		String resultTemp = resultList.get(0);
		System.out.println("从redis中取出来转换成String的字节数组"+resultTemp);
		byte[] resultByte = StringUtils.hexStringToByteArray(resultTemp);
		System.out.println("从redis中取出来的字节数组:\n"+resultByte);
		
		try {
			AFR_FSDK_FACEMODEL resultMode = AFR_FSDK_FACEMODEL.fromByteArray(resultByte);
			System.out.println(ArcfaceUtils.compareFaceSimilarity(faceModel, resultMode));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		commonResponseBody.setCode(0);
		commonResponseBody.setMessage("SUCCESS!");
		response.setStatus(200);
		return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);

	}
	
	private boolean detectionFileType(String fileName) {
		String suffixName = fileName.substring(fileName.lastIndexOf(".")).replaceFirst("\\.", "");
		if(StringUtils.isEmpty(suffixName)) {
			return false;
		}
		suffixName = suffixName.trim();
		for(String str:fileTypes) {
			if(str.toUpperCase().equals(suffixName.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}

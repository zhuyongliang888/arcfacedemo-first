package com.hoperun.arcfacedemo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.hoperun.arcfacedemo.arcsoft.utils.ArcfaceUtils;
import com.hoperun.arcfacedemo.common.Constant;
import com.hoperun.arcfacedemo.common.ErrorCode;
import com.hoperun.arcfacedemo.response.entity.CommonDataResponse;
import com.hoperun.arcfacedemo.response.entity.CommonResponseBody;
import com.hoperun.arcfacedemo.utils.RedisUtils;
import com.hoperun.arcfacedemo.utils.StringUtils;

@RestController
public class FaceRecognitionController
{

    private final static Logger LOGGER = LoggerFactory.getLogger(FaceRecognitionController.class);

    @Autowired
    private RedisUtils redisUtils;
    // 保存图片的路径
    private final String FILE_PATH = Constant.FILE_PATH;
    private final String P_NAME = "name";
    private final String P_FILE = "file";
    private final String FACEMODE = "face_mode";

    // 暂时定义个固定值，实际传输应hub端传入其唯一标识符
    private final String DEVICE_ID = "hub-device-id";

    private final float SIMILARITY_VALUE = 0.75f;

    private final String[] fileTypes = new String[] { "YUV", "JPG", "PNG", "BMP" };

    // 创建文件目录
    {
	File file = new File(FILE_PATH);

	if (!file.exists())
	{
	    file.mkdir();
	}

    }

    /**
     * 人脸信息注册
     * 
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "api/arcface/userFaceRegister", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String userFaceRegister(@RequestParam(P_FILE) MultipartFile file, HttpServletRequest request,
	    HttpServletResponse response)
    {
	CommonResponseBody commonResponseBody = new CommonResponseBody();
	String name = request.getParameter(P_NAME);
	if (StringUtils.isEmpty(name))
	{

	    commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_NOTNULL);
	    commonResponseBody.setMessage(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_NAME));
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_NAME));
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	if (file == null)
	{
	    commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_NOTNULL);
	    commonResponseBody.setMessage(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_FILE));
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_FILE));
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	// 获取原始文件名
	String fileName = file.getOriginalFilename();
	if (!detectionFileType(fileName))
	{
	    commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    commonResponseBody.setMessage("file type not support");
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error("file type not support");
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	// 获取文件后缀
	String suffixName = fileName.substring(fileName.lastIndexOf("."));
	// 重新生成唯一文件名,uuid_name.jpg,将name也一起存入的文件名中
	String newFileName = UUID.randomUUID().toString() + "_" + name + suffixName;
	LOGGER.debug("new file name is " + newFileName);
	// 创建文件
	File dest = new File(FILE_PATH + newFileName);
	LOGGER.debug("new file name is " + newFileName);

	try
	{
	    file.transferTo(dest);
	} catch (Exception e)
	{
	    deleteFile(dest);
	    commonResponseBody.setCode(5030000);
	    commonResponseBody.setMessage("save file fail");
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error(" save file fail ", e);
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	// 1.根据key 值 DEVICE_ID的值从redis中获取value --List<String>
	@SuppressWarnings("unchecked")
	List<Map<String, String>> faceModeStringList = (List<Map<String, String>>) redisUtils.getObject(DEVICE_ID);
	if (faceModeStringList == null)
	{
	    faceModeStringList = new ArrayList<Map<String, String>>();
	    // 此时需要去判断下存放图片的目录中是否存在图片,如果存在的话，将其中的所有图片特征值放入redis一份

	}

	// 1.获取此图片文件的特征值
	String faceModeString = getFaceModelValueString(dest);
	System.out.println("-------------------------将获取的图片特征值装换成String\n" + faceModeString);
	if (StringUtils.isEmpty(faceModeString))
	{
	    // 获取特征值失败
	    commonResponseBody.setCode(ErrorCode.ERROR_CODE_GET_FACEMODE_FAIL);
	    commonResponseBody.setMessage("get the face mode value fail!");
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error("get the face mode value fail!");
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}
	// 2.将 图片文件的特征值和名称一起存入redis中
	Map<String, String> valueMap = new HashMap<>();
	valueMap.put(P_NAME, name);
	valueMap.put(FACEMODE, faceModeString);
	faceModeStringList.add(valueMap);
	redisUtils.saveObject(DEVICE_ID, faceModeStringList);

	// 测试一下
	// @SuppressWarnings("unchecked")
	// List<Map<String, String>> testList =
	// (List<Map<String,String>>)redisUtils.getObject(DEVICE_ID);
	// Map<String,String> tempMap =testList.get(0);
	// System.out.println("从redis中再取出后的特征值\n"+tempMap.get(FACEMODE));

	commonResponseBody.setCode(0);
	commonResponseBody.setMessage("SUCCESS!");
	response.setStatus(200);
	return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);

    }

    /**
     * 人脸识别
     * 
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "api/arcface/userFaceRecognize", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String userFaceRecognize(@RequestParam(P_FILE) MultipartFile file, HttpServletRequest request,
	    HttpServletResponse response)
    {
	CommonResponseBody commonResponseBody = new CommonResponseBody();

	if (file == null)
	{
	    commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_NOTNULL);
	    commonResponseBody.setMessage(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_FILE));
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error(String.format(ErrorCode.ERROR_MSG_PARAMETER_NULL, P_FILE));
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	// 获取原始文件名
	String fileName = file.getOriginalFilename();
	if (!detectionFileType(fileName))
	{
	    commonResponseBody.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    commonResponseBody.setMessage("file type not support");
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error("file type not support");
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	// 获取文件后缀
	String suffixName = fileName.substring(fileName.lastIndexOf("."));

	// 创建临时文件
	File tempDest = null;
	try
	{
	    tempDest = File.createTempFile(UUID.randomUUID().toString(), suffixName);
	    file.transferTo(tempDest);
	} catch (Exception e)
	{
	    deleteFile(tempDest);
	    commonResponseBody.setCode(5030000);
	    commonResponseBody.setMessage("create temp file fail");
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error(" create temp file fail ", e);
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	// 1.获取此图片文件的特征值
	AFR_FSDK_FACEMODEL faceMode = ArcfaceUtils.extractFRFeature(tempDest);

	String faceModeString = getFaceModelValueString(tempDest);
	if (StringUtils.isEmpty(faceModeString))
	{
	    // 获取特征值失败
	    commonResponseBody.setCode(ErrorCode.ERROR_CODE_GET_FACEMODE_FAIL);
	    commonResponseBody.setMessage("get the face mode value fail!");
	    response.setStatus(commonResponseBody.getCode() / 10000);
	    LOGGER.error("get the face mode value fail!");
	    return JSON.toJSONString(commonResponseBody, SerializerFeature.WriteMapNullValue);
	}

	deleteFile(tempDest);

	CommonDataResponse<Map<String, Object>> commonDataResponse = new CommonDataResponse<>();
	Map<String, Object> tempResultMap = new HashMap<>();

	// 比较此特征值比较后的相似度是否大于75%
	// 1.根据key 值 DEVICE_ID的值从redis中获取value --List<Map<String,String>
	@SuppressWarnings("unchecked")
	List<Map<String, String>> faceModeStringList = (List<Map<String, String>>) redisUtils.getObject(DEVICE_ID);
	if (faceModeStringList == null)
	{
	    // 直接返回不能识别
	    commonDataResponse.setCode(0);
	    commonDataResponse.setMessage("compare fail!");
	    tempResultMap.put("recognition", false);
	    response.setStatus(200);
	    LOGGER.error("compare fail");
	    return JSON.toJSONString(commonDataResponse, SerializerFeature.WriteMapNullValue);
	}
	System.out.println("faceModeStringList.size()==" + faceModeStringList.size());
	// 判断图片能否识别
	boolean isSuccess = false;
	String resultName = "";
	for (int i = 0; i < faceModeStringList.size(); i++)
	{
	    Map<String, String> tempMap = faceModeStringList.get(i);
	    String tempFaceModeString = tempMap.get(FACEMODE);
	    if (StringUtils.isEmpty(tempFaceModeString))
	    {
		continue;

	    }

	    byte[] tempfaceModeBytes = StringUtils.hexStringToByteArray(tempFaceModeString);
	    // 获取AFR_FSDK_FACEMODEL 对象
	    AFR_FSDK_FACEMODEL tempFaceModel;
	    try
	    {
		tempFaceModel = AFR_FSDK_FACEMODEL.fromByteArray(tempfaceModeBytes);

	    } catch (Exception e)
	    {
		tempFaceModel = null;
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    if (tempFaceModel != null)
	    {
		// 获得比较值
		float compareFaceSimilarityValue = ArcfaceUtils.compareFaceSimilarity(faceMode, tempFaceModel);
		tempFaceModel.freeUnmanaged();
		LOGGER.debug("compareFaceSimilarityValue==" + compareFaceSimilarityValue);
		if (compareFaceSimilarityValue >= SIMILARITY_VALUE)
		{
		    System.out.println("recognize success!");
		    isSuccess = true;
		    resultName = tempMap.get(P_NAME);

		    break;
		}
	    }
	}
	faceMode.freeUnmanaged();

	LOGGER.debug("COMPARE SUCCESS OR FAIL:" + isSuccess);
	if (isSuccess)
	{
	    // 返回成功识别
	    // 直接返回不能识别
	    commonDataResponse.setCode(0);
	    commonDataResponse.setMessage("compare success!");
	    tempResultMap.put("recognition", true);
	    tempResultMap.put("name", resultName);
	    commonDataResponse.setData(tempResultMap);
	    response.setStatus(200);
	    LOGGER.debug("compare success");
	    return JSON.toJSONString(commonDataResponse, SerializerFeature.WriteMapNullValue);
	} else
	{
	    // 返回无法识别
	    // 直接返回不能识别
	    commonDataResponse.setCode(0);
	    commonDataResponse.setMessage("compare fail!");
	    tempResultMap.put("recognition", false);
	    commonDataResponse.setData(tempResultMap);
	    response.setStatus(200);
	    LOGGER.debug("compare fail");
	    return JSON.toJSONString(commonDataResponse, SerializerFeature.WriteMapNullValue);
	}

    }

    /**
     * 删除文件
     * 
     * @param files
     */
    private void deleteFile(File... files)
    {
	for (File file : files)
	{
	    if (null != file && file.exists())
	    {
		file.delete();
	    }
	}
    }

    /**
     * 获取该图片信息的特征值转换后的16进制字符串
     * 
     * @param dest
     * @return
     */
    private String getFaceModelValueString(File dest)
    {
	String resultStr = "";
	AFR_FSDK_FACEMODEL faceModel = ArcfaceUtils.extractFRFeature(dest);
	if (faceModel == null)
	{
	    return "";
	}
	try
	{
	    byte[] faceModelbyte = faceModel.toByteArray();

	    // 字节数组转换成16进制表示的字符串
	    resultStr = StringUtils.bytes2Hex(faceModelbyte);
	    return resultStr;
	} catch (Exception e1)
	{

	    e1.printStackTrace();
	    return "";
	}
    }

    /**
     * 判断图片后缀类型是否支持
     * 
     * @param fileName
     * @return
     */
    private boolean detectionFileType(String fileName)
    {
	String suffixName = fileName.substring(fileName.lastIndexOf(".")).replaceFirst("\\.", "");
	if (StringUtils.isEmpty(suffixName))
	{
	    return false;
	}
	suffixName = suffixName.trim();
	for (String str : fileTypes)
	{
	    if (str.toUpperCase().equals(suffixName.toUpperCase()))
	    {
		return true;
	    }
	}
	return false;
    }
}

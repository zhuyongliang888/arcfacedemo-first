
AFD_FSDK_FACERES 检测到的脸部信息

AFD_FSDK_VERSION SDK版本信息

AFD_FSDK_OrientPriority 定义脸部角度的检测范围

AFD_FSDK_OrientCode 定义人脸检测结果的人脸角度

ASVL_PAF_I420

ASVL_PAF_YUYV

ASVL_PAF_RGB24_B8G8R8  颜色格式及其对其规则

AFD_FSDK_InitialFaceEngine 初始化脸部检测引擎

AFD_FSDK_StillmageFaceDetection 根据输入的图像检测出人脸位置，一般用于静态图像检测

AFD_FSDK_UninitialFaceEngine 销毁引擎，释放相应资源

AFD_FSDK_GetVersion 获取SDK版本信息

人脸比对
AFR_FSDK_FACEINPUT 脸部信息

AFR_FSDK_FACEMODEL 脸部特征信息

AFR_FSDK_VERSION 引擎版本信息

AFR_FSDK_ORIENTCODE 基于逆时针的脸部方向枚举值

AFR_FSDK_InitialEngine 初始化引擎参数

AFR_FSDK_ExtractFRFeature 获取脸部特征参数

AFR_FSDK_FacePairMatching 脸部特征比较

AFR_FSDK_UninitialEngine 销毁引擎，释放相应资源

AFR_FSDK_GetVersion 获取SDK版本信息参数


ret返回的信息
MOK	00000	成功
MERR_BASIC_BASE	00001	基础错误起始值
MERR_UNKNOWN	00002	未知错误
MERR_INVALID_PARAM	00003	参数错误
MERR_UNSUPPORTED	00004	输入了引擎不支持的参数或者数据
MERR_NO_MEMORY	00005	内存不足
MERR_BAD_STATE	00006	状态错误（未初始化就调用了接口）
MERR_BUFFER_OVERFLOW	00009	内存上溢
MERR_BUFFER_UNDERFLOW	00010	内存下溢
MERR_FSDK_BASE	28672	校验错误起始值
MERR_FSDK_INVALID_APP_ID	28673	非法APPID
MERR_FSDK_INVALID_SDK_ID	28674	非法SDKID
MERR_FSDK_INVALID_ID_PAIR	28675	SDKKEY不是于当前APPID名下的
MERR_FSDK_MISMATCH_ID_AND_SDK	28676	SDKKEY不是当前SDK所支持的
MERR_FSDK_SYSTEM_VERSION_UNSUPPORTED	28677	不支持的系统版本
MERR_FSDK_LICENCE_EXPIRED	28678	SDK过期
MERR_FSDK_FR_ERROR_BASE	73728	FR错误起始值
MERR_FSDK_FR_INVALID_MEMORY_INFO	73729	内存信息错误
MERR_FSDK_FR_INVALID_IMAGE_INFO	73730	图像信息错误
MERR_FSDK_FR_INVALID_FACE_INFO	73731	人脸信息错误
MERR_FSDK_FR_NO_GPU_AVAILABLE	73732	GPU不支持
MERR_FSDK_FR_MISMATCHED_FEATURE_LEVEL	73733	特征信息版本不匹配





















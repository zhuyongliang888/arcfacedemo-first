package com.hoperun.arcfacedemo.arcsoft.cls;

import com.hoperun.arcfacedemo.arcsoft.utils.LoadUtils;
import com.hoperun.arcfacedemo.common.Constant;
import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public interface AFD_FSDKLibrary extends Library
{

    // AFD_FSDKLibrary INSTANCE =
    // (AFD_FSDKLibrary)LoadUtils.loadLibrary(Platform.isWindows()?"libarcsoft_fsdk_face_detection.dll":"libarcsoft_fsdk_face_detection.so",AFD_FSDKLibrary.class);
    AFD_FSDKLibrary INSTANCE = (AFD_FSDKLibrary) LoadUtils
	    .loadLibrary(Platform.isWindows() ? Constant.WINDOWS_X64_ARCFACE_FSDK_DETECTION_SO_PATH
		    : Constant.LINUX_X64_ARCFACE_FSDK_DETECTION_SO_PATH, AFD_FSDKLibrary.class);

    NativeLong AFD_FSDK_InitialFaceEngine(String appid, String sdkid, Pointer pMem, int lMemSize,
	    PointerByReference phEngine, int iOrientPriority, int nScale, int nMaxFaceNum);

    NativeLong AFD_FSDK_StillImageFaceDetection(Pointer hEngine, ASVLOFFSCREEN pImgData, PointerByReference pFaceRes);

    NativeLong AFD_FSDK_UninitialFaceEngine(Pointer hEngine);

    AFD_FSDK_Version AFD_FSDK_GetVersion(Pointer hEngine);
}
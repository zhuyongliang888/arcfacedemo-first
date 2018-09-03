package com.hoperun.arcfacedemo.common;

import com.sun.jna.Platform;

public class Constant
{
    
    //public static final String LINUX_X64_ARCFACE_FSDK_DETECTION_SO_PATH = "/home/zhuyongliang/test-test/libarcsoft_fsdk_face_detection.so";
    //public static final String LINUX_X64_ARCFACE_FSDK_RECOGNITION_SO_PATH = "/home/zhuyongliang/test-test/libarcsoft_fsdk_face_recognition.so";
    public static final String LINUX_X64_ARCFACE_FSDK_DETECTION_SO_PATH = "/home/needso/libarcsoft_fsdk_face_detection.so";
    public static final String LINUX_X64_ARCFACE_FSDK_RECOGNITION_SO_PATH = "/home/needso/libarcsoft_fsdk_face_recognition.so";
    
    public static final String WINDOWS_X64_ARCFACE_FSDK_DETECTION_SO_PATH = "D:\\libarcsoft_fsdk_face_detection.dll";
    public static final String WINDOWS_X64_ARCFACE_FSDK_RECOGNITION_SO_PATH = "D:\\libarcsoft_fsdk_face_recognition.dll";
    //public static final String FILE_PATH = Platform.isWindows()?"D:\\data_p3\\":"/home/zhuyongliang/data_p/";
    //public static final String FILE_PATH = Platform.isWindows()?"D:\\data_p3\\":"/usr/services/service-face-recognition/data_p/";
    public static final String FILE_PATH = Platform.isWindows()?"D:\\data_p3\\":"/home/arcface_service/data_p/";
    public static final String APPID = Platform.isWindows()?"4dBSjEpL7vj4LJHZRqMr1EvTggWapqCfVkZRXcKzFDgA":"4dBSjEpL7vj4LJHZRqMr1EvTggWapqCfVkZRXcKzFDgA";
    public static final String FD_SDKKEY=Platform.isWindows()?"AGPxD5wcFdKdMb8ezdiMfN3DhbYC3ufZJ86sso8haYPb":"AGPxD5wcFdKdMb8ezdiMfN3DZUxtDZn5bvsPBxr44hV5";
    public static final String FR_SDKKEY=Platform.isWindows()?"AGPxD5wcFdKdMb8ezdiMfN3iMCapbcZ9Yw1UFSjcRr5H":"AGPxD5wcFdKdMb8ezdiMfN3iD61dVVSttyMqAUqiusEh";
}

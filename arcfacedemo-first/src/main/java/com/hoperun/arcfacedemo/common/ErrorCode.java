package com.hoperun.arcfacedemo.common;

/**
 * Error Code
 * @author zhuyongliang
 *
 */
public class ErrorCode {
	
	public static final String KEYWORD_ERROR = "error";
	public static final String KEYWORD_CODE = "code";
	public static final String KEYWORD_MSG = "message";
	public static final String KEYWORD_STATUS = "responese_status";

	public static final int CODE_OK = 200;
	public static final int CODE_BAD_REQUEST = 400;
	public static final int CODE_UNAUTHORIZED = 401;
	public static final int CODE_FORBIDDEN = 403;
	public static final int CODE_NOT_FOUND = 404;
	public static final int CODE_SERICE_UNAVAIABLE = 503;

	//******400
	public static final Integer ERROR_CODE_PARAMETER_NOTNULL = 4001000;
	public static final String ERROR_MSG_PARAMETER_NULL = "paramter [%s] must not null";

	public static final Integer ERROR_CODE_PARAMETER_ERROR = 4001001;
	public static final String ERROR_MSG_PARAMETER_ERROR = "paramter [%s] = %s error";

	//******403
	public static final Integer ERROR_CODE_NO_TOKEN = 4031000;
	public static final String ERROR_MSG_NO_TOKEN = "have not Access-Token";

	public static final Integer ERROR_CODE_EXPIRED_TOKEN = 4031001;
	public static final String ERROR_MSG_EXPIRED_TOKEN = "Access-Token Expired";

	//******404
	public static final Integer ERROR_CODE_NO_EXIST = 4041000;
	public static final String ERROR_MSG_NO_EXIST = "[%s] = %s not exist";




}

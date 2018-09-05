package com.hoperun.arcfacedemo.arcsoft.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class HelloWorld
{

    public interface CLibarary extends Library
    {
	CLibarary INSTANCE = (CLibarary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibarary.class);

	void printf(String format, Object... objects);
    }

    public static void main(String[] args)
    {
	// TODO Auto-generated method stub
	CLibarary.INSTANCE.printf("Hello,World\n");
	for (int i = 0; i < args.length; i++)
	{
	    CLibarary.INSTANCE.printf("Argument %d:%s\n", i, args[i]);
	}

    }

}

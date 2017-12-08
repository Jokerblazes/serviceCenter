package com.joker.server.entity;

public class MessageConstant {
	
	
	
	public static final byte  Header = (byte)0x6e;
	
	
	public static final int  maxFrameLength = Short.MAX_VALUE;
	//public static final int  maxFrameLength = 1024;
	public static final int lengthFieldOffset = 1; // 帧中长度字段（帧长）偏移量
	public static final int lengthFieldLength = 2; // 帧中长度字段（帧长）长度
	public static final int lengthAdjustment = -3; // 帧中业务数据读取字段偏移量
	public static final int initialBytesToStrip = 0;
	
	
}

package com.joker.server.entity;

import java.util.Arrays;

public class Head {
	private byte head;
	private int length;
	private byte[] url;
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public byte getHead() {
		return head;
	}
	public void setHead(byte head) {
		this.head = head;
	}
	public byte[] getUrl() {
		return url;
	}
	public void setUrl(byte[] url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "Head [head=" + head + ", length=" + length + ", url=" + Arrays.toString(url) + "]";
	}
	
}

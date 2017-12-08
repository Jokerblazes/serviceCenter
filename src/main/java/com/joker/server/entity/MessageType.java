
package com.joker.server.entity;

public enum MessageType {
	
//    ShakeHand((byte) 0x00),
    Login((byte) 0x01), 
    Other((byte) 0x02), 
	HEARTBEAT((byte) 0x03),
	PROVIDER_REGIST((byte) 0x04),
	CUSTOMER_REGIST((byte) 0x05),
	CUSTOMER_CLOSE((byte) 0x06),
	PROVIDER_CLOSE((byte) 0x07),

//	Exception ((byte) 0x11),
//	Error ((byte) 0x00),
	Success ((byte) 0x01);

	
	
	

	private byte value;

	private MessageType(byte value) {
		this.value = value;
	}

	public byte value() {
		return this.value;
	}
}

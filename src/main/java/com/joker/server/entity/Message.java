package com.joker.server.entity;


import java.util.Arrays;


import io.netty.buffer.ByteBuf;

public class Message implements Cloneable {

    @Override
    public Object clone() {
        Message o = null;
        try {
            o = (Message) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    private Head head;

    private int length;

    private byte channel;

    private byte direction;

    private int source;

    private int dest;

    private byte cmdType;

    private byte opStatus;

    private byte[] optionData;

    private short checkSum;

    private long id;

    private ByteBuf frame;

    public int getLength() {
        return length;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getDirection() {
        return direction;
    }

    public void setDirection(byte direction) {
        this.direction = direction;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public byte getCmdType() {
        return cmdType;
    }

    public void setCmdType(byte cmdType) {
        this.cmdType = cmdType;
    }

    public byte getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(byte opStatus) {
        this.opStatus = opStatus;
    }

    public byte[] getOptionData() {
        return optionData;
    }

    public void setOptionData(byte[] optionData) {
        this.optionData = optionData;
    }


    public short getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(short checkSum) {
        this.checkSum = checkSum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ByteBuf getFrame() {
        return frame;
    }

    public void setFrame(ByteBuf frame) {
        this.frame = frame;
    }

    public Message() {
    }

    public static Message messageResult(byte[] bytes, byte result, Head head) {
        Message message = new Message();
        message.setHead(head);
        message.setHead(head);
        message.setOptionData(bytes);
        message.setCmdType(MessageType.Other.value());
        message.setOpStatus(result);
        if (bytes == null)
            message.setLength(18 + head.getLength());
        else
            message.setLength(bytes.length + 18 + head.getLength());
        return message;
    }

    public static Message messageResult(byte[] bytes, byte result, String url) {
        Message message = new Message();
        Head head = new Head();
        head.setHead(MessageConstant.Header);
        byte[] urlBytes = url.getBytes();
        head.setLength(urlBytes.length);
        head.setUrl(urlBytes);
        message.setHead(head);
        message.setHead(head);
        message.setOptionData(bytes);
        message.setCmdType(MessageType.Other.value());
        message.setOpStatus(result);
        if (bytes == null)
            message.setLength(18 + head.getLength());
        else
            message.setLength(bytes.length + 18 + head.getLength());
        return message;
    }

    public static Message messageDecode(ByteBuf byteBuf) {
        Message message = new Message();
        message.setFrame(byteBuf.copy());
        Head head = new Head();
        head.setHead(byteBuf.readByte());
        if (head.getHead() != MessageConstant.Header)
            return null;
        message.setLength(byteBuf.readUnsignedShort());
        int headLength = byteBuf.readByte();
        head.setLength(headLength);
        byte[] urlData = new byte[headLength];
        byteBuf.readBytes(urlData.length).getBytes(0, urlData);
        head.setUrl(urlData);
        Arrays.toString(urlData);
        message.setHead(head);

        message.setChannel(byteBuf.readByte());

        message.setDirection(byteBuf.readByte());

        message.setSource(byteBuf.readInt());

        message.setDest(byteBuf.readInt());

        message.setCmdType(byteBuf.readByte());

        message.setOpStatus(byteBuf.readByte());

        byte[] data = new byte[message.getLength() - headLength - 18];
        byteBuf.readBytes(data.length).getBytes(0, data);

        message.setOptionData(data);

        message.setCheckSum(byteBuf.readShort());
        return message;

    }

    @Override
    public String toString() {
        return "Message [head=" + head + ", length=" + length + ", channel=" + channel + ", direction=" + direction
                + ", source=" + source + ", dest=" + dest + ", cmdType=" + cmdType + ", opStatus=" + opStatus
                + ", optionData=" + Arrays.toString(optionData) + ", checkSum=" + checkSum + ", id=" + id + ", frame="
                + frame + "]";
    }



}

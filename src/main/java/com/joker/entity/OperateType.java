package com.joker.entity;

/**
 * 操作类型
 * Created by joker on 2017/12/8.
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public enum OperateType {
    DELETE(1),
    ADD(2);

    private int value;

    private OperateType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}

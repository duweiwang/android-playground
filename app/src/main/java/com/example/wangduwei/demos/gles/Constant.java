package com.example.wangduwei.demos.gles;

public class Constant {

    public static final int POSITION_COMPONENT_COUNT = 3;
    public static final int COLOR_COMPONENT_COUNT = 3;

    public static final int BYTE_PER_FLOAT = 4;
    //跨距，位置坐标点之间又多少个字节
    public static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTE_PER_FLOAT;
}

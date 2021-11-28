package com.example.wangduwei.demos.media.camera.camera1;

/**
 * @desc: 这是一个对象，保存对象的基本属性，坐标等，调用对象的draw方法就把对象画上去
 * @auther:duwei
 * @date:2019/3/6
 */
public class Camera1GLPreviewObject {
    //每行前两个值为顶点坐标，后两个为纹理坐标
    private static final float[] vertexData = {
            1f, 1f, 1f, 1f,
            -1f, 1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
            1f, 1f, 1f, 1f,
            -1f, -1f, 0f, 0f,
            1f, -1f, 1f, 0f
    };

    public Camera1GLPreviewObject(){

    }


    public void draw(){

    }

}

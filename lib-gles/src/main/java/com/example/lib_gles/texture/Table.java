package com.example.lib_gles.texture;

import android.opengl.GLES20;

import com.example.lib_gles.Constant;
import com.example.lib_gles.VertexArray;


/**
 * @desc:
 * @auther:duwei
 * @date:2018/10/18
 */

public class Table {
    /**
     * 桌子坐标由XY组成
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    /**
     * 桌子的纹理坐标ST
     */
    public static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    /**
     * 跨距是4 * 4
     */
    public static final int STRIDE = (POSITION_COMPONENT_COUNT +
            TEXTURE_COORDINATES_COMPONENT_COUNT) * Constant.BYTE_PER_FLOAT;

    /**
     * 顶点数据
     */
    private static final float[] VERTEX_DATA = {
            //x,y,s,t
            //为什么T是0.1和0.9,：因为图像是512*1024，
            // 而桌子是1:1.6，我们裁剪了图片的上下各0.1
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,

            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,

            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f

    };

    /**
     * native层数据
     */
    private final VertexArray vertexArray;


    public Table() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    /**
     * 绑定顶点数据去着色器
     */
    public void bindData(TextureShaderProgram textureShaderProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureShaderProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );

    }


    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }

}

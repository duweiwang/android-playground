package com.example.wangduwei.demos.media.camera.camera1;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.example.lib_gles.ShaderHelper;
import com.example.lib_gles.TextResourceReader;
import com.example.wangduwei.demos.R;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/3/6
 */
public class Camera1GLPreviewShader {
    private int mProgram;

    private final int aPositionLocation ;
    private final int aTextureCoordLocation ;
    private final int uTextureMatrixLocation ;
    private final int uTextureSamplerLocation ;

    public Camera1GLPreviewShader(Context context) {
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, R.raw.vertex_camera1glpreview),
                TextResourceReader.readTextFileFromResource(context, R.raw.fragment_camera1glpreview));

        //获取Shader中定义的变量在program中的位置
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, "aPosition");
        aTextureCoordLocation = GLES20.glGetAttribLocation(mProgram, "aTextureCoordinate");
        uTextureMatrixLocation = GLES20.glGetUniformLocation(mProgram, "uTextureMatrix");
        uTextureSamplerLocation = GLES20.glGetUniformLocation(mProgram, "uTextureSampler");

        useProgram();
    }

    public int getaPositionLocation() {
        return aPositionLocation;
    }

    public int getaTextureCoordLocation() {
        return aTextureCoordLocation;
    }


    public void useProgram(){
        GLES20.glUseProgram(mProgram);
    }

    public void setUniforms(float[] matrix, int textureId){

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);  //激活纹理单元0
        //绑定外部纹理到纹理单元0
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        //将此纹理单元传给片段着色器的uTextureSampler外部纹理采样器
        GLES20.glUniform1i(uTextureSamplerLocation, 0);

        //将纹理矩阵传给片段着色器
        GLES20.glUniformMatrix4fv(uTextureMatrixLocation,
                1, false, matrix, 0);
    }
}

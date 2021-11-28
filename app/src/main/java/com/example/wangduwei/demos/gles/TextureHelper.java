package com.example.wangduwei.demos.gles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/10/17
 */

public class TextureHelper {

    /**
     * @param context
     * @param resourceId
     * @return 加载图像后的OpenGL纹理ID
     */
    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjId = new int[1];
        GLES20.glGenTextures(1, textureObjId, 0);

        if (textureObjId[0] == 0) {
            Log.d("wdw", "产生纹理出错");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;//不缩放

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resourceId, options);

        if (bitmap == null) {

            GLES20.glDeleteTextures(1, textureObjId, 0);
            return 0;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjId[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,//缩小的情况下
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR_MIPMAP_LINEAR);//缩小情况下使用三线性过滤

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,//放大的情况下
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);//放大情况下使用双线性过滤
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        bitmap.recycle();

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);//传0，解除绑定
        return textureObjId[0];
    }


}

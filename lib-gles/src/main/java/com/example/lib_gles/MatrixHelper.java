package com.example.lib_gles;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/10/17
 */

public class MatrixHelper {

    //投影矩阵
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        //第一列
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;
        //第二列
        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;
        //第三列
        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;
        //第四列
        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }


}

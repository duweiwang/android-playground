package com.example.lib_gles.video_filter.core.bean;

public enum FillMode {
    PRESERVE_ASPECT_FIT,//等比缩放，完整显示内容，不裁剪，可能会留黑
    PRESERVE_ASPECT_CROP,//等比缩放，铺满输出画布，超出部分会被裁
    CUSTOM;

    public static float[] getScaleAspectFit(int angle, int widthIn, int heightIn, int widthOut, int heightOut) {
        final float[] scale = {1, 1};
        scale[0] = scale[1] = 1;
        if (angle == 90 || angle == 270) {//如果有旋转，宽高互换
            int cx = widthIn;
            widthIn = heightIn;
            heightIn = cx;
        }
        //输入的宽高比
        float aspectRatioIn = (float) widthIn / (float) heightIn;
        //宽度固定，输出的高度
        float heightOutCalculated = (float) widthOut / aspectRatioIn;

        if (heightOutCalculated < heightOut) {
            scale[1] = heightOutCalculated / heightOut;
        } else {
            scale[0] = heightOut * aspectRatioIn / widthOut;
        }

        return scale;
    }

    public static float[] getScaleAspectCrop(int angle, int widthIn, int heightIn, int widthOut, int heightOut) {
        final float[] scale = {1, 1};
        scale[0] = scale[1] = 1;
        if (angle == 90 || angle == 270) {
            int cx = widthIn;
            widthIn = heightIn;
            heightIn = cx;
        }

        float aspectRatioIn = (float) widthIn / (float) heightIn;
        float aspectRatioOut = (float) widthOut / (float) heightOut;

        if (aspectRatioIn > aspectRatioOut) {
            float widthOutCalculated = (float) heightOut * aspectRatioIn;
            scale[0] = widthOutCalculated / widthOut;
        } else {
            float heightOutCalculated = (float) widthOut / aspectRatioIn;
            scale[1] = heightOutCalculated / heightOut;
        }

        return scale;
    }
}

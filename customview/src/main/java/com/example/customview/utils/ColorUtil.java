package com.example.customview.utils;

import android.graphics.Color;

public class ColorUtil {

    public static int getAlphaColor(int color, float alpha) {
        int a = ((int) (alpha * 255)) % 256;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(a, r, g, b);
    }

    public static int getAlphaColorNoAlpha(int color) {
        float a = Color.alpha(color) / (float)0xff;
        int r = (int) (Color.red(color) * a);
        int g = (int) (Color.green(color) * a);
        int b = (int) (Color.blue(color) * a);
        return Color.argb(0xff, r, g, b);
    }

    public static int getHybridAlphaColor(int color, float alpha) {
        int a = (int)(Color.alpha(color) * alpha);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(a, r, g, b);
    }

    /**
     * @param alpha1 0.0 - 1.0
     * @param alpha2 0.0 - 1.0
     * @return
     */
    public static int mixColor(int color1, int color2, float alpha1, float alpha2) {
        return (int) ((color1 * alpha1 * (1 - alpha2) + color2 * alpha2) / (alpha1 + alpha2 - alpha1 * alpha2));
    }

    public static int getSC_Color(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = Math.max(0.2f, hsv[1] - 0.6f);
        hsv[2] = 1.0f;
        return Color.HSVToColor(hsv);
    }

    //根据color id 获取颜色16进制值
    public static String changeColor(int color){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("#");
        String alpha = Integer.toHexString(Color.alpha(color));
        alpha = alpha.length() < 2 ? ("0" + alpha) : alpha;
        stringBuffer.append(alpha);
        String red = Integer.toHexString(Color.red(color));
        red = red.length() < 2 ? ("0" + red) : red;
        stringBuffer.append(red);
        String green = Integer.toHexString(Color.green(color));
        green = green.length() < 2 ? ("0" + green) : green;
        stringBuffer.append(green);
        String blue = Integer.toHexString(Color.blue(color));
        blue = blue.length() < 2 ? ("0" + blue) : blue;
        stringBuffer.append(blue);
        return stringBuffer.toString();
    }

    /**
     * 返回色值表中的位置
     * @param bitmap
     * @return
     */
//    public static int getBitmapColor(Bitmap bitmap) {
//        int imageColor;
//        Palette.Swatch vibrantSwatch = Palette.generate(bitmap).getVibrantSwatch();
//        if (null == vibrantSwatch)
//            vibrantSwatch = Palette.generate(bitmap).getDarkVibrantSwatch();
//        if (null == vibrantSwatch)
//            imageColor = ImageUtil.matchColorByImage(bitmap, ImageUtil.SkinColor);
//        else
//            imageColor = ImageUtil.matchNearColor(vibrantSwatch.getRgb(), ImageUtil.SkinColor);
//        for (int i = 0; i < SkinColorLib.colors.length; i++) {
//            if (imageColor == SkinColorLib.colors[i]) {
//                return i;   //0为自选色
//            }
//        }
//        return 0;
//    }

    public static String colorInt2HexString(int color){
        return "#"+Integer.toHexString(color).substring(2);
    }

    public static int getProgressColor(float progress,int startColor,int endColor){
        int startInt = (Integer) startColor;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endColor;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(progress * (endA - startA))) << 24) |
                (int)((startR + (int)(progress * (endR - startR))) << 16) |
                (int)((startG + (int)(progress * (endG - startG))) << 8) |
                (int)((startB + (int)(progress * (endB - startB))));
    }

}

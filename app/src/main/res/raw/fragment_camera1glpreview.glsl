#extension GL_OES_EGL_image_external : require //使用外部纹理必须支持此扩展
precision mediump float;

uniform samplerExternalOES uTextureSampler;    //外部纹理采样器
varying vec2 vTextureCoord;
void main() {
    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord); //获取此纹理（预览图像）对应坐标的颜色值
//    float fGrayColor = (0.3*vCameraColor.r + 0.59*vCameraColor.g+  0.11*vCameraColor.b);   //求此颜色的灰度值
//    gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);   //将此灰度值作为输出颜色的RGB值，这样就会变成黑白滤镜
        gl_FragColor = vec4((1.0 - vCameraColor.rgb), vCameraColor.w);
}
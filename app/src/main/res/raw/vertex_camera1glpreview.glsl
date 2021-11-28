attribute vec4 aPosition;
uniform mat4 uTextureMatrix;     //纹理矩阵
attribute vec4 aTextureCoordinate;   //自己定义的纹理坐标
varying vec2 vTextureCoord;    //传给片段着色器的纹理坐标

void main()  {
    vTextureCoord = (uTextureMatrix * aTextureCoordinate).xy;
    gl_Position = aPosition;
}
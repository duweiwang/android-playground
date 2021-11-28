precision mediump float;

uniform sampler2D u_TextureUnit;//二维纹理数据的数据
varying vec2 v_TextureCoordinates;

void main(){
    gl_FragColor = texture2D(u_TextureUnit , v_TextureCoordinates);

}
package com.example.lib_gles.video_filter.filter_impl.filter;

import com.example.lib_gles.video_filter.core.filter.GlFilter;
import android.opengl.GLES20;

public class GlGammaFilter extends GlFilter {
    private static final String GAMMA_FRAGMENT_SHADER = "" +
            "precision mediump float;" +
            " varying vec2 vTextureCoord;\n" +
            " \n" +
            " uniform lowp sampler2D sTexture;\n" +
            " uniform lowp float gamma;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n" +
            "     \n" +
            "     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n" +
            " }";

    public GlGammaFilter() {
        super(DEFAULT_VERTEX_SHADER, GAMMA_FRAGMENT_SHADER);
    }

    private float gamma = 1.2f;

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    @Override
    protected void onDraw(long presentationTimeUs) {
        GLES20.glUniform1f(getHandle("gamma"), gamma);
    }


}

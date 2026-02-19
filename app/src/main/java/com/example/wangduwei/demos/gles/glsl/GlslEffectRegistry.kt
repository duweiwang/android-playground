package com.example.wangduwei.demos.gles.glsl

import com.example.wangduwei.demos.R

object GlslEffectRegistry {
    val effects: List<ShaderEffectItem> = listOf(
        ShaderEffectItem(
            name = "Awesome Fragment Shader",
            shaderResId = R.raw.awesome_fragment_shader
        ),
        ShaderEffectItem(
            name = "Magic Fragment Shader",
            shaderResId = R.raw.magic_fragment_shader
        ),
        ShaderEffectItem(
            name = "Fractal Pyramid (Shadertoy)",
            shaderResId = R.raw.fractal_pyramid_fragment_shader
        ),
        ShaderEffectItem(
            name = "Heartfelt Rain (Shadertoy)",
            shaderResId = R.raw.heartfelt_rain_fragment_shader
        ),
        ShaderEffectItem(
            name = "Flame Raymarch (Shadertoy)",
            shaderResId = R.raw.flame_raymarch_fragment_shader
        ),
        ShaderEffectItem(
            name = "Fire Burn (Mobile)",
            shaderResId = R.raw.fire_burn_fragment_shader
        ),
        ShaderEffectItem(
            name = "Aurora Noise (Shadertoy)",
            shaderResId = R.raw.aurora_noise_fragment_shader
        ),
        ShaderEffectItem(
            name = "【基础】触摸屏幕换色",
            shaderResId = R.raw.mouse_color_fragment_shader
        ),
        ShaderEffectItem(
            name = "【基础】双矩形",
            shaderResId = R.raw.double_rect_fragment_shader
        ),
        ShaderEffectItem(
            name = "【基础】噪声双色流动",
            shaderResId = R.raw.noise_dual_color_fragment_shader
        )
    )
}

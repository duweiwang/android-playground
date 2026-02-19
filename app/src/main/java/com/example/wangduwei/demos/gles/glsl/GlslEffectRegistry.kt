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
        )
    )
}

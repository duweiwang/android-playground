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
        )
    )
}

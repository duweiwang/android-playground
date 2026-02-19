package com.example.wangduwei.demos.gles.glsl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment

@PageInfo(
    description = "GLSL特效预览",
    navigationId = R.id.fragment_glsl_view,
    title = "GLSL特效预览",
    preview = R.drawable.preview_opengl
)
class GlslDemoFragment : BaseSupportFragment() {
    private val effects = GlslEffectRegistry.effects

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_glsl_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_glsl_effects)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = EffectAdapter(effects) { item ->
            findNavController().navigate(
                R.id.fragment_glsl_shader_preview,
                bundleOf(
                    ShaderPreviewFragment.ARG_SHADER_RES_ID to item.shaderResId,
                    ShaderPreviewFragment.ARG_EFFECT_NAME to item.name
                )
            )
        }
    }

    private class EffectAdapter(
        private val items: List<ShaderEffectItem>,
        private val onClick: (ShaderEffectItem) -> Unit
    ) : RecyclerView.Adapter<EffectAdapter.EffectViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EffectViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return EffectViewHolder(view)
        }

        override fun onBindViewHolder(holder: EffectViewHolder, position: Int) {
            holder.bind(items[position], onClick)
        }

        override fun getItemCount(): Int = items.size

        class EffectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val titleView: TextView = itemView.findViewById(android.R.id.text1)

            fun bind(item: ShaderEffectItem, onClick: (ShaderEffectItem) -> Unit) {
                titleView.text = item.name
                itemView.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }
}

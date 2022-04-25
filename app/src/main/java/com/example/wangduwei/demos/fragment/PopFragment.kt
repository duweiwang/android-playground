package com.example.wangduwei.demos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.customview.pop.PopView
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import kotlinx.android.synthetic.main.fragment_pop_view.*

/**
 *  @auther: davewang
 *  @since: 2019/7/16
 **/
@PageInfo(
    description = "弹出气泡",
    navigationId = R.id.fragment_pop,
    title = "Popup",
    preview = R.drawable.preview_pop
)
class PopFragment : BaseSupportFragment() {

    lateinit var mContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pop_view, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        left.setOnClickListener {
            var popView = PopView.Builder()
                .setTargetView(left)
                .setPosition(PopView.Position.LEFT).setTipText("我是左边")
                .setWidth(250).setHeight(100)
                .build()
            popView.show()
        }
        top.setOnClickListener {
            var popView = PopView.Builder()
                .setTargetView(top)
                .setPosition(PopView.Position.TOP)
                .setTipText("我是上边")
                .setWidth(250).setHeight(100)
                .build()
            popView.show()
        }

        right.setOnClickListener {
            var popView = PopView.Builder()
                .setTargetView(right)
                .setPosition(PopView.Position.RIGHT)
                .setTipText("我是右边")
                .setWidth(250).setHeight(100)
                .build()
            popView.show()
        }

        bottom.setOnClickListener {
            var popView =
                PopView.Builder()
                    .setTargetView(bottom)
                    .setPosition(PopView.Position.BOTTOM)
                    .setTipText("我是下边")
                    .setWidth(250).setHeight(100)
                    .build()
            popView.show()
        }

        mContainer = view.findViewById(R.id.container)
        align_bottom.setOnClickListener {
            var lp: RelativeLayout.LayoutParams =
                mContainer.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            mContainer.layoutParams = lp
        }
        center_verticle.setOnClickListener {
            var lp: RelativeLayout.LayoutParams =
                mContainer.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
            mContainer.layoutParams = lp
        }
    }

}
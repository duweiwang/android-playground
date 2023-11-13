package com.example.wangduwei.demos.opensource

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuCompat
import androidx.core.view.postDelayed
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.AbsBusinessDelegate
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import me.saket.cascade.CascadePopupMenu
import me.saket.cascade.allChildren
import me.saket.cascade.sample.RoundedRectDrawable

/**
 * @author 杜伟
 * @date 2023/11/13 17:23
 *
 */
class PopupMenuCascadeDelegate : AbsBusinessDelegate() {

    override fun onViewCreated(view: View?) {
        super.onViewCreated(view)
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.inflateMenu(R.menu.toolbar)

        val menuButton = toolbar?.findViewById<View>(R.id.overflow_menu)
        showcaseMenuButton(toolbar, menuButton!!)
        menuButton.setOnClickListener {
            showCascadeMenu(anchor = menuButton)
        }
    }
    private fun showcaseMenuButton(toolbar: Toolbar?, menuButton: View) {
        val tapTarget = TapTarget
            .forToolbarMenuItem(toolbar, R.id.overflow_menu, "Tap to see Cascade in action")
            .transparentTarget(true)
            .outerCircleColor(R.color.color_control_normal)
            .titleTextColor(R.color.window_background)

        TapTargetView.showFor(mActivity, tapTarget, object : TapTargetView.Listener() {
            override fun onTargetClick(view: TapTargetView) {
                super.onTargetClick(view)
                view.postDelayed(200) {
                    menuButton.performClick()
                }
            }
        })
    }

    private fun showCascadeMenu(anchor: View) {
        val popupMenu = CascadePopupMenu(anchor.context, anchor, styler = cascadeMenuStyler())
        popupMenu.menu.apply {
            MenuCompat.setGroupDividerEnabled(this, true)

            add("About").setIcon(R.drawable.ic_language_24)
            add("Copy").setIcon(R.drawable.ic_file_copy_24)
            addSubMenu("Share").also {
                val addShareTargets = { sub: SubMenu ->
                    sub.add("PDF")
                    sub.add("EPUB")
                    sub.add("Image")
                    sub.add("Web page")
                    sub.add("Markdown")
                    sub.add("Plain text", groupId = 42)
                    sub.add("Microsoft word", groupId = 42)
                }
                it.setIcon(R.drawable.ic_share_24)
                addShareTargets(it.addSubMenu("To clipboard"))
                addShareTargets(it.addSubMenu("As a file"))
            }
            addSubMenu("Remove").also {
                it.setIcon(R.drawable.ic_delete_sweep_24)
                it.setHeaderTitle("Are you sure?")
                it.add("Yep").setIcon(R.drawable.ic_check_24)
                it.add("Go back").setIcon(R.drawable.ic_close_24)
            }
            addSubMenu("Cash App").also {
                it.setIcon(cashAppIcon())
                it.add("contour").intent = intent("https://github.com/cashapp/contour")
                it.add("duktape").intent = intent("https://github.com/cashapp/duktape-android")
                it.add("misk").intent = intent("https://github.com/cashapp/misk")
                it.add("paparazzi").intent = intent("https://github.com/cashapp/paparazzi")
                it.add("sqldelight").intent = intent("https://github.com/cashapp/SQLDelight")
                it.add("turbine").intent = intent("https://github.com/cashapp/turbine")
            }

            allChildren.filter { it.intent == null }.forEach {
                it.setOnMenuItemClickListener {
                    popupMenu.navigateBack()
                }
            }
        }
        popupMenu.show()
    }

    private fun cascadeMenuStyler(): CascadePopupMenu.Styler {
        val rippleDrawable = {
            RippleDrawable(ColorStateList.valueOf(Color.parseColor("#B1DDC6")), null, ColorDrawable(BLACK))
        }

        return CascadePopupMenu.Styler(
            background = {
                RoundedRectDrawable(Color.parseColor("#E0EEE7"), radius = 8f.dip)
            },
            menuTitle = {
                it.titleView.typeface = ResourcesCompat.getFont(mView.context, R.font.work_sans_medium)
                it.setBackground(rippleDrawable())
            },
            menuItem = {
                it.titleView.typeface = ResourcesCompat.getFont(mView.context, R.font.work_sans_medium)
                it.setBackground(rippleDrawable())
                it.setGroupDividerColor(Color.parseColor("#BED9CF"))
            }
        )
    }

    private fun cashAppIcon() =
        AppCompatResources.getDrawable(mView.context, R.drawable.ic_language_24)!!.also {
            it.mutate()
            it.setTint(ContextCompat.getColor(mView.context, R.color.color_control_normal))
        }

    private fun intent(url: String) =
        Intent(ACTION_VIEW, Uri.parse(url))

    override fun onDestroy() {
    }
    private val Float.dip: Float
        get() {
            val metrics = mView.context.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
        }
}
fun Menu.add(
    title: CharSequence,
    itemId: Int = Menu.NONE,
    groupId: Int = Menu.NONE,
    order: Int = Menu.NONE,
    onClick: ((MenuItem) -> Unit)? = null
): MenuItem = add(groupId, itemId, order, title).apply {
    if (onClick != null) {
        setOnMenuItemClickListener {
            onClick(it)
            true
        }
    }
}

package com.example.wangduwei.demos.serialization

import com.widget.any.view.default.SystemWidgetFactory

/**
 * @author 杜伟
 * @date 2022/7/14 9:55 AM
 *
 * 用于对Widget进行创建操作
 *
 */
class WidgetBuilder private constructor(
    private val root: WidgetGroup
) {

    private val position2Widget = mutableMapOf<Position, WidgetGroup>()

    companion object {
        /**
         * 创建一次空编辑操作
         */
        fun create(
            name: String,
            size: Size
        ): WidgetBuilder {

            val root = WidgetGroup(
                name = name,
                category = WidgetCategory.CUSTOM,
                author = WidgetAuthor.AUTHOR_USER,
                size = size,
                position = Position(0, 0),
                lastModify = System.currentTimeMillis()
            )

            return WidgetBuilder(root)
        }

        /**
         * 加载并进行编辑操作
         */
        fun load() {


        }
    }

    /**
     * 获取最外层widget
     */
    fun root(): WidgetGroup {
        return root
    }


    /**
     * 拖拽操作
     * @param position 拖拽到的位置
     * @param widget 哪个组件
     */
    fun add(position: Position, widget: SystemDefaultWidget): WidgetGroup? {

        val systemWidget = SystemWidgetFactory.create(widget, position)
        val result = root.addWidget(systemWidget)
        return if (result) {
            position2Widget[position] = systemWidget
            systemWidget
        } else null
    }

    /**
     * 删除指定位置的widget
     */
    fun delete(position: Position): Boolean {

        position2Widget.remove(position)
        val result = root.children.first {
            it is WidgetGroup && it.position == position
        }

        return if (result != null)
            root.removeWidget(result)
        else false
    }

    /**
     * 通过对象删除
     */
    fun delete(widgetGroup: WidgetGroup): Boolean {
        position2Widget.remove(widgetGroup.position)
        return root.removeWidget(widgetGroup)
    }

    /**
     * 选中某个widget
     */
    fun selected(position: Position): WidgetGroup? {

        val result = root.children.firstOrNull {
            it is WidgetGroup && it.position == position
        }
        return if (result == null) result else result as WidgetGroup
    }


    /**
     * 替换一个小组件
     * @return 替换后的小组件
     */
    fun replace(old: WidgetGroup, new: SystemDefaultWidget): WidgetGroup? {

        val deleteR = delete(old.position)

        if (deleteR) {
            return add(old.position, new)
        }
        return null
    }

    /**
     * 保存操作
     */
    fun save() {

    }

    /**
     * 取消退出操作
     */
    fun discard() {

        position2Widget.clear()
    }

}
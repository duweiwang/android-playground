package com.example.wangduwei.project.selector


/**
 * 列表的选择器
 */
interface Selector<T> {
    /**
     * 反正选择
     */
    fun toggleSelect(position: Int)

    /**
     * 该数据是否选择
     */
    fun isSelectedData(data: T): Boolean

    /**
     * 下标的数据是否选择
     */
    fun isSelected(index: Int): Boolean

    /**
     * 选择单个
     */
    fun select(position: Int)

    /**
     * 选择列表数据
     * 是否清空原来的
     */
    fun select(list: List<T>, replace: Boolean = false)

    /**
     * 清除选择
     */
    fun clearSelected()

    fun clearSelected(list: List<T>)

    /**
     * 是否选择了所有
     */
    fun isSelectedAll(): Boolean

    /**
     * 选择所有
     */
    fun selectAll()

    /**
     * 已选择的数量
     */
    fun count(): Int

    /**
     * 选择列表数据
     */
    fun datas(): List<T>

}
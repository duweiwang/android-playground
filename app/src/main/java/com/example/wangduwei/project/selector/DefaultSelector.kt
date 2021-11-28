package com.example.wangduwei.project.selector

/**
 * 集合选择器：
 *
 *  用户选择列表item后，监听到选择的回调。通知recyclerview-notifyDataSetChanged
 *  重新绑定数据时判断数据是否被选中。
 *  其实就是一个环路。
 *
 */
class DefaultSelector<T>(private val dataList: List<T>) : Selector<T> {

    private var mSelectedList = hashSetOf<T>()
    private val excludeList = hashSetOf<T>()

    private var selectChangedListener: SelectChangedListener<T>? = null

    override fun toggleSelect(position: Int) {
        val data = dataList[position]
        if (excludeList.contains(data)) return
        if (mSelectedList.contains(data)) {
            mSelectedList.remove(data)
            selectChangedListener?.onChanged(SelectChangedListener.TYPE_UNSELECT, listOf(data))
        } else {
            mSelectedList.add(data)
            selectChangedListener?.onChanged(SelectChangedListener.TYPE_SELECT, listOf(data))
        }
    }

    override fun isSelectedData(data: T): Boolean {
        return mSelectedList.contains(data)
    }

    override fun isSelected(index: Int): Boolean {
        return mSelectedList.contains(dataList[index])
    }

    override fun select(position: Int) {
        val data = dataList[position]
        if (!excludeList.contains(data) && !mSelectedList.contains(data)) {
            mSelectedList.add(data)
            selectChangedListener?.onChanged(SelectChangedListener.TYPE_SELECT, listOf(data))
        }
    }

    override fun select(list: List<T>, replace: Boolean) {
        if (replace) {
            mSelectedList.clear()
        }
        for (data in list) {
            if (!excludeList.contains(data) && dataList.contains(data)) {
                mSelectedList.add(data)
            }
        }
        selectChangedListener?.onChanged(SelectChangedListener.TYPE_SELECT, mSelectedList.toList())
    }

    override fun clearSelected() {
        if (mSelectedList.isNotEmpty()) {
            mSelectedList.clear()
            selectChangedListener?.onChanged(SelectChangedListener.TYPE_UNSELECT, dataList)
        }
    }

    override fun clearSelected(list: List<T>) {
        if (mSelectedList.removeAll(list)) {
            selectChangedListener?.onChanged(SelectChangedListener.TYPE_UNSELECT, list)
        }
    }

    override fun isSelectedAll(): Boolean {
        return mSelectedList.isNotEmpty() && mSelectedList.size == (dataList.size - excludeList.size)
    }

    override fun selectAll() {
        if (!isSelectedAll()) {
            val selectList = mutableListOf<T>()
            dataList.forEach {
                if (!excludeList.contains(it) && mSelectedList.add(it)) {
                    selectList.add(it)
                }
            }
            if (selectList.isNotEmpty()) {
                selectChangedListener?.onChanged(SelectChangedListener.TYPE_SELECT, selectList)
            }
        }
    }

    override fun count(): Int {
        return mSelectedList.size
    }

    override fun datas(): List<T> {
        return mSelectedList.toList()
    }

    fun selectableCount(): Int {
        return dataList.size - excludeList.size
    }

    interface SelectChangedListener<T> {
        companion object {
            const val TYPE_SELECT = 1
            const val TYPE_UNSELECT = 2
        }

        fun onChanged(type: Int, changedList: List<T>)
    }

    fun setSelectChangedListener(listener: SelectChangedListener<T>) {
        selectChangedListener = listener
    }

    fun addExcludeList(list: List<T>, clear: Boolean) {
        if (clear) {
            excludeList.clear()
        }
        excludeList.addAll(list)
    }
}
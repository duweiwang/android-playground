package com.example.wangduwei.demos.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.customview.sidebar.SideBar
import com.example.lib_processor.FragmentInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.router.RouterManager
import com.example.wangduwei.demos.service.ForegroundService
import kotlinx.android.synthetic.main.fragment_main.*


/**
 * RecyclerView:
 * 1.[RecyclerView.canScrollVertically]方法解析
 * 当RecyclerView已经滑动到底部时，`canScrollVertically(1)`返回false `canScrollVertically(-1)`返回true
 * 所以可以通过此方法判断RecyclerView是否滑到底部或顶部
 */

class MainFragment : BaseSupportFragment(), MainAdapter.OnItemClickListener,SideBar.OnTouchingLetterChangedListener {

    private lateinit var mAdapter: MainAdapter
    private lateinit var mTitles: List<FragmentInfo>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_recyclerview!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val vertically = main_recyclerview!!.canScrollVertically(1)
                val vertically2 = main_recyclerview!!.canScrollVertically(-1)
                if (vertically) {
                    Log.d("wdw-recyclerview", "可以滑动1")
                }
                if (vertically2) {
                    Log.d("wdw-recyclerview", "可以滑动-1")
                }
            }
        })
        mTitles = RouterManager.getInstance().pages
        mAdapter = MainAdapter(mTitles, activity)
        mAdapter.setOnItemClickListener(this)

        main_recyclerview.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )//GridLayoutManager(activity, 2)
//            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = mAdapter
        }

        floatbtn.setOnClickListener {
            guideView.attachTarget(floatbtn);
            val intent = Intent(context, ForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(intent)
            }
        }

        val mSideBar = view.findViewById<SideBar>(R.id.school_friend_sidrbar)
        val mDialog = view.findViewById<TextView>(R.id.school_friend_dialog)
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
    }

    override fun onItemClick(position: Int) {
        val navController = findNavController()
        navController.navigate(mTitles[position].id)
    }

    override fun onTouchingLetterChanged(s: String?) {
    }
}

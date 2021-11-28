package com.example.wangduwei.demos.main.appinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import com.example.wangduwei.demos.main.appinfo.build.BuildInfoDelegate
import com.example.wangduwei.demos.main.appinfo.cpu.CpuInfoDelegate
import com.example.wangduwei.demos.main.appinfo.display.DisplayInfoDelegate
import com.example.wangduwei.demos.main.appinfo.network.NetWorkInfoDelegate
import com.example.wangduwei.demos.main.appinfo.os.OsInfoDelegate

/**
 * <p>获取手机的基本信息-信息太多了，业务代理模式全部把逻辑挂出去</p>
 * @author duwei
 * @since 2019/6/29
 */
class MainInformationFragment : BaseSupportFragment() {

    private lateinit var displayDelegate: DisplayInfoDelegate
    private lateinit var cpuInfoDelegate: CpuInfoDelegate
    private lateinit var buildInfoDelegate: BuildInfoDelegate
    private lateinit var osInfoDelegate: OsInfoDelegate
    private lateinit var networkDelegate: NetWorkInfoDelegate

    companion object {
        fun newInstance(): MainInformationFragment = MainInformationFragment().apply {
            val bundle = Bundle()
            arguments = bundle
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myself, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayDelegate = DisplayInfoDelegate()
        displayDelegate.onViewCreated(view.findViewById(R.id.app_display_info))

        cpuInfoDelegate = CpuInfoDelegate()
        cpuInfoDelegate.onViewCreated(view.findViewById(R.id.app_cpu_info))

        buildInfoDelegate = BuildInfoDelegate()
        buildInfoDelegate.onViewCreated(view.findViewById(R.id.app_build_info))


        osInfoDelegate = OsInfoDelegate()
        osInfoDelegate.onViewCreated(view.findViewById(R.id.app_os_info))

        networkDelegate = NetWorkInfoDelegate()
        networkDelegate.onViewCreated(view.findViewById(R.id.app_network_info))

    }


}

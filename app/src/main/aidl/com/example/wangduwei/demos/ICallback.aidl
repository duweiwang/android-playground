// ICallback.aidl
package com.example.wangduwei.demos;

// Declare any non-default types here with import statements
import com.example.wangduwei.demos.ipc.callback.IPCBean;
//回调接口
interface ICallback {
    oneway void callback(in IPCBean bean);
}

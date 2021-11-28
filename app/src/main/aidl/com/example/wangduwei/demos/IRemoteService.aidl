// IRemoteService.aidl
package com.example.wangduwei.demos;

// Declare any non-default types here with import statements
import com.example.wangduwei.demos.ICallback;
//通讯接口
interface IRemoteService {

    void registeCallback(ICallback callback);

    void unregisteCallBack(ICallback callback);

    void sleepAndCallback();

}

### Volley对Http304的处理

#### 1.从基本使用说起
#####    1.1 volley的基本使用

```java
mQueue = Volley.newRequestQueue(context)
```
    主要是 1.建立缓存目录 2.通过版本判断使用HttpURLConnection还是HttpClient,源码及注释如下
```java
     public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
     //缓存目录
            File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

            String userAgent = "volley/0";
            try {
                String packageName = context.getPackageName();
                PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
                userAgent = packageName + "/" + info.versionCode;
            } catch (NameNotFoundException e) {
            }
    //用哪种Http请求
            if (stack == null) {
                if (Build.VERSION.SDK_INT >= 9) {
                    stack = new HurlStack();
                } else {
                    // Prior to Gingerbread, HttpUrlConnection was unreliable.
                    // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                    stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
                }
            }
    //
            Network network = new BasicNetwork(stack);
    //这里初始化了4个网络线程：NetWorkDispatcher数组

            RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
    //start方法内部直接初始化一个CacheDispatcher，循环初始化四个NetWorkDispatcher
    //并调用线程的start方法开启这五个线程
            queue.start();

            return queue;
        }
```
 #####       1.2 CacheDispatcher线程

        （1）从缓存队列中提出一个请求对象：final Request<?> request = mCacheQueue.take();
        （2）判断是否有缓存（ttl）-->无，放进网络请求队列
        （3）判断是否过期-->是，放进网络请求队列
        （4）是否需要刷新-->是，放进网络请求队列
        （5）回调回去：mDelivery.postResponse(request, response);

 #####       1.3 NetWorkDispatcher线程

        （1）取出网络请求对象：request = mQueue.take();
        （2）执行请求：mNetwork.performRequest(request);
                请求会带上上一次请求的header信息（If-None-Match，If-Modified-Since）
                包含对请求超时等异常的处理和retry，如果返回304则更新本地缓存的entry
        （3）parseNetworkResponse 此为抽象方法，继承Request类实现该方法，做一些预处理
        （4）HttpHeaderParser.parseCacheHeaders
                解析头部信息，主要是http缓存相关，用于缓存过期判断，产生Cache.Entry对象
        （5）回调回去：mDelivery.postResponse(request, response);
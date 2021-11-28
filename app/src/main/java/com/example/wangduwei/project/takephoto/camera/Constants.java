package com.example.wangduwei.project.takephoto.camera;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/9
 */

public interface Constants {

    String TAG = "wdw-camera";

    AspectRatio DEFAULT_ASPECT_RATIO = AspectRatio.of(16, 9);

    int FACING_BACK = 0;
    int FACING_FRONT = 1;

    int FLASH_OFF = 0;
    int FLASH_ON = 1;
    int FLASH_TORCH = 2;
    int FLASH_AUTO = 3;
    int FLASH_RED_EYE = 4;

    int LANDSCAPE_90 = 90;
    int LANDSCAPE_270 = 270;
}

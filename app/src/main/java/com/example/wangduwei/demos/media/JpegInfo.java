package com.example.wangduwei.demos.media;

import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/1/8
 */

public class JpegInfo {


    public void jpegInfo(String path) throws IOException {
        ExifInterface exifInterface = new ExifInterface(path);

        String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
        String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
        String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
        String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
        String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        String imageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
        String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        String exposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
        String isoSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO);
        String dateTimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
        String subSecTime = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
        String subSecTimeOrig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIG);
        String subSecTimeDig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIG);
        String altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
        String altitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
        String gpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
        String gpsDateStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
        String whiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
        String focalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
        String processingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

        Log.e("TAG", "## orientation=" + orientation);
        Log.e("TAG", "## dateTime=" + dateTime);
        Log.e("TAG", "## make=" + make);
        Log.e("TAG", "## model=" + model);
        Log.e("TAG", "## flash=" + flash);
        Log.e("TAG", "## imageLength=" + imageLength);
        Log.e("TAG", "## imageWidth=" + imageWidth);
        Log.e("TAG", "## latitude=" + latitude);
        Log.e("TAG", "## longitude=" + longitude);
        Log.e("TAG", "## latitudeRef=" + latitudeRef);
        Log.e("TAG", "## longitudeRef=" + longitudeRef);
        Log.e("TAG", "## exposureTime=" + exposureTime);
        Log.e("TAG", "## aperture=" + aperture);
        Log.e("TAG", "## isoSpeedRatings=" + isoSpeedRatings);
        Log.e("TAG", "## dateTimeDigitized=" + dateTimeDigitized);
        Log.e("TAG", "## subSecTime=" + subSecTime);
        Log.e("TAG", "## subSecTimeOrig=" + subSecTimeOrig);
        Log.e("TAG", "## subSecTimeDig=" + subSecTimeDig);
        Log.e("TAG", "## altitude=" + altitude);
        Log.e("TAG", "## altitudeRef=" + altitudeRef);
        Log.e("TAG", "## gpsTimeStamp=" + gpsTimeStamp);
        Log.e("TAG", "## gpsDateStamp=" + gpsDateStamp);
        Log.e("TAG", "## whiteBalance=" + whiteBalance);
        Log.e("TAG", "## focalLength=" + focalLength);
        Log.e("TAG", "## processingMethod=" + processingMethod);

    }
}

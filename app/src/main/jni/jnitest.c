//
// Created by wangduwei on 2019/2/27.
//

#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "com_example_wangduwei_demos_jni_JniCenter.h"

JNIEXPORT jstring JNICALL Java_com_example_wangduwei_demos_jni_JniCenter_getStringFromC
        (JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, "Hellow World");
}

JNIEXPORT void JNICALL Java_com_example_wangduwei_demos_jni_JniCenter_changeArray
        (JNIEnv *env, jobject obj, jintArray arr) {

    //拿到整型数组的长度以及第0个元素的地址
    //jsize       (*GetArrayLength)(JNIEnv*, jarray);
    int length = (*env)->GetArrayLength(env, arr);
    // jint*       (*GetIntArrayElements)(JNIEnv*, jintArray, jboolean*);
    int *arrp = (*env)->GetIntArrayElements(env, arr, 0);
    int i;
    for (i = 0; i < length; i++) {
        *(arrp + i) += 10; //将数组中的每个元素加10
    }

}


JNIEXPORT jstring JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_getStringFromCObjectMethod(JNIEnv *env,
                                                                          jobject thiz) {
    return (*env)->NewStringUTF(env, "getStringFromCObjectMethod");
}


JNIEXPORT jint JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_sumArray(JNIEnv *env, jobject obj,
                                                        jintArray arr) {
    jint buf[10];
    jint i, sum = 0;
    (*env)->GetIntArrayRegion(env, arr, 0, 10, buf);
    for (int i =0; i < 10; i ++){
        sum += buf[i];
    }
    return sum;
}

JNIEXPORT jint JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_sumArray2(JNIEnv *env, jobject obj,
                                                         jintArray arr) {
    jint i, sum = 0;
    jint* carr = (*env)->GetIntArrayElements(env, arr, NULL);
    if(!carr) {
        return 0; /* exception occurred */
    }
    for (int i =0; i < 10; i ++){
        sum += carr[i];
    }
    (*env)->ReleaseIntArrayElements(env, arr, carr, 0);
    return sum;
}


JNIEXPORT jobjectArray JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_initArrayInNative(JNIEnv *env, jclass cls,
                                                                 jint size) {
    jobjectArray result;
    int i;
    jclass intArrCls = (*env)->FindClass(env, "[I");
    if(!intArrCls) {
        return NULL; /* exception thrown */
    }
    result = (*env)->NewObjectArray(env, size, intArrCls, NULL);
    if(!result) {
        return NULL; /* out of memory error thrown */
    }
    for (i = 0; i < size; ++i) {
        jint tmp[256]; /* make sure it is large enough! */
        int j;
        jintArray iarr = (*env)->NewIntArray(env, size);
        if(!iarr) {
            return NULL;
        }
        for (j = 0; j < size; j ++) {
            tmp[j] = i + j;
        }
        (*env)->SetIntArrayRegion(env, iarr, 0, size, tmp);
        (*env)->SetObjectArrayElement(env, result, i, iarr);
        (*env)->DeleteLocalRef(env, iarr);
    }
    return result;
}



JNIEXPORT void JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_accessJavaField(JNIEnv *env,
                                                               jobject obj) {
    jfieldID fid; /* store the field ID */
    jstring jstr;
    const char* str;
    /* Get a reference to obj's class */
    jclass cls = (*env)->GetObjectClass(env, obj);
    printf("In C: \n");

    /*Look for the instance field s in cls */
    fid = (*env)->GetFieldID(env, cls, "str", "Ljava/lang/String;");
    if(!fid) {
        return; /* failed to find the field ！*/
    }
    /* Read the instance field s */
    jstr = (*env)->GetObjectField(env, obj, fid);
    str = (*env)->GetStringUTFChars(env, jstr, NULL);
    if(!str) {
        return; /* out of memory */
    }
    printf("    c.s = \"%s\"\n", str);
    (*env)->ReleaseStringUTFChars(env, jstr, str);
    /* Create a new string and overwrite the instance field */
    jstr = (*env)->NewStringUTF(env, "123");
    if(!jstr) {
        return; /*out of memory */
    }
    (*env)->SetObjectField(env, obj, fid, jstr);
}

JNIEXPORT void JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_accessJavaField2(JNIEnv *env, jobject obj) {
    static jfieldID fid_s; /* store the field ID */
    jstring jstr;
    const char* str;
    /* Get a reference to obj's class */
    jclass cls = (*env)->GetObjectClass(env, obj);
    printf("In C: \n");

    /*Look for the instance field s in cls */
    if(!fid_s){
        fid_s = (*env)->GetFieldID(env, cls, "str", "Ljava/lang/String;");
        if(!fid_s) {
            return; /* failed to find the field ！*/
        }
    }
    /* Read the instance field s */
    jstr = (*env)->GetObjectField(env, obj, fid_s);
    str = (*env)->GetStringUTFChars(env, jstr, NULL);
    if(!str) {
        return; /* out of memory */
    }
    printf("    c.s = \"%s\"\n", str);
    (*env)->ReleaseStringUTFChars(env, jstr, str);
    /* Create a new string and overwrite the instance field */
    jstr = (*env)->NewStringUTF(env, "456");
    if(!jstr) {
        return; /*out of memory */
    }
    (*env)->SetObjectField(env, obj, fid_s, jstr);
}


JNIEXPORT void JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_accessJavaStatisticField(JNIEnv *env, jobject obj) {
    jfieldID fid; /* store the field ID */
    jint si;
    /* Get a reference to obj's class */
    jclass cls = (*env)->GetObjectClass(env, obj);
    printf("In C:\n");
    /* Look for the static field si in cls */
    fid = (*env)->GetStaticFieldID(env, cls, "sNum", "I");
    if(!fid) {
        return; /* field not found */
    }
    /* Access the static field si */
    si = (*env)->GetStaticIntField(env, cls, fid);
    printf("  StaticFieldAccess.si = %d\n", si);
    (*env)->SetStaticIntField(env, cls, fid, 200);
}

JNIEXPORT void JNICALL
Java_com_example_wangduwei_demos_jni_JniCenter_subscribeListener(JNIEnv
* env,
jobject thiz, jcallback
listener) {
    //todo

}















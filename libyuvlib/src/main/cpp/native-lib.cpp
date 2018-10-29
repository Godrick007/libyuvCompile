#include <cwchar>
#include <libyuv.h>
#include "jni.h"


#include <android/native_window.h>

using namespace libyuv;


JNIEXPORT void JNICALL
Java_com_gaosiedu_libyuv_Ndk_yuv420BuffToRgb32(JNIEnv *env, jobject instance, jbyteArray yBytes_,
                                               jint yStride, jbyteArray uBytes_, jint uStride,
                                               jbyteArray vBytes_, jint vStride) {

    jbyte *yBytes = env->GetByteArrayElements(yBytes_, NULL);
    jbyte *uBytes = env->GetByteArrayElements(uBytes_, NULL);
    jbyte *vBytes = env->GetByteArrayElements(vBytes_, NULL);

    // TODO
//    libyuv::NV21ToARGB();
//    libyuv::ARGBTO





    env->ReleaseByteArrayElements(yBytes_, yBytes, 0);
    env->ReleaseByteArrayElements(uBytes_, uBytes, 0);
    env->ReleaseByteArrayElements(vBytes_, vBytes, 0);
}
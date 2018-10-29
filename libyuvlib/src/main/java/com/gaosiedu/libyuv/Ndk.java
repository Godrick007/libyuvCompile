package com.gaosiedu.libyuv;

public class Ndk {

    static {
        System.loadLibrary("yuv");
        System.loadLibrary("yuv-rgb");
    }

    public native void yuv420BuffToRgb32(byte[] yBytes, int yStride, byte[] uBytes, int uStride, byte[] vBytes, int vStride);

}

package com.lovepoem.libyuv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gaosiedu.libyuv.Ndk;

import java.util.Arrays;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";

    private GLSurfaceView glSurfaceView;

    private Ndk ndk;

    private String[] cameraIdList;
    private String cameraId;
    private CameraCharacteristics cameraCharacteristics;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mSession;
    private CaptureRequest.Builder builder = null;
    private CaptureRequest captureRequest;

    private ImageReader imageReader;


    private CameraDevice.StateCallback cameraDevicesStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {

        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.flags |= SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_STABLE;

        getWindow().setAttributes(params);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_priview);
        glSurfaceView = findViewById(R.id.gl_surface_view);
        ndk = new Ndk();

        initCamera();

    }

    @SuppressLint("MissingPermission")
    private void initCamera() {

        imageReader = ImageReader.newInstance(1920, 1080, ImageFormat.YUV_420_888, 1);

        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {

            cameraIdList = cameraManager.getCameraIdList();

            for (String s : cameraIdList) {
                Log.i(TAG, s);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }


        cameraId = cameraIdList[0];

        try {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "get camera error", Toast.LENGTH_SHORT).show();
            return;
        }

        StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        Size sizes[] = map.getOutputSizes(ImageFormat.YUV_420_888);

        for (Size s : sizes) {
            Log.e(TAG, String.format("yuv420_888 's size width is %d and height is %d", s.getWidth(), s.getHeight()));
        }

        try {
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    Log.e(TAG, "camera opened");
                    mCameraDevice = camera;
                    cameraSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    Log.e(TAG, "camera disconnected");
                    camera.close();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    Log.e(TAG, "camera error, error code is" + error);
                }
            }, new Handler());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }

    private void cameraSession() {


        try {
            builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        builder.addTarget(imageReader.getSurface());

        try {
            mCameraDevice.createCaptureSession(Arrays.asList(glSurfaceView.getHolder().getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {

                    if (null == mCameraDevice) {
                        Log.e(TAG, "camera devices is null");
                        return;
                    }

                    mSession = session;

                    builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);

                    captureRequest = builder.build();

                    try {
                        session.setRepeatingRequest(captureRequest, new CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                                super.onCaptureProgressed(session, request, partialResult);

                            }
                        }, new Handler());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, new Handler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }


}

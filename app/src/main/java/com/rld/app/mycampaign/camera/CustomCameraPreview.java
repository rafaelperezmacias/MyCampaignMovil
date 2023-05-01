package com.rld.app.mycampaign.camera;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.databinding.ActivityNewCameraPreviewBinding;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.UUID;

public class CustomCameraPreview extends AppCompatActivity {

    public static final int RESULT_OK = 1000;
    public static final int RESULT_IMAGE_FILE_NOT_CREATE = 1001;
    public static final int RESULT_IMAGE_NOT_TAKEN = 1002;
    public static final int RESULT_ERROR = 1003;

    private static final SparseIntArray ORIENTATION;

    static {
        ORIENTATION = new SparseIntArray();
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    private static final String MODE_TAKE_PICTURE = "TAKE_PICTURE";
    private static final String MODE_TAKE_PICTURE_AGAIN = "TAKE_PICTURE_AGAIN";

    private ActivityNewCameraPreviewBinding binding;
    private TextureView textureView;
    private RelativeLayout lytImage;
    private GraphicOverlay graphicOverlay;
    private ImageView imageTaken;
    private ImageButton btnDone;
    private ImageButton btnBack;
    private View lytLoad;

    private String cameraID;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;

    private Handler backgroundHandler;
    private HandlerThread backgroundHandlerThread;

    private File currentImageFile;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewCameraPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FloatingActionButton btnTakePicture = binding.btnTakePicture;
        textureView = binding.textureView;
        graphicOverlay = binding.graphicOverLay;
        lytImage = binding.lytImage;
        imageTaken = binding.imageTaken;
        btnDone = binding.btnDone;
        btnBack = binding.btnBack;
        lytLoad = binding.lytLoad;

        textureView.setSurfaceTextureListener(textureListener);
        lytLoad.setVisibility(View.VISIBLE);
        lytImage.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);
        btnTakePicture.setVisibility(View.INVISIBLE);

        btnTakePicture.setTag(MODE_TAKE_PICTURE);
        btnTakePicture.setOnClickListener(v -> {
            if ( btnTakePicture.getTag().equals(MODE_TAKE_PICTURE) ) {
                takePicture();
                btnTakePicture.setTag(MODE_TAKE_PICTURE_AGAIN);
                btnTakePicture.setImageResource(R.drawable.round_refresh_24);
                return;
            }
            startBackgroundThread();
            if ( currentImageFile != null && currentImageFile.exists() ) {
                currentImageFile.delete();
                currentImageFile = null;
            }
            textureView.setVisibility(View.VISIBLE);
            graphicOverlay.setVisibility(View.VISIBLE);
            lytImage.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);
            btnTakePicture.setTag(MODE_TAKE_PICTURE);
            btnTakePicture.setImageResource(R.drawable.ic_menu_camera);
        });

        btnBack.setOnClickListener(v -> {
            if ( currentImageFile != null && currentImageFile.exists() ) {
                currentImageFile.delete();
            }
            setResult(RESULT_IMAGE_NOT_TAKEN);
            finish();
        });

        btnDone.setOnClickListener(v -> {
            ProgressDialogBuilder builder = new ProgressDialogBuilder()
                    .setTitle("Guardando imagen")
                    .setCancelable(false);
            progressDialog = new ProgressDialog(CustomCameraPreview.this, builder);
            progressDialog.show();
            Intent outputPath = new Intent();
            outputPath.putExtra("imagePath", currentImageFile.getAbsolutePath());
            setResult(RESULT_OK, outputPath);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if ( currentImageFile != null && currentImageFile.exists() ) {
            currentImageFile.delete();
        }
        setResult(RESULT_IMAGE_NOT_TAKEN);
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePicture() {
        if ( textureView.isAvailable() ) {
            String imageFileName = UUID.randomUUID().toString();
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                currentImageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(currentImageFile));
                Bitmap imageMap = textureView.getBitmap();
                imageMap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            } catch (IOException e) {
                Intent errorIntent = new Intent();
                errorIntent.putExtra("error", e.getMessage());
                setResult(RESULT_IMAGE_FILE_NOT_CREATE);
                finish();
            }
            stopBackgroundThread();
            imageTaken.setImageBitmap(textureView.getBitmap());
            lytImage.setVisibility(View.VISIBLE);
            textureView.setVisibility(View.GONE);
            graphicOverlay.setVisibility(View.GONE);
            btnDone.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();

        }
        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if ( cameraDevice == null ) {
                        return;
                    }
                    cameraCaptureSession = session;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, null);
        } catch (CameraAccessException e) {
            Intent errorIntent = new Intent();
            errorIntent.putExtra("error", "CreateCameraPreview: " + e.getMessage());
            setResult(RESULT_ERROR, errorIntent);
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updatePreview() {
        if ( cameraDevice == null ) {
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        }
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    BorderDetect.detectFromBitmap(textureView.getBitmap(), binding.graphicOverLay);
                    if ( lytLoad.getVisibility() == View.VISIBLE ) {
                        runOnUiThread(() -> {
                            lytLoad.setVisibility(View.GONE);
                            binding.btnTakePicture.setVisibility(View.VISIBLE);
                        });
                    }
                }
            }, backgroundHandler);
        } catch ( CameraAccessException e ) {
            Intent errorIntent = new Intent();
            errorIntent.putExtra("error", "UpdatePreview: " + e.getMessage());
            setResult(RESULT_ERROR, errorIntent);
            finish();
        }
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraID = manager.getCameraIdList()[0];
            CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imageDimension = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
            manager.openCamera(cameraID, stateCallback, null);
        } catch (CameraAccessException e) {
            Intent errorIntent = new Intent();
            errorIntent.putExtra("error", "OpenCamera: " + e.getMessage());
            setResult(RESULT_ERROR, errorIntent);
            finish();
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }
        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    private void startBackgroundThread() {
        backgroundHandlerThread = new HandlerThread("Camera background");
        backgroundHandlerThread.start();
        backgroundHandler = new Handler(backgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread() {
        backgroundHandlerThread.quitSafely();
        try {
            backgroundHandlerThread.join();
            backgroundHandlerThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            Intent errorIntent = new Intent();
            errorIntent.putExtra("error", "StopBackgroundThread: " + e.getMessage());
            setResult(RESULT_ERROR, errorIntent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( currentImageFile == null ) {
            startBackgroundThread();
        }
        if ( textureView.isAvailable() ) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( currentImageFile == null ) {
            stopBackgroundThread();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( progressDialog != null ) {
            progressDialog.dismiss();
        }
        if ( cameraDevice != null ) {
            cameraDevice.close();
        }
    }

}
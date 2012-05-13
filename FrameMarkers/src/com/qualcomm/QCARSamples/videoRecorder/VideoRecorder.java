package com.qualcomm.QCARSamples.videoRecorder;

import java.io.File;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;
import android.widget.VideoView;

import com.qualcomm.QCARSamples.FrameMarkers.DebugLog;
import com.qualcomm.QCARSamples.FrameMarkers.R;

public class VideoRecorder {

	public static final String EXTRA_USE_FRONT_FACING_CAMERA ="frontcamera";
	   private static final String OUTPUT_FILE = "/sdcard/videooutput";
	   private static final String TAG = "RecordVideo";
	   private Boolean mRecording = false;
	   private Boolean mUseFrontFacingCamera = false;
	   private VideoView mVideoView = null;
	   private MediaRecorder mVideoRecorder = null;
	   private Camera mCamera;
	   
	   
	   
	   public void stopRecording() throws Exception {
	       mRecording = false;
	       if (mVideoRecorder != null) {
	           mVideoRecorder.stop();
	           mVideoRecorder.release();
	           mVideoRecorder = null;
	       }
	       if (mCamera != null) {
	           mCamera.reconnect();
	           mCamera.stopPreview();
	           mCamera.release();
	           mCamera = null;
	       }
	   }
	
	/**
	    * Uses the surface defined in video_recorder.xml 
	    * Tested using 
	    * 2.2 (HTC Desire/Hero phone) -> Use all defaults works, records back facing camera with AMR_NB audio
	    * 3.0 (Motorola Xoom tablet) -> Use all defaults doesn't work, works with these specs, might work with others
	    * 
	    * @param holder The surfaceholder from the videoview of the layout
	    * @throws Exception
	    */
	   public void beginRecording() throws Exception {
	       if (mVideoRecorder != null) {
	           mVideoRecorder.stop();
	           mVideoRecorder.release();
	           mVideoRecorder = null;
	       }
	       if (mCamera != null) {
	           mCamera.reconnect();
	           mCamera.stopPreview();
	           mCamera.release();
	           mCamera = null;
	       }

	       File rootDcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	       String uniqueOutFile = OUTPUT_FILE + System.currentTimeMillis() + ".3gp";
	       File outFile = new File(rootDcim.getAbsolutePath() + "/test/" + uniqueOutFile);
//	       File outFile = new File(uniqueOutFile);
	       DebugLog.LOGI("created output file successfully = " + outFile.getAbsolutePath());
	       if (outFile.exists()) {
	           outFile.delete();
	       }

	       try {
	           mCamera = Camera.open();
	           

	           // Camera setup is based on the API Camera Preview demo
	           Camera.Parameters parameters = mCamera.getParameters();
	           parameters.setPreviewSize(640, 480);
	           mCamera.setParameters(parameters);
	           mCamera.startPreview();
	           mCamera.unlock();

	           mVideoRecorder = new MediaRecorder();
	           mVideoRecorder.setCamera(mCamera);

	           // Media recorder setup is based on Listing 9-6, Hashimi et all 2010
	           // values based on best practices and good quality, 
	           // tested via upload to YouTube and played in QuickTime on Mac Snow Leopard
	           mVideoRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	           mVideoRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	           mVideoRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// THREE_GPP
	                                                                          // MPEG_4 as another option
	           mVideoRecorder.setVideoSize(640, 480);// YouTube recommended size: 320x240,
	                                                                           // OpenGazer eye tracker: 640x480
	                                                                           // YouTube HD: 1280x720
	           mVideoRecorder.setVideoFrameRate(20); //might be auto-determined due to lighting
	           mVideoRecorder.setVideoEncodingBitRate(3000000);// 3 megapixel, or the max of
	                                                                                               // the camera
	           mVideoRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// MPEG_4_SP
	                                                                                                                               // Simple Profile is
	                                                                                                                               // for low bit
	                                                                                                                               // rate and low
	                                                                                                                               // resolution
	                                                                                                                               // H264 is MPEG-4 Part 10 
	                                                                                                                               //is commonly referred to
	                                                                                                                               // as H.264 or AVC
	           int sdk = android.os.Build.VERSION.SDK_INT;
	           // Gingerbread and up can have wide band ie 16,000 hz recordings 
	           // (Okay quality for human voice)
	           if (sdk >= 10) {
	               mVideoRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
	               mVideoRecorder.setAudioSamplingRate(16000);
	           } else {
	               // Other devices only have narrow band, ie 8,000 hz 
	               // (Same quality as a phone call, not really good quality for any purpose. 
	               // For human voice 8,000 hz means /f/ and /th/ are indistinguishable)
	               mVideoRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	           }
	           mVideoRecorder.setMaxDuration(30000); // limit to 30 seconds
	           mVideoRecorder.setOutputFile(uniqueOutFile);
	           mVideoRecorder.prepare();
	           mVideoRecorder.start();
	           mRecording = true;
	       } catch (Exception e) {
	           Log.e(TAG, e.toString());
	           e.printStackTrace();
	       }
	   }
}

package proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.qualcomm.QCARSamples.FrameMarkers.DebugLog;

public class ConnectionManager implements Runnable {
	private final String CHARSET = "UTF-8";

	private static String cameraId = "0";
	private static String serverAddress = "192.168.0.100:1280";
	private static Thread connectionThread = null;

	private static final Map<Integer, String> pendingUpdates = new HashMap<Integer, String>();

	private static final String TAG = ConnectionManager.class.getName();

	public ConnectionManager() {
		if (connectionThread == null) {
			Thread connectionThread = new Thread(this);
			connectionThread.setDaemon(true);
			connectionThread.start();
			new KeepAliveThread(this).start();
		}
	}

	public void sendKeepAlive() {
		DebugLog.LOGD("calling update connection!");
		try {
			// Create a new HttpClient and Post Header
			HttpClient client = new DefaultHttpClient();
			String url = "http://" + serverAddress + "/api/updateConnection/" + cameraId;
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			post.setHeader("Accept", "*/*");

			HttpResponse response = client.execute(post);
			DebugLog.LOGD("executed POST = " + url + " ## response = " + response.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			DebugLog.LOGD("ERROR! " + e.getMessage());
		}
	}

	private void updateServer(int markerId, float x, float y, float z, float poseMatrix0, float poseMatrix1,
			float poseMatrix2, float poseMatrix3, float poseMatrix4, float poseMatrix5, float poseMatrix6,
			float poseMatrix7, float poseMatrix8, float poseMatrix9, float poseMatrix10, float poseMatrix11) {

		// no marker detected
		if (markerId == -1) {
			return;
		}

		DebugLog.LOGD("calling update pawn!");

		String markerData = String.format(
				"marker=%d&x=%.4f&y=%.4f&z=%.4f&matrix=%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f",
				markerId, x, y, z, poseMatrix0, poseMatrix1, poseMatrix2, poseMatrix3, poseMatrix4, poseMatrix5,
				poseMatrix6, poseMatrix7, poseMatrix8, poseMatrix9, poseMatrix10, poseMatrix11);

		synchronized (pendingUpdates) {
			pendingUpdates.put(markerId, markerData);
			pendingUpdates.notify();
		}
	}

	private void sendUpdate(String markerInfo) {
		try {
			// Create a new HttpClient and Post Header
			HttpClient client = new DefaultHttpClient();

			String url = "http://" + serverAddress + "/api/cameras/" + cameraId;
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			post.setHeader("Accept", "*/*");

			post.setEntity(new StringEntity(markerInfo, CHARSET));
			HttpResponse response = client.execute(post);
			DebugLog.LOGD("executed POST = " + url + " ## data = " + markerInfo + " ## response = "
					+ response.toString());

		} catch (IOException e) {
			DebugLog.LOGE("ERROR! " + e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				String updateToApply = null;
				synchronized (pendingUpdates) {
					while (pendingUpdates.size() == 0) {
						pendingUpdates.wait(250);
					}
					Integer updateKey = pendingUpdates.keySet().iterator().next();
					updateToApply = pendingUpdates.get(updateKey);
					pendingUpdates.remove(updateKey);
				}
				sendUpdate(updateToApply);
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "Interrupted", e);
		}
	}

	public static String getCameraId() {
		return ConnectionManager.cameraId;
	}

	public static void setCameraId(String cameraId) {
		ConnectionManager.cameraId = cameraId;
	}

	public static String getServerAddress() {
		return ConnectionManager.serverAddress;
	}

	public static void setServerAddress(String serverAddress) {
		ConnectionManager.serverAddress = serverAddress;
	}

	public native void register();
}

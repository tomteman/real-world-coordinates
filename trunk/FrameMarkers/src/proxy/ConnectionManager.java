package proxy;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.qualcomm.QCARSamples.FrameMarkers.DebugLog;

public class ConnectionManager {
	private final String CHARSET = "UTF-8";
	
	private static String cameraId = "0";
	private static String serverAddress = "192.168.0.100:1280";
	
	public ConnectionManager() {

	}


	private void updateConnection()
	{
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
	
	private void updateServer(int markerId, float x, float y, float z,
			float poseMatrix0, float poseMatrix1, float poseMatrix2,
			float poseMatrix3, float poseMatrix4, float poseMatrix5,
			float poseMatrix6, float poseMatrix7, float poseMatrix8,
			float poseMatrix9, float poseMatrix10, float poseMatrix11) {

		// no marker detected
		if (markerId == -1)
		{
			updateConnection();
			return;
		}
		
		DebugLog.LOGD("calling update pawn!");
		try {
			// Create a new HttpClient and Post Header
			HttpClient client = new DefaultHttpClient();
			
			String url = "http://" + serverAddress + "/api/cameras/" + cameraId;
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			post.setHeader("Accept", "*/*");

			String message = String
					.format("marker=%d&x=%.4f&y=%.4f&z=%.4f&matrix=%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f",
							markerId, x, y, z, poseMatrix0, poseMatrix1,
							poseMatrix2, poseMatrix3, poseMatrix4, poseMatrix5,
							poseMatrix6, poseMatrix7, poseMatrix8, poseMatrix9,
							poseMatrix10, poseMatrix11);

			post.setEntity(new StringEntity(message, CHARSET));
			HttpResponse response = client.execute(post);
			DebugLog.LOGD("executed POST = " + url + message + " ## response = " + response.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			DebugLog.LOGD("ERROR! " + e.getMessage());
		}

	}

	public static String getCameraId()
	{
		return ConnectionManager.cameraId;
	}
	
	public static void setCameraId(String cameraId)
	{
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

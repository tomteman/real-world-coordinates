package proxy;

import java.io.IOException;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.qualcomm.QCARSamples.FrameMarkers.DebugLog;

public class ConnectionManager {
	String url = "http://www.google.com/";
	String charset = "UTF-8";
	URLConnection conn;

	public ConnectionManager() {

	}

	private void updateServer(int markerId, float x, float y, float z,
			float poseMatrix0, float poseMatrix1, float poseMatrix2,
			float poseMatrix3, float poseMatrix4, float poseMatrix5,
			float poseMatrix6, float poseMatrix7, float poseMatrix8,
			float poseMatrix9, float poseMatrix10, float poseMatrix11) {

		int cameraId = 0;
		DebugLog.LOGD("calling update pawn!");
		try {
			// Create a new HttpClient and Post Header
			HttpClient client = new DefaultHttpClient();
			String cameraIdString = Integer.toString(cameraId);
			HttpPost post = new HttpPost(
					"http://192.168.0.100:1280/api/cameras/" + cameraIdString);
			DebugLog.LOGD("created post!");
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			post.setHeader("Accept", "*/*");

			String message = String
					.format("marker=%d&x=%.4f&y=%.4f&z=%.4f&matrix=%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f",
							markerId, x, y, z, poseMatrix0, poseMatrix1,
							poseMatrix2, poseMatrix3, poseMatrix4, poseMatrix5,
							poseMatrix6, poseMatrix7, poseMatrix8, poseMatrix9,
							poseMatrix10, poseMatrix11);

			post.setEntity(new StringEntity(message, "UTF-8"));
			HttpResponse response = client.execute(post);
			DebugLog.LOGD("executed post! = "
					+ "http://192.168.0.100:8080/api/cameras/" + cameraIdString
					+ "/" + message + " ## response = " + response.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			DebugLog.LOGD("ERROR! " + e.getMessage());
		}

	}

	public native void register();

}

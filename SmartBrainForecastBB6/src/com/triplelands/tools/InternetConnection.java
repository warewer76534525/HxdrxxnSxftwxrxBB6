package com.triplelands.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.triplelands.datastore.DataStorer;
import com.triplelands.utils.HttpUtils;

public class InternetConnection {

	private InternetConnectionListener listener;
	private HttpConnection conn;
	private InputStream is;
	private OutputStream os;

	public InternetConnection(InternetConnectionListener listener) {
		this.listener = listener;
	}

	public void setAndAccessURL(String url) {
		url += HttpUtils.getBestConnectionSuffix();
		System.out.println("Akses: " + url);
		try {
			conn = (HttpConnection) Connector.open(url);
			DataStorer data = new DataStorer();
			System.out.println("Sending cookie: session: "
					+ data.getData("session") + ", Email: "
					+ data.getData("email"));
			conn.setRequestProperty("Cookie",
					"UA=hisoftBB; HSSession=" + data.getData("session") + "; HSEmail="
							+ data.getData("email"));
//			conn.setRequestProperty("User-Agent", "BlackBerry_Hisoft_"
//					+ DeviceInfo.getPlatformVersion());
			int responseCode = conn.getResponseCode();
			
			if (responseCode == HttpConnection.HTTP_NOT_FOUND) {
				System.out.println("not found");
				if (listener != null) {
					listener.onNotFound();
				}
			}
			if (responseCode != HttpConnection.HTTP_OK) {
				System.out.println("RESPONSE CODE IS NOT OK: " + responseCode);
				throw new IOException(
						"Koneksi gagal, silahkan cek koneksi internet anda.");
			}

			if (listener != null) {
				listener.onStartEvent(conn.getLength(), conn.getType());
			}

			is = conn.openInputStream();

			if (listener != null) {
				listener.onReceiveResponseEvent(is);
			}

			if (is != null) {
				is.close();
				is = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}

		} catch (IllegalArgumentException e) {
			if (listener != null) {
				System.out.println("Illegal Arg Error occured: " + e.getMessage());
				listener.onErrorOccurEvent(e);
			}
		} catch (ConnectionNotFoundException e) {
			if (listener != null) {
				System.out.println("Not found Error occured: " + e.getMessage());
				listener.onErrorOccurEvent(e);
			}
		} catch (InterruptedIOException e) {
			System.out.println("Interupted Error occured: " + e.getMessage());
			// do nothing...
		} catch (IOException e) {
			if (listener != null) {
				System.out.println("IO Error occured: " + e.getMessage());
				listener.onErrorOccurEvent(e);
			}
		} catch (Exception e) {
			if (listener != null) {
				System.out.println("Error occured: " + e.getMessage());
				listener.onErrorOccurEvent(e);
			}
		}
	}

	public void uploadParameter(String url, String parameter) {
		url += HttpUtils.getBestConnectionSuffix();
		System.out.println("Post: " + url);
		try {
			System.out.println("parameter : " + parameter);
			conn = (HttpConnection) Connector.open(url);
			conn.setRequestMethod(HttpConnection.POST);
			DataStorer data = new DataStorer();
			System.out.println("Sending cookie: session: "
					+ data.getData("session") + ", Email: "
					+ data.getData("email"));
			conn.setRequestProperty("Cookie",
					"UA=hisoftBB; HSSession=" + data.getData("session") + "; HSEmail="
							+ data.getData("email"));
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
//			conn.setRequestProperty("Content-Length",
//					Integer.toString(parameter.length()));
//			conn.setRequestProperty("User-Agent", "BlackBerry_Hisoft_"
//					+ DeviceInfo.getPlatformVersion());

			os = conn.openOutputStream();

			if (os != null) {
				os.write(parameter.getBytes());
			}

			if (os != null) {
				os.close();
				os = null;
			}

			if (conn.getResponseCode() == HttpConnection.HTTP_NOT_FOUND) {
				if (listener != null) {
					listener.onNotFound();
				}
			} else if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
				is = conn.openInputStream();
				if (listener != null) {
					listener.onReceiveResponseEvent(is);
				}
			} else {
				System.out.println("RESPONSE CODE IS NOT OK: "
						+ conn.getResponseCode());
				throw new IOException(
						"Koneksi gagal, silahkan cek koneksi internet anda.");
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("IO ERROR: " + ex.getMessage());
			if (listener != null) {
				listener.onErrorOccurEvent(ex);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception ERROR: " + ex.getMessage());
			if (listener != null) {
				listener.onErrorOccurEvent(ex);
			}
		}
	}

	public void cancel() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (os != null) {
				os.close();
				os = null;
			}
			if (listener != null) {
				listener.onCancelEvent();
			}
		} catch (IOException e) {
			if (listener != null) {
				listener.onErrorOccurEvent(e);
			}
		}
	}
}

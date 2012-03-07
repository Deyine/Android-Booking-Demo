package org.pullrequest.android.bookingnative.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.C;

import android.util.Base64;
import android.util.Log;

public class RequestService {

	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String CHARSET_ENCODING = "UTF-8";

	private static RequestService instance;

	private HttpClient client;
	private String email;

	/**
	 * 
	 * @return
	 */
	public static RequestService getInstance() {
		if (instance == null) {
			instance = new RequestService();
		}
		return instance;
	}

	private RequestService() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(httpParameters, CHARSET_ENCODING);
		HttpProtocolParams.setHttpElementCharset(httpParameters, CHARSET_ENCODING);

		client = new DefaultHttpClient(httpParameters);
	}

	public void close() {
		client = null;
		instance = null;
	}

	/**
	 * 
	 * @param uri
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Response get(URI uri) throws ClientProtocolException, IOException {
		String textResponse = null;

		HttpGet get = new HttpGet(uri);
		if (email != null) {
			get.addHeader("Authorization", buildAuthorizationToken());
		}
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpResponse response;
		response = executeRequest(get);

		// Checking response
		if (response != null) {
			InputStream in = response.getEntity().getContent();
			textResponse = convertStreamToString(in);
		}

		return new Response(response.getStatusLine().getStatusCode(), textResponse);
	}

	/**
	 * 
	 * @return
	 */
	private final String buildAuthorizationToken() {
		return "Basic " + Base64.encodeToString((email + ":sample").getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
	}

	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @param uri
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Response post(URI uri, JSONObject json) throws ClientProtocolException, IOException, JSONException {
		HttpPost post = new HttpPost(uri);
		if (C.DEVELOPER_MODE) {
			Log.d(C.LOG_TAG, uri + " /POST, json : " + json.toString());
		}
		StringEntity se = new StringEntity(json.toString(), CHARSET_ENCODING);
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE));
		post.setEntity(se);

		return postRequest(post);
	}

	/**
	 * 
	 * @param uri
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Response post(URI uri, JSONArray jsonArray) throws ClientProtocolException, IOException, JSONException {
		HttpPost post = new HttpPost(uri);
		if (C.DEVELOPER_MODE) {
			Log.d(C.LOG_TAG, uri + " /POST, json array : " + jsonArray.toString());
		}
		StringEntity se = new StringEntity(jsonArray.toString(), CHARSET_ENCODING);
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, JSON_CONTENT_TYPE));

		post.setEntity(se);

		return postRequest(post);
	}

	/**
	 * 
	 * @param uri
	 * @param parameters
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Response post(URI uri, Map<String, String> parameters) throws ClientProtocolException, IOException, JSONException {
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpPost postRequest = new HttpPost(uri);

		// parameters
		if (parameters != null) {
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			for (String parameterName : parameters.keySet()) {
				postParameters.add(new BasicNameValuePair(parameterName, parameters.get(parameterName)));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters, CHARSET_ENCODING);
			postRequest.setEntity(formEntity);
		}

		return postRequest(postRequest);
	}

	/**
	 * 
	 * @param httpPost
	 * @return Response
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	private Response postRequest(HttpPost httpPost) throws ClientProtocolException, IOException, JSONException {
		String textResponse = null;
		if (email != null) {
			httpPost.addHeader("Authorization", buildAuthorizationToken());
		}
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpResponse response;
		response = executeRequest(httpPost);

		// Checking response
		if (response != null) {
			InputStream in = response.getEntity().getContent();
			textResponse = convertStreamToString(in);
		}

		return new Response(response.getStatusLine().getStatusCode(), textResponse);
	}

	/**
	 * 
	 * @param request
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private synchronized HttpResponse executeRequest(HttpRequestBase request) throws ClientProtocolException, IOException {
		return client.execute(request);
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, CHARSET_ENCODING));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}
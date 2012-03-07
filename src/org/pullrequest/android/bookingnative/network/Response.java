package org.pullrequest.android.bookingnative.network;

public class Response {

	private int code;
	private String jsonData;

	public Response(int code) {
		this.code = code;
	}

	public Response(int code, String jsonData) {
		this.code = code;
		this.jsonData = jsonData;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	@Override
	public String toString() {
		return "HTTP Response " + this.code + " : " + this.jsonData;
	}
}
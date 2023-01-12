package response;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import webserver.HttpStatus;

public class HttpResponse {

	private HttpResponseLine line;
	private Map<String, String> header;
	private String responseBody;

	public HttpResponse() {
		this.line = null;
		this.header = null;
		this.responseBody = null;

	}

	public HttpResponse(final HttpStatus httpStatus, final String responseBody,
		final ContentType contentType, final String redirectUrl) {
		this.line = new HttpResponseLine(httpStatus);
		this.header = new HashMap<>();
		initHeader(contentType, responseBody, redirectUrl);
		this.responseBody = responseBody;
	}

	public HttpResponse(final HttpStatus httpStatus, final String responseBody, final ContentType contentType) {
		this.line = new HttpResponseLine(httpStatus);
		this.header = new HashMap<>();
		initHeader(contentType, responseBody);
		this.responseBody = responseBody;
	}

	private void initHeader(final ContentType contentType, final String responseBody) {
		header.put("Content-Type", contentType.getContentType() + ";charset-utf-8");
		header.put("Content-Length", String.valueOf(responseBody.getBytes(StandardCharsets.UTF_8).length));
	}

	private void initHeader(final ContentType contentType, final String responseBody, final String redirectUrl) {
		initHeader(contentType, responseBody);
		header.put("Location", redirectUrl);
	}

	public void setHttpResponse(HttpStatus httpStatus, String responseBody, ContentType contentType) {
		this.line = new HttpResponseLine(httpStatus);
		this.header = new HashMap<>();
		initHeader(contentType, responseBody);
		this.responseBody = responseBody;
	}

	public void setHttpResponse(HttpStatus httpStatus, String responseBody,
		ContentType contentType, String redirectUrl) {
		this.line = new HttpResponseLine(httpStatus);
		this.header = new HashMap<>();
		initHeader(contentType, responseBody, redirectUrl);
		this.responseBody = responseBody;
	}

	@Override
	public String toString() {
		return String.join("\r\n",
			line + " ",
			printHeader() + "\n",
			responseBody);
	}

	public String printHeader() {
		return header.entrySet().stream()
			.map(entry -> String.format("%s: %s ", entry.getKey(), entry.getValue()))
			.collect(Collectors.joining(System.lineSeparator()));
	}
}
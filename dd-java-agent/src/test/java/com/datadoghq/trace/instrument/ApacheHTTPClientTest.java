package com.datadoghq.trace.instrument;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;

import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;

public class ApacheHTTPClientTest {

	protected MockTracer tracer = new MockTracer();

	@Before
	public void beforeTest() throws Exception {
		GlobalTracer.register(tracer);
	}
	
	@Test
	public void test() throws Exception{
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("http://apache.org");

		// add request header
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : "
		                + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result);
		
		assertThat(tracer.finishedSpans().size()).isEqualTo(2);
	}

}
package test.com.inzent.ixeb.manager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IxebPostRequest implements AutoCloseable {
	private static Logger logger = Logger.getLogger( IxebPostRequest.class );
	
	private OkHttpClient				client = null;
	
	{
		client = new OkHttpClient.Builder()
							.connectTimeout( 0, TimeUnit.SECONDS )
							.readTimeout( 0,  TimeUnit.SECONDS )
							.build();
	}

	public ResponseBody request( String url, String webOrders ) {
		FormBody.Builder builder = new FormBody.Builder();
		builder.addEncoded( "ixeb_request", webOrders );
		
		FormBody requestBody = builder.build();
		
		Request.Builder requestBuilder = new Request.Builder();
		Request request = requestBuilder
                .url( url )
                .addHeader( "content-type", "application/x-www-form-urlencoded;charset=UTF-8" )
                .addHeader( "accept", "text/plain, */*; q=0.01")
                .addHeader( "accept-language", "ko-KR" )
                .post( requestBody )
                .build();
		
	    try {
//	    	client.set
			Response response = client.newCall( request ).execute();
			if( null != response.body() ) {
				return response.body();//.string();
			}
		} catch ( IOException e ) {
			logger.error( e.getMessage(), e );
		} catch ( Exception e ) {
			logger.error( e.getMessage(), e );
		}
		return null;
	}

	@Override
	public void close() throws Exception {
		client = null;
	}
	
}

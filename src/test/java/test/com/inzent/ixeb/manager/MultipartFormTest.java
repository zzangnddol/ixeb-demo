package test.com.inzent.ixeb.manager;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.inzent.ixeb.IxebException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MultipartFormTest {
	private static Logger logger = Logger.getLogger( MultipartFormTest.class );
	private OkHttpClient				client = null;
	private String							baseUrl = "http://localhost:8080/ixeb-manager/";


	private String	makeUrl( String uri ) {
		return baseUrl + uri;
	}

	private ResponseBody multipartRequest( String url, IxebProtocolGenerator protocol ) {
		System.out.println( "ixeb_request : <<" + protocol.stringify() + ">>");

		MultipartBody.Builder builder = new MultipartBody.Builder();
		builder.setType( MultipartBody.FORM );
//		builder.addPart(part)
		/**
		 * 
		builder.addPart(
			Headers.of("Content-Disposition", "form-data; name=\"image\""),
					RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
		 */
		builder.addFormDataPart( "ixeb_request", protocol.stringify() );
		
		RequestBody requestBody = builder.build();
		Request request = new Request.Builder()
                .url( url )
                .method( "POST", requestBody )
                .build();
	
	    try {
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
	
	@Before
	public void setUp() throws Exception {
		client = new OkHttpClient();
	}

	@After
	public void tearDown() throws Exception {
		client = null;
	}
	

	@Test
	public void normalTest() {
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "tbl_cctv2", "OUTPUT", null, null );

			ResponseBody responseBody = multipartRequest( makeUrl( "service.ixeb" ), protocol );
			if( null == responseBody ) {
				fail( "responseBody is null." );
				return;
			}
			
			try {
				logger.debug( responseBody.string() );
			} catch (IOException e) {
				fail( e.getMessage() );
			}
		} catch (IxebException e) {
			fail( e.getMessage() );
		}
	}
	
}

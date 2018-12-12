package test.com.inzent.ixeb.manager;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.inzent.ixeb.IxebException;
import com.inzent.ixeb.io.Bundle;

import okhttp3.ResponseBody;

public class StoredProcedureTest {
	private static Logger logger = Logger.getLogger( StoredProcedureTest.class );

	private String							baseUrl = "http://localhost:8080/ixeb-manager/";
	private IxebPostRequest		requester = null;

	private String	makeUrl( String uri ) {
		return baseUrl + uri;
	}
	
	@Before
	public void setUp() throws Exception {
		requester = new IxebPostRequest();
	}

	@After
	public void tearDown() throws Exception {
		requester.close();
		requester = null;
	}
	
	//@Test
	public void normalTest() {
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			{
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put( "yyyfield", "1234" );
				protocol.newBundle( "service_service04", "OUTPUT", params, null );
			}
			
			{
				Bundle newBundle = protocol.newBundle( "service_service04", "INPUT", null, null );
				{
					Map<String, Object> row = new HashMap<String, Object>();
					row.put( "_STATUS_", "U" );
					row.put( "studentid", 6 );
					row.put( "email", "taisuck22222222@gmail.com" );
					protocol.addBundleRow( newBundle, row);	
				}

				{
					Map<String, Object> row = new HashMap<String, Object>();
					row.put( "_STATUS_", "C" );
					//row.put( "studentid", 5 );
					row.put( "first_name", "Kim111" );
					row.put( "last_name", "lastKim111" );
					row.put( "email", "taisuck11111@gmail.com" );
					protocol.addBundleRow( newBundle, row);
				}
				
				{
					Map<String, Object> row = new HashMap<String, Object>();
					row.put( "_STATUS_", "D" );
					row.put( "studentid", 4 );
					protocol.addBundleRow( newBundle, row);
				}
			}

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
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
	
	
	@Test
	public void normalUserTest() {
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			{
				HashMap<String, Object> params = new HashMap<String, Object>();
				//params.put( "yyyfield", "1234" );
				protocol.newBundle( "SVR_USER", "OUTPUT", params, null );
			}
			
			//**
			{
				Bundle newBundle = protocol.newBundle( "SVR_USER", "INPUT", null, null );
				

				{
					Map<String, Object> row = new HashMap<String, Object>();
					row.put( "_STATUS_", "C" );
					//#{id}, #{name}, #{email}, #{mobile}
					row.put( "ID", "taisuck" );
					row.put( "name", "David" );
					row.put( "email", "taisuck11111@gmail.com" );
					row.put( "mobile", "01025684506" );
					protocol.addBundleRow( newBundle, row);
				}
				
//				{
//					Map<String, Object> row = new HashMap<String, Object>();
//					row.put( "_STATUS_", "U" );
//					row.put( "studentid", 6 );
//					row.put( "email", "taisuck22222222@gmail.com" );
//					protocol.addBundleRow( newBundle, row);	
//				}
//
//				{
//					Map<String, Object> row = new HashMap<String, Object>();
//					row.put( "_STATUS_", "C" );
//					//row.put( "studentid", 5 );
//					row.put( "first_name", "Kim111" );
//					row.put( "last_name", "lastKim111" );
//					row.put( "email", "taisuck11111@gmail.com" );
//					protocol.addBundleRow( newBundle, row);
//				}
//				
//				{
//					Map<String, Object> row = new HashMap<String, Object>();
//					row.put( "_STATUS_", "D" );
//					row.put( "studentid", 4 );
//					protocol.addBundleRow( newBundle, row);
//				}
			}
			//**/

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
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

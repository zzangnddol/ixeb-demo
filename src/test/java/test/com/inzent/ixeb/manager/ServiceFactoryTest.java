package test.com.inzent.ixeb.manager;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.inzent.ixeb.IxebException;
import com.inzent.ixeb.io.Bundle;

import okhttp3.ResponseBody;

public class ServiceFactoryTest {
	private static Logger logger = Logger.getLogger( ServiceFactoryTest.class );

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
	
	@Ignore("")
	@Test
	public void normalTest() {
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "tbl_cctv2", "OUTPUT", null, null );

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
	
	@Ignore("")
	@Test
	public void normalInputTest() {
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "tbl_cctv2", "INPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
					//webRequest( url, protocol );
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

	@Ignore("")
	@Test
	public void normalMixTest() {
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "tbl_cctv2", "INPUT", null, null );
			protocol.newBundle( "tbl_cctv2", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
			//webRequest( url, protocol );
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

	@Ignore
	@Test
	public void NormalTest() {
		logger.debug( "public void NormalTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "tbl_cctv2", "INPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
			//webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void NormalTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	@Ignore
	@Test
	public void AsyncTest() {
		logger.debug( "public void AsyncTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "tbl_cctv2", "INPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//			webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void AsyncTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	@Ignore
	@Test
	public void notExistServiceNormalTest() {
		logger.debug( "public void notExistServiceNormalTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "INPUT", null, null );
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	@Ignore
	@Test
	public void notExistServiceAsyncTest() {
		logger.debug( "public void notExistServiceAsyncTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "INPUT", null, null );
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	//*
	
	/*
	 * 1-1. 서비스 input/output( 등록된 service호출 )	- normar
	 * notExistServiceNormalInputOutputTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalInputOutputTest() {
		logger.debug( "public void notExistServiceNormalInputOutputTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "INPUT", null, null );
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
					//webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalInputOutputTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 1-2. 서비스 input/output( 등록된 service호출 )	- slice
	 * notExistServiceAsyncInputOutputTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncInputOutputTest() {
		logger.debug( "public void notExistServiceAsyncInputOutputTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "INPUT", null, null );
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncInputOutputTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 2-1. 서비스 input/output( 없는 service호출 )	- normar
	 * notExistServiceNormalInputAndOutputNotServiceTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalInputAndOutputNotServiceTest() {
		logger.debug( "public void notExistServiceNormalInputAndOutputNotServiceTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service044", "INPUT", null, null );
			protocol.newBundle( "service_service044", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalInputAndOutputNotServiceTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 2-2. 서비스 input/output( 없는 service호출 )	- slice
	 * notExistServiceAsyncInputAndOutputNotServiceTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncInputAndOutputNotServiceTest() {
		logger.debug( "public void notExistServiceAsyncInputAndOutputNotServiceTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service044", "INPUT", null, null );
			protocol.newBundle( "service_service044", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncInputAndOutputNotServiceTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 3-1. 서비스 input/output( input은 등록된 service호출, output은 없는 service호출 )	- normal
	 * notExistServiceNormalInputOrOutputNotServiceTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalInputOrOutputNotServiceTest() {
		logger.debug( "public void notExistServiceNormalInputOrOutputNotServiceTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "INPUT", null, null );
			protocol.newBundle( "service_service044", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalInputOrOutputNotServiceTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 3-2. 서비스 input/output( input은 등록된 service호출, output은 없는 service호출 )	- slice
	 * notExistServiceAsyncInputOrOutputNotServiceTest()
	 * ExistsServiceIsInputModeAndNotExistsServiceIsOutputModeAsyncTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncInputOrOutputNotServiceTest() {
		logger.debug( "public void notExistServiceAsyncInputOrOutputNotServiceTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "INPUT", null, null );
			protocol.newBundle( "service_service044", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncInputOrOutputNotServiceTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 4-1. 서비스 input/output( input은 없는 service호출, output은 등로된 service있음 )	- normal
	 * notExistServiceNormalInputNotServiceOrOutputTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalInputNotServiceOrOutputTest() {
		logger.debug( "public void notExistServiceNormalInputNotServiceOrOutputTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service044", "INPUT", null, null );
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalInputNotServiceOrOutputTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 4-2. 서비스 input/output( input은 없는 service호출, output은 등로된 service있음 )	- slice
	 * notExistServiceAsyncInputNotServiceOrOutputTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncInputNotServiceOrOutputTest() {
		logger.debug( "public void notExistServiceAsyncInputNotServiceOrOutputTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service044", "INPUT", null, null );
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncInputNotServiceOrOutputTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 5-1. ioType( ioType이 없음 )	- normal
	 * notExistServiceNormalNotIoTypeTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalNotIoTypeTest() {
		logger.debug( "public void notExistServiceNormalNotIoTypeTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalNotIoTypeTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 5-2. ioType( ioType이 없음 )	- slice
	 * notExistServiceAsyncNotIoTypeTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncNotIoTypeTest() {
		logger.debug( "public void notExistServiceAsyncNotIoTypeTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "service_service04", "", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncNotIoTypeTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 6-1. 서비스 input( 없는 serviceID호출 )	 - normal
	 * notExistServiceNormalInputNotServiceIDTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalInputNotServiceIDTest() {
		logger.debug( "public void notExistServiceNormalInputNotServiceIDTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "INPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalInputNotServiceIDTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 6-2. 서비스 input( 없는 serviceID호출 )	 - slice
	 * notExistServiceAsyncInputNotServiceIDTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncInputNotServiceIDTest() {
		logger.debug( "public void notExistServiceAsyncInputNotServiceIDTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "INPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncInputNotServiceIDTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 7-1. 서비스 output( 없는 serviceID호출 )	- normal
	 * notExistServiceNormalOutputNotServiceIDTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalOutputNotServiceIDTest() {
		logger.debug( "public void notExistServiceNormalOutputNotServiceIDTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalOutputNotServiceIDTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 7-2. 서비스 output( 없는 serviceID호출 )	- slice
	 * notExistServiceAsyncOutputNotServiceIDTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncOutputNotServiceIDTest() {
		logger.debug( "public void notExistServiceAsyncOutputNotServiceIDTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncOutputNotServiceIDTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 8-1. 서비스 in/put( serviceID가 없음 )	 - normal
	 * notExistServiceNormalInputAndOutputNotServiceIDTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalInputAndOutputNotServiceIDTest() {
		logger.debug( "public void notExistServiceNormalInputAndOutputNotServiceIDTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "INPUT", null, null );
			protocol.newBundle( "", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalInputAndOutputNotServiceIDTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 8-2. 서비스 in/put( serviceID가 없음 )	 - slice
	 * notExistServiceAsyncInputAndOutputNotServiceIDTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncInputAndOutputNotServiceIDTest() {
		logger.debug( "public void notExistServiceAsyncInputAndOutputNotServiceIDTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "INPUT", null, null );
			protocol.newBundle( "", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncInputAndOutputNotServiceIDTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 9-1. 서비스 타입없음( serviceID와 ioType이 없음 )	 - normal
	 * notExistServiceNormalNotServiceIDNotioTypeTest()
	 */
	@Ignore
	@Test
	public void notExistServiceNormalNotServiceIDNotioTypeTest() {
		logger.debug( "public void notExistServiceNormalNotServiceIDNotioTypeTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalNotServiceIDNotioTypeTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	/*
	 * 9-2. 서비스 타입없음( serviceID와 ioType이 없음 )	 - slice
	 * notExistServiceAsyncNotServiceIDNotioTypeTest()
	 */
	@Ignore
	@Test
	public void notExistServiceAsyncNotServiceIDNotioTypeTest() {
		logger.debug( "public void notExistServiceAsyncNotServiceIDNotioTypeTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			protocol.newBundle( "", "", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.slice.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceAsyncNotServiceIDNotioTypeTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 10-1. Delete Data	 - normal
	 * DB Table에 없는 데이터를 삭제
	 * 
	 */
	@Ignore
	@Test
	public void notExistServiceNormalDeleteDataTest() {
		logger.debug( "public void notExistServiceNormalDeleteDataTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			//protocol.newBundle( "service_service04", "INPUT", null, null );
			Bundle newBundle = protocol.newBundle( "service_service04", "INPUT", null, null );
			Map<String, Object> row = new HashMap<String, Object>();
			row.put( "_STATUS_", "D" );
			row.put( "ID", "a" );
			protocol.addBundleRow(newBundle, row);
			
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalDeleteDataTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	
	/*
	 * 11-1. Insert Data(한글)	 - normal
	 * 
	 */
//	@Ignore
	@Test
	public void notExistServiceNormalInsertDataTest() {
		logger.debug( "public void notExistServiceNormalInsertDataTest()");
		logger.debug( "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		IxebProtocolGenerator protocol = new IxebProtocolGenerator();
		try {
			//protocol.newBundle( "service_service04", "INPUT", null, null );
			Bundle newBundle = protocol.newBundle( "service_service04", "INPUT", null, null );
			Map<String, Object> row = new HashMap<String, Object>();
			row.put( "_STATUS_", "C" );
			try {
				row.put( "ID", new String( "가".getBytes(), "utf-8" ) );
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				logger.error( e1.getMessage(), e1);
			}
			row.put( "ORG", "나" );
			row.put( "NAME", "다" );
			row.put( "TEL", "라" );
			row.put( "NOTE", "마" );
			protocol.addBundleRow(newBundle, row);
			
			protocol.newBundle( "service_service04", "OUTPUT", null, null );

			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );
//					webRequest( makeUrl( "service.ixeb"), protocol );
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
		} finally {
			logger.debug( "public void notExistServiceNormalInsertDataTest()");
			logger.debug( ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}
	//*/

}

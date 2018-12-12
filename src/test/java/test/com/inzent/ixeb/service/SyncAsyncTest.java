package test.com.inzent.ixeb.service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.inzent.ixeb.IxebException;
import com.inzent.ixeb.io.Bundle;

import okhttp3.ResponseBody;
import test.com.inzent.ixeb.manager.IxebPostRequest;
import test.com.inzent.ixeb.manager.IxebProtocolGenerator;
import test.com.inzent.ixeb.manager.rpc.MappedQuery;
import test.com.inzent.ixeb.manager.rpc.MethodBuilder;
import test.com.inzent.ixeb.manager.rpc.Return;

public class SyncAsyncTest {
	private static Logger	logger = Logger.getLogger( SyncAsyncTest.class );
	private static String		baseUrl = "http://localhost:8080/ixeb-manager/";
	
	private static String		queryService = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		queryService = "SVR_QUERY";

		try ( IxebPostRequest 	requester = new IxebPostRequest() ) {
			ResponseBody responseBody = requester.request( makeUrl( "service.manager.ixeb" ), MethodBuilder.serviceCreate( queryService, "tbl_Students") );

			String ret = responseBody.string();
			logger.debug( "ret - create : " + ret );
			Return returnValue = new Return( ret );
			JSONObject root = ( JSONObject ) returnValue.getReturnObject();
			logger.debug( "stringify : " + root.toJSONString() );
			
			JSONArray queries = ( JSONArray ) root.get( "queries" );
			logger.debug( "size : " + queries.size() );
			List<MappedQuery> mappedQueries = new Vector<MappedQuery>();
			for( int i = 0 ; i < queries.size() ; i++ ) {
				JSONObject statement = ( JSONObject ) queries.get( i );
				String query = (String) statement.get( "query" );
				String type = (String) statement.get( "type" );
				String mapperId = (String) statement.get( "mapperId" );
				String statementType = (String) statement.get( "statementType" );
				
				if( type.equalsIgnoreCase( "UPDATE") ) {
					query = "UPDATE tbl_Students SET Firstname =  #{Firstname},  Lastname =  #{Lastname},  Email =  #{Email} "
							+ " WHERE Studentid = #{Studentid}";
				} else if( type.equalsIgnoreCase( "DELETE") ) {
					query = "DELETE FROM tbl_Students WHERE Studentid = #{Studentid}";
				} else if( type.equalsIgnoreCase( "INSERT") ) {
					query = "INSERT INTO tbl_Students( Firstname, Lastname, Email ) VALUES ( #{Firstname}, #{Lastname}, #{Email} )";
				}
				
				MappedQuery mappedQuery = new MappedQuery(mapperId, statementType, type, "java.util.HashMap", query);
				mappedQueries.add( mappedQuery );
			}
			responseBody = requester.request( makeUrl( "service.manager.ixeb" ),
					MethodBuilder.serviceUpdate( queryService, "dbms", queryService, mappedQueries) );
			ret = responseBody.string();
			
			responseBody = requester.request( makeUrl( "service.manager.ixeb"), MethodBuilder.serviceQuery( queryService ) );
			ret = responseBody.string();
			logger.debug( "ret - updated : " + ret );
		} catch (IOException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// 등록된 서비스 삭제
		try ( IxebPostRequest requester = new IxebPostRequest() ) {
			ResponseBody responseBody = requester.request( makeUrl( "service.manager.ixeb"), MethodBuilder.serviceDelete( queryService ) );
			
			String ret = responseBody.string();
			logger.debug( "ret - delete : " + ret );
			Return returnValue = new Return( ret );
			logger.debug( "returnValue.getReturnObject() : " + returnValue.getReturnObject().getClass().getName() );
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} finally {
			queryService = null;
		}
	}
	
	
//	@Before
//	public void setUp() throws Exception {
////	@BeforeClass
////	public static void setUpBeforeClass() throws Exception {
//		// test할 일반 쿼리서비스, SP 서비스를 등록한다.
//		queryService = "SVR_QUERY";
////		SPService = "SVR_SP";
//		
//		try ( IxebPostRequest 	requester = new IxebPostRequest() ) {
//			ResponseBody responseBody = requester.request( makeUrl( "service.manager.ixeb" ), MethodBuilder.serviceCreate( queryService, "tbl_Students") );
//
//			String ret = responseBody.string();
//			logger.debug( "ret - create : " + ret );
//			Return returnValue = new Return( ret );
//			JSONObject root = ( JSONObject ) returnValue.getReturnObject();
//			logger.debug( "stringify : " + root.toJSONString() );
//			
//			JSONArray queries = ( JSONArray ) root.get( "queries" );
//			logger.debug( "size : " + queries.size() );
//			List<MappedQuery> mappedQueries = new Vector<MappedQuery>();
//			for( int i = 0 ; i < queries.size() ; i++ ) {
//				JSONObject statement = ( JSONObject ) queries.get( i );
//				String query = (String) statement.get( "query" );
//				String type = (String) statement.get( "type" );
//				String mapperId = (String) statement.get( "mapperId" );
//				String statementType = (String) statement.get( "statementType" );
//				
//				if( type.equalsIgnoreCase( "UPDATE") ) {
//					query = "UPDATE tbl_Students SET Firstname =  #{Firstname},  Lastname =  #{Lastname},  Email =  #{Email} "
//							+ " WHERE Studentid = #{Studentid}";
//				} else if( type.equalsIgnoreCase( "DELETE") ) {
//					query = "DELETE FROM tbl_Students WHERE Studentid = #{Studentid}";
//				} else if( type.equalsIgnoreCase( "INSERT") ) {
//					query = "INSERT INTO tbl_Students( Firstname, Lastname, Email ) VALUES ( #{Firstname}, #{Lastname}, #{Email} )";
//				}
//				
//				MappedQuery mappedQuery = new MappedQuery(mapperId, statementType, type, "java.util.HashMap", query);
//				mappedQueries.add( mappedQuery );
//			}
//			responseBody = requester.request( makeUrl( "service.manager.ixeb" ),
//					MethodBuilder.serviceUpdate( queryService, "dbms", queryService, mappedQueries) );
//			ret = responseBody.string();
//			
//			responseBody = requester.request( makeUrl( "service.manager.ixeb"), MethodBuilder.serviceQuery( queryService ) );
//			ret = responseBody.string();
//			logger.debug( "ret - updated : " + ret );
//		} catch (IOException e) {
//			logger.error( e.getMessage(), e );
//			fail( e.getMessage() );
//		} catch( Exception e ) {
//			logger.error( e.getMessage(), e );
//			fail( e.getMessage() );
//		}
//	}

//	@After
//	public void tearDown() throws Exception {
//		// 등록된 서비스 삭제
//		try ( IxebPostRequest requester = new IxebPostRequest() ) {
//			ResponseBody responseBody = requester.request( makeUrl( "service.manager.ixeb"), MethodBuilder.serviceDelete( queryService ) );
//			
//			String ret = responseBody.string();
//			logger.debug( "ret - delete : " + ret );
//			Return returnValue = new Return( ret );
//			logger.debug( "returnValue.getReturnObject() : " + returnValue.getReturnObject().getClass().getName() );
//		} catch( IOException e ) {
//			logger.error( e.getMessage(), e );
//			fail( e.getMessage() );
//		} catch( Exception e ) {
//			logger.error( e.getMessage(), e );
//			fail( e.getMessage() );
//		}
//	}

	private static String makeUrl( String uri ) {
		return baseUrl + uri;
	}
	
	@Test
	public void NormalSync() {
		// 쿼리로 작성된 서비스를 sync 하게 다중 요청하여 정상적인 결과가 나오는지를 확인한다.
		logger.debug( "============ public void NormalSync() ==========" );
		try ( IxebPostRequest requester = new IxebPostRequest() ) {
			IxebProtocolGenerator protocol = new IxebProtocolGenerator();

			Bundle bundle = protocol.newBundle( queryService, "INPUT", null, null );
			for( int i = 0 ; i < 10 ; i++ ) {
				HashMap<String, String> row = new HashMap<String, String>();
				row.put( "_STATUS_", "C" );
				row.put( "Firstname", "Firstname#" + i );
				row.put( "Lastname", "Lastname#" + i );
				row.put( "Email", "Email#" + i );
				protocol.addBundleRow( bundle, row );
			}
			
			protocol.newBundle( queryService, "OUTPUT", null, null );
			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );

			if( null == responseBody ) {
				fail( "responseBody is null." );
				return;
			}

			String ret = responseBody.string();
			Return returnValue = new Return( ret );
			if( returnValue.hasError() ) {
				fail( ret );
			} else {
				logger.debug( "sync response : [" + ret + "]" );
				if( !validateJson( ret ) ) {
					fail( "if( !validateJson( ret ) )" );
					return;
				}
			}
		} catch (IxebException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}  catch (IOException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}
	}
	
	
	@Test
	public void NormalAsync() {
		logger.debug( "============ public void NormalAsync() ==========" );
		try ( IxebPostRequest requester = new IxebPostRequest() ) {
			IxebProtocolGenerator protocol = new IxebProtocolGenerator();

			Bundle bundle = protocol.newBundle( queryService, "INPUT", null, null );
			for( int i = 0 ; i < 10 ; i++ ) {
				HashMap<String, String> row = new HashMap<String, String>();
				row.put( "_STATUS_", "C" );
				row.put( "Firstname", "Firstname#" + i );
				row.put( "Lastname", "Lastname#" + i );
				row.put( "Email", "Email#" + i );
				protocol.addBundleRow( bundle, row );
			}
			
			protocol.newBundle( queryService, "OUTPUT", null, null );
			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );

			if( null == responseBody ) {
				fail( "responseBody is null." );
				return;
			}

			String ret = responseBody.string();
			Return returnValue = new Return( ret );
			if( returnValue.hasError() ) {
				fail( ret );
				return;
			} else {
				logger.debug( "async response : [" + ret + "]" );
				if( !validateJson( ret ) ) {
					fail( "if( !validateJson( ret ) )" );
					return;
				}
			}
		} catch (IxebException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}  catch (IOException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}
	}
	
	private boolean validateJson( String s ) {
		try {
			JSONParser parser = new JSONParser();
			parser.parse( s );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			return false;
		}
		return true;
	}
	
	@Test
	public void NoServiceSync() {
		logger.debug( "============ public void NoServiceSync() ==========" );
		String service = "TEST";
		try ( IxebPostRequest requester = new IxebPostRequest() ) {
			IxebProtocolGenerator protocol = new IxebProtocolGenerator();

			Bundle bundle = protocol.newBundle( service, "INPUT", null, null );
			for( int i = 0 ; i < 10 ; i++ ) {
				HashMap<String, String> row = new HashMap<String, String>();
				row.put( "_STATUS_", "C" );
				row.put( "Firstname", "Firstname#" + i );
				row.put( "Lastname", "Lastname#" + i );
				row.put( "Email", "Email#" + i );
				protocol.addBundleRow( bundle, row );
			}
			
			protocol.newBundle( service, "OUTPUT", null, null );
			ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );

			if( null == responseBody ) {
				fail( "responseBody is null." );
				return;
			}

			String ret = responseBody.string();
			Return returnValue = new Return( ret );
			if( returnValue.hasError() ) {
				fail( ret );
			} else {
				logger.debug( "sync response : [" + ret + "]" );
				if( !validateJson( ret ) ) {
					fail( "if( !validateJson( ret ) )" );
					return;
				}
			}
		} catch (IxebException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}  catch (IOException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}
	}
	
	@Test
	public void NoServiceAsync() {
		logger.debug( "============ public void NoServiceAsync() ==========" );
		String service = "TEST";
		try ( IxebPostRequest requester = new IxebPostRequest() ) {
			IxebProtocolGenerator protocol = new IxebProtocolGenerator();

			Bundle bundle = protocol.newBundle( service, "INPUT", null, null );
			for( int i = 0 ; i < 10 ; i++ ) {
				HashMap<String, String> row = new HashMap<String, String>();
				row.put( "_STATUS_", "C" );
				row.put( "Firstname", "Firstname#" + i );
				row.put( "Lastname", "Lastname#" + i );
				row.put( "Email", "Email#" + i );
				protocol.addBundleRow( bundle, row );
			}
			
			protocol.newBundle( service, "OUTPUT", null, null );
			ResponseBody responseBody = requester.request( makeUrl( "service.slice.ixeb"), protocol.stringify() );

			if( null == responseBody ) {
				fail( "responseBody is null." );
				return;
			}

			String ret = responseBody.string();
			Return returnValue = new Return( ret );
			if( returnValue.hasError() ) {
				fail( ret );
				return;
			} else {
				logger.debug( "async response : [" + ret + "]" );
				if( !validateJson( ret ) ) {
					fail( "if( !validateJson( ret ) )" );
					return;
				}
			}
		} catch (IxebException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}  catch (IOException e) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		}
	}
	
}

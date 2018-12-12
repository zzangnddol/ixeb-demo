package test.com.inzent.ixeb.service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

public class DataTest {
	private static Logger	logger = Logger.getLogger( DataTest.class );
	private static String		baseUrl = "http://localhost:8080/ixeb-manager/";
	private static String		serviceName = "SVR_EMPT";

	private static String makeUrl( String uri ) {
		return baseUrl + uri;
	}
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		try ( IxebPostRequest 	requester = new IxebPostRequest() ) {
			ResponseBody responseBody = requester.request(
																			makeUrl( "service.manager.ixeb" ),
																			MethodBuilder.serviceCreate( serviceName, "TBL_EMP") );

			String ret = responseBody.string();
//			logger.debug( "ret - create : " + ret );
			Return returnValue = new Return( ret );
			JSONObject root = ( JSONObject ) returnValue.getReturnObject();
//			logger.debug( "stringify : " + root.toJSONString() );
			
			JSONArray queries = ( JSONArray ) root.get( "queries" );
//			logger.debug( "size : " + queries.size() );
			List<MappedQuery> mappedQueries = new Vector<MappedQuery>();
			for( int i = 0 ; i < queries.size() ; i++ ) {
				JSONObject statement = ( JSONObject ) queries.get( i );
				String query = (String) statement.get( "query" );
				String type = (String) statement.get( "type" );
				String mapperId = (String) statement.get( "mapperId" );
				String statementType = (String) statement.get( "statementType" );
				
				if( type.equalsIgnoreCase( "UPDATE") ) {
					query = "UPDATE TBL_EMP SET NAME =  #{NAME} "
							+ " WHERE EMAIL = #{EMAIL}";
				} else if( type.equalsIgnoreCase( "DELETE") ) {
					query = "DELETE FROM TBL_EMP WHERE EMAIL = #{EMAIL}";
				} else if( type.equalsIgnoreCase( "INSERT") ) {
					query = "BEGIN\n"
							+ "	INSERT INTO TBL_EMP( NAME, EMAIL ) VALUES ( #{NAME}, #{EMAIL} );\n"
							+ "	INSERT INTO TBL_EMP( NAME, EMAIL ) VALUES ( #{NAME}, #{EMAIL} );\n"
							+ "END;\n";
					/**
					 * BEGIN
INSERT INTO CLOB_TEST(  COLUMN1,  COLUMN2 ) VALUES (  #{COLUMN1},  #{COLUMN2} ) ;
INSERT INTO CLOB_TEST(  COLUMN1,  COLUMN2 ) VALUES (  #{COLUMN1},  #{COLUMN2} ) ;
END;
					 */
				}
				
				MappedQuery mappedQuery = new MappedQuery(mapperId, statementType, type, "java.util.HashMap", query);
				mappedQueries.add( mappedQuery );
			}
			responseBody = requester.request( makeUrl( "service.manager.ixeb" ),
																MethodBuilder.serviceUpdate( serviceName, "dbms", serviceName, mappedQueries) );
			ret = responseBody.string();
			
			responseBody = requester.request( makeUrl( "service.manager.ixeb"), MethodBuilder.serviceQuery( serviceName ) );
			ret = responseBody.string();
//			logger.debug( "ret - updated : " + ret );
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
			ResponseBody responseBody = requester.request( makeUrl( "service.manager.ixeb"), MethodBuilder.serviceDelete( serviceName ) );
			
			String ret = responseBody.string();
//			logger.debug( "ret - delete : " + ret );
			Return returnValue = new Return( ret );
			Object object = returnValue.getReturnObject();
			if( null != object ) {
				logger.debug( "returnValue.getReturnObject() : " + object.getClass().getName() );
			}
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} catch( Exception e ) {
			logger.error( e.getMessage(), e );
			fail( e.getMessage() );
		} finally {
			serviceName = null;
		}
	}
	
	
	private List<HashMap<String, String>> rows = new Vector<HashMap<String, String>> ();
	
	{

//		HashMap<String, String> rowFirst = new HashMap<String, String>();
//		rowFirst.put( "NAME", null );
//		rowFirst.put( "EMAIL", null );
//		rows.add( rowFirst );
		
		for( int i = 0 ; i < 10 ; i++ ) {
			HashMap<String, String> row = new HashMap<String, String>();
			row.put( "NAME", "name#" + i );
			row.put( "EMAIL", "email#" + i );
			rows.add( row );
		}
	}

	@Test
	public void testEMPTYROW() {
		//fail("Not yet implemented");

		try ( IxebPostRequest requester = new IxebPostRequest() ) {
			IxebProtocolGenerator protocol = new IxebProtocolGenerator();

			{

				Bundle bundle = protocol.newBundle( serviceName, "INPUT", null, null );
				for( int i = 0 ; i < rows.size() ; i++ ) {
					HashMap<String, String> row = (HashMap<String, String>) rows.get( i );
					row.put( "_STATUS_", "C" );
					
					row.put( "NAME", row.get( "NAME") );
					row.put( "EMAIL", row.get( "EMAIL") );
					protocol.addBundleRow( bundle, row );
				}
				
				protocol.newBundle( serviceName, "OUTPUT", null, null );
				ResponseBody responseBody = requester.request( makeUrl( "service.ixeb"), protocol.stringify() );

				if( null == responseBody ) {
					fail( "responseBody is null." );
					return;
				}

				String ret = responseBody.string();
//				logger.debug( "ret [" + ret + "]");
				Return returnValue = new Return( ret );
				if( returnValue.hasError() ) {
					fail( ret );
				} else {
					logger.debug( "sync response : [" + ret + "]" );
				}
			}
			
			//**
			{
				Bundle bundle = protocol.newBundle( serviceName, "INPUT", null, null );
				for( int i = 0 ; i < rows.size() ; i++ ) {
					HashMap<String, String> row = (HashMap<String, String>) rows.get( i );
					row.put( "_STATUS_", "D" );
//					row.put( "NAME", row.get( "NAME") );
					row.put( "EMAIL", row.get( "EMAIL") );
					protocol.addBundleRow( bundle, row );
				}
				
				protocol.newBundle( serviceName, "OUTPUT", null, null );
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
				}
			}
			//**/
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

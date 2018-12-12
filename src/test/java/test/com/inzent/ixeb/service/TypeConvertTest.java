package test.com.inzent.ixeb.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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

public class TypeConvertTest {
	private static Logger	logger = Logger.getLogger( TypeConvertTest.class );
	private static String		baseUrl = "http://localhost:8080/ixeb-manager/";
	private static String		serviceName = "SVR_CLOB_TEST";

	private static String makeUrl( String uri ) {
		return baseUrl + uri;
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		try ( IxebPostRequest 	requester = new IxebPostRequest() ) {
			ResponseBody responseBody = requester.request(
																			makeUrl( "service.manager.ixeb" ),
																			MethodBuilder.serviceCreate( serviceName, "CLOB_TEST") );
			String ret = responseBody.string();
			Return returnValue = new Return( ret );
			JSONObject root = ( JSONObject ) returnValue.getReturnObject();
			
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
					query = "UPDATE CLOB_TEST SET COLUMN1 = #{COLUMN1}  "
							+ " WHERE COLUMN2  = #{COLUMN2}";
				} else if( type.equalsIgnoreCase( "DELETE") ) {
					query = "DELETE FROM CLOB_TEST WHERE COLUMN2 = #{COLUMN2}";
				} else if( type.equalsIgnoreCase( "INSERT") ) {
					query = "INSERT INTO CLOB_TEST( COLUMN1, COLUMN2 ) VALUES ( #{COLUMN1}, #{COLUMN2} )";
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
			Return returnValue = new Return( ret );
			logger.debug( "returnValue.getReturnObject() : " + returnValue.getReturnObject().getClass().getName() );
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

		HashMap<String, String> rowFirst = new HashMap<String, String>();
		rowFirst.put( "COLUMN1", null );
		rowFirst.put( "COLUMN2", null );
		rows.add( rowFirst );
		
		for( int i = 0 ; i < 10 ; i++ ) {
			HashMap<String, String> row = new HashMap<String, String>();
			row.put( "COLUMN1", "COLUMN1#" + i );
			row.put( "COLUMN2", "COLUMN2#" + i );
			rows.add( row );
		}
	}
	
	

	@Test
	public void testClobTypeConvert() {
		try ( IxebPostRequest requester = new IxebPostRequest() ) {
			IxebProtocolGenerator protocol = new IxebProtocolGenerator();

			{

				Bundle bundle = protocol.newBundle( serviceName, "INPUT", null, null );
				for( int i = 0 ; i < rows.size() ; i++ ) {
					HashMap<String, String> row = (HashMap<String, String>) rows.get( i );
					row.put( "_STATUS_", "C" );
					
					row.put( "COLUMN1", row.get( "COLUMN1") );
					row.put( "COLUMN2", row.get( "COLUMN2") );
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
					row.put( "COLUMN2", row.get( "COLUMN2") );
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

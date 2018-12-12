package test.com.inzent.ixeb.manager;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.ResponseBody;
import test.com.inzent.ixeb.manager.rpc.IxebMapper;
import test.com.inzent.ixeb.manager.rpc.MappedQuery;
import test.com.inzent.ixeb.manager.rpc.MethodBuilder;
import test.com.inzent.ixeb.manager.rpc.Return;
import test.com.inzent.ixeb.manager.rpc.ReturnUtils;


public class ServiceBuilderTest {
  private static Logger logger = Logger.getLogger( ServiceBuilderTest.class );

	private String							baseUrl = "http://localhost:8080/ixeb-manager/service.manager.ixeb";
	private IxebPostRequest		requester = null;
	
	private List<String>				excludeNamespaces = new Vector<String>();
	
	private List<String>				tableNames = new Vector<String>();
	
	private List<String>				newServices = new Vector<String>();
	

	@Before
	public void setUp() throws Exception {
		requester = new IxebPostRequest();
		
		{
//			tableNames.add( "TBL_SERVICE04" );
			tableNames.add( "TBL_USER" );
			tableNames.add( "TBL_CCTV3" );
			tableNames.add( "TBH_USER" );
			tableNames.add( "TBH_BOARD_LIST" );
			tableNames.add( "TBL_ACCESS_LOG" );
			tableNames.add( "COM_ADMIN" );
			tableNames.add( "COM_ADMIN_GROUP_FUNCTION" );
			tableNames.add( "COM_ADMIN_GROUP_MENU" );
			tableNames.add( "COM_ADMIN_LIBRARY" );
			tableNames.add( "COM_ADMIN_LOG" );
			tableNames.add( "COM_AGENT" );
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		ResponseBody responseBody = requester.request( baseUrl, MethodBuilder.serviceQuery( ) );
		try {
			JSONParser responseParser = new JSONParser();
			JSONObject responseFactor = (JSONObject) responseParser.parse( responseBody.string() );
//			logger.debug( "parser : " + responseFactor.toJSONString() );
			JSONObject result = (JSONObject) responseFactor.get( "result" );
			JSONArray mappers = (JSONArray) result.get( "mappers" );
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iter = mappers.iterator();
			while( iter.hasNext() ) {
				JSONObject mapper = iter.next();
				String namespace = (String) mapper.get( "namespace" );
				excludeNamespaces.add( namespace );
			}
			
		} catch (IOException e) {
			logger.error( e.getMessage(), e );
		} catch (ParseException e) {
			logger.error( e.getMessage(), e );
		} finally {
//			logger.debug( "<<<<<<<<<<<< serviceListTest - END >>>>>>>>>>>>");
		}
	}

	@After
	public void tearDown() throws Exception {
		requester.close();
		requester = null;
	}
	
	private boolean existService( String serviceId ) {
		Iterator<String> iter = excludeNamespaces.iterator();
		while( iter.hasNext() ) {
			String id = iter.next();
			if( id.equals( serviceId ) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isNewService( String s ) {
		Iterator<String> iter = newServices.iterator();
		while( iter.hasNext() ) {
			String id = iter.next();
			if( id.equals( s )) {
				return true;
			}
		}
		return false;
	}
	
	private Return serviceCreate( String tableName ) {
		String serviceId = "SVR_" + tableName;
		Return returnValue = null;
		ResponseBody responseBody = requester.request( baseUrl, MethodBuilder.serviceCreate( serviceId, tableName) );
		try {
			returnValue = new Return( responseBody.string() );
		} catch (IOException e) {
			logger.error( e.getMessage(), e );
		}
		newServices.add( serviceId );
		
		return returnValue;
	}
	
	private Return serviceList() {
		ResponseBody responseBody = requester.request( baseUrl, MethodBuilder.serviceQuery() );
		try {
			return new Return( responseBody.string() );
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
		}
		return null;
	}
	
	private Return serviceFields( String serviceId ) {
		ResponseBody responseBody = requester.request( baseUrl, MethodBuilder.serviceFields(serviceId) );
		try {
			return new Return( responseBody.string() );
		} catch (IOException e) {
			logger.error( e.getMessage(), e );
		}
		return null;
	}
	
	private Return serviceRemove( String serviceId ) {
		ResponseBody responseBody = requester.request( baseUrl, MethodBuilder.serviceDelete( serviceId ) );
		try {
			return new Return( responseBody.string() );
		} catch (IOException e) {
			logger.error( e.getMessage(), e );
		}
		return null;
	}
	
	@Test
	public void serviceAddDeleteTest() {
		//**
		{	// 서비스 추가
			Iterator<String> iter = tableNames.iterator();
			while( iter.hasNext() ) {
				String id = iter.next();
				if( existService( id ) ) {
					logger.debug( id + " service is exists.");
					continue;
				}
				logger.debug(  id );
				Return returnVal = serviceCreate( id );
				if( !returnVal.hasError() ) {
					JSONObject o = (JSONObject) returnVal.getReturnObject();
					@SuppressWarnings("unused")
					List<MappedQuery> mappedQueries = ReturnUtils.asMappedQueries( (JSONArray) o.get( "queries") );
				}
				break;
			}
		}
		//**/
		
		{	// 서비스 목록중에 추가된 서비스 확인
			Return returnValue = serviceList();
			JSONObject o = (JSONObject) returnValue.getReturnObject();
			logger.debug( o.toJSONString() );
			List<IxebMapper> mappers = ReturnUtils.asMappers( o.get( "mappers") );
			Iterator<IxebMapper> iter = mappers.iterator();
			while( iter.hasNext() ) {
				IxebMapper mapper = iter.next();
				String namespace = mapper.getNamespace();
				if( isNewService( namespace ) ) {
					logger.debug( namespace + " is new Service.");
					
					returnValue = serviceFields( namespace );
					o = (JSONObject) returnValue.getReturnObject();
					List<String> fields = ReturnUtils.asFields( ( (JSONObject) o ).get( "fields") );
					Iterator<String> field_iter = fields.iterator();
					while( field_iter.hasNext() ) {
						logger.debug( "field : " + field_iter.next() );
					}
				}
			}
			//**/
		}
		
		{	// 추가된 서비스 삭제
			Iterator<String> iter = newServices.iterator();
			while( iter.hasNext() ) {
				String serviceId = iter.next();
				logger.debug( "serviceId : " + serviceId + " was removed." );
				Return returnVal = serviceRemove( serviceId );
				JSONObject o = (JSONObject) returnVal.getReturnObject();
				logger.debug( "return value : " + o.toJSONString() );
			}
		}
	}
  

}

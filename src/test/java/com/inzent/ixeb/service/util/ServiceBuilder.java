package com.inzent.ixeb.service.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import com.inzent.ixeb.IxebException;
import com.inzent.ixeb.io.IxebObject;
import com.inzent.ixeb.io.IxebObjectType;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceBuilder {
	private static Logger logger = Logger.getLogger( ServiceBuilder.class );
	
	private OkHttpClient				connection = null;
	private String url = "http://localhost:8080/ixeb-manager/service.manager.ixeb";
	
	public ServiceBuilder() {
		connection = new OkHttpClient.Builder()
										.connectTimeout( 0, TimeUnit.SECONDS )
										.readTimeout( 0,  TimeUnit.SECONDS )
										.build();
	}
	
	public ServiceBuilder( String u ) {
		connection = new OkHttpClient.Builder()
										.connectTimeout( 0, TimeUnit.SECONDS )
										.readTimeout( 0,  TimeUnit.SECONDS )
										.build();
		url = u;
	}
	
	public ServiceBuilder( OkHttpClient c ) {
		connection = c;
	}
	
	public ServiceBuilder( OkHttpClient c, String u ) {
		connection = c;
		url = u;
	}
	
	public void setUrl( String u ) {
		url = u;
	}
	
	private Response request( String order ) throws IOException {
		FormBody.Builder builder = new FormBody.Builder();
		builder.addEncoded( "ixeb_request", order );
		
		FormBody requestBody = builder.build();

		Request.Builder requestBuilder = new Request.Builder();
		Request request = requestBuilder
                .url( url )
                .addHeader( "content-type", "application/x-www-form-urlencoded;charset=UTF-8" )
                .addHeader( "accept", "text/plain, */*; q=0.01")
                .addHeader( "accept-language", "ko-KR" )
                .post( requestBody )
                .build();
		
		return connection.newCall( request ).execute();
	}

	public boolean make( String tblName, String serviceId, String c, String r, String u, String d ) {
		try {
			WebRequest request = new PostMethodWebRequest( url );
			IxebObject req = new IxebObject( IxebObjectType.MAP );
			IxebObject params = new IxebObject( IxebObjectType.MAP );
			params.add( "serviceId", serviceId );    // Create id - {"namespace" : "tbl_mybatis"}
			params.add( "tableName", tblName );   // RDB Table Name
			
			req.add( "method", "service.create");
			req.add( "params", params );
			request.setParameter( "ixeb_request", req.toJSONString() );
			Response response = request( req.toJSONString() );
//			WebResponse response = connection.getResponse( request );
//			logger.debug( response.getText() );
			return update( serviceId, c, r, u, d );
		} catch( IxebException e ) {
	        Assert.fail( e.getMessage() );
	        return false;
	    } catch (IOException e) {
	    	Assert.fail( e.getMessage() );
	        return false;
	    } finally {
	    	
	    }
	}
	
	public boolean update( String serviceId, String c, String r, String u, String d ) {
		try {
			WebRequest request = new PostMethodWebRequest( url );

			IxebObject req = new IxebObject( IxebObjectType.MAP );
			IxebObject params = new IxebObject( IxebObjectType.MAP );
			params.add( "serviceId", serviceId );
		      
			String  strContent = "{\"storeType\" : \"dbms\", \"namespace\" :\"" + serviceId + "\", "
					+ " \"queries\" : ["
					+ "{\"parameterType\" : \"java.util.HashMap\", \"mapperId\" : \"select\", \"type\" : \"select\", \"resultType\" : \"java.util.HashMap\", \"query\" : \"" + r + "\"}, "
					+ "{\"parameterType\" : \"java.util.HashMap\", \"mapperId\" : \"insert\", \"type\" : \"insert\", \"query\" : \"" + c + "\"}, "
					+ "{\"parameterType\" : \"java.util.HashMap\", \"mapperId\" : \"update\", \"type\" : \"update\", \"query\" : \"" + u + "\"}, "
					+ "{\"parameterType\" : \"java.util.HashMap\", \"mapperId\" : \"delete\", \"type\" : \"delete\", \"query\" : \"" + d + "\"}]}";
			IxebObject mapperObject = new IxebObject(  );
			JSONParser parser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) parser.parse( strContent );
		        mapperObject.set(jsonObject);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			params.add( "mapper", mapperObject );
			
			req.add( "method", "service.update");
			req.add( "params", params );
			System.out.println("req.toJSONString() : " +req.toJSONString());
//			request.setParameter( "ixeb_request", req.toJSONString() );
			
			Response response = request( req.toJSONString() );

//			WebResponse response = connection.getResponse( request );
//			logger.debug( response.getText() );
		} catch( IxebException e ) {
	        Assert.fail( e.getMessage() );
	        return false;
	    }  catch (IOException e) {
	    	Assert.fail( e.getMessage() );
	        return false;
	    } finally {
		      
		}
		return true;
	}
	
	public boolean delete( String serviceId ) {
		try {
			WebRequest request = new PostMethodWebRequest( url );
			IxebObject req = new IxebObject( IxebObjectType.MAP );
			IxebObject params = new IxebObject( IxebObjectType.MAP );
			params.add( "serviceId", serviceId );

			req.add( "method", "service.delete");
			req.add( "params", params );
			request.setParameter( "ixeb_request", req.toJSONString() );

			Response response = request( req.toJSONString() );
			
//			WebResponse response = connection.getResponse( request );
//			logger.debug( response.getText() );
		} catch( IxebException e ) {
	        Assert.fail( e.getMessage() );
	        return false;
	    }  catch (IOException e) {
	    	Assert.fail( e.getMessage() );
	        return false;
	    } finally {
			
		}
		return true;
	}
	
}

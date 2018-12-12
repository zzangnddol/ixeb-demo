package com.inzent.ixeb.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestWorker implements Runnable {
	private static Logger logger = Logger.getLogger( RequestWorker.class );
	private OkHttpClient	connection = null;
	private Response		response = null;
	private String			requestUrl = "http://localhost:8080/ixeb-manager/service.slice.ixeb";
	private String			bodyString;
	private String			orderString;
	
	private List<HttpHeaderCover>		covers = new Vector<HttpHeaderCover>();
	private Map<String, String>			header = new HashMap<String, String>();
	
	private boolean		successful = false;
	private boolean		crossOrigin = false;

	public static class DefaultHeader implements HttpHeaderCover {

		@Override
		public void increseHeader(Map<String, String> map) {
			map.put( "host", "localhost:8080" );
			map.put( "connection", "keep-alive" );
			map.put( "origin", "http://localhost:9999" );
			map.put( "access-control-request-headers", "x-requested-with" );
			map.put( "accept", "*/*" );
			map.put( "referer", "http://localhost:9999/test1.html" );
			map.put( "accept-encoding", "gzip, deflate, br" );
			map.put( "accept-language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4" );
		}
	}
	
	public RequestWorker() {
		connection = new OkHttpClient.Builder().connectTimeout( 0, TimeUnit.SECONDS )
																	.readTimeout( 0,  TimeUnit.SECONDS )
																	.build();
	}
	
	public RequestWorker( OkHttpClient c ) {
		connection = c;
	}
	
	public RequestWorker setRequestUrl( String url ) {
		requestUrl = url;
		return this;
	}
	
	public RequestWorker addHeader( String key, String value ) {
		header.put( key, value );
		return this;
	}
	
	public RequestWorker addHeaderCover( HttpHeaderCover cover ) {
		covers.add( cover );
		return this;
	}

	private Response request( String order ) {
		FormBody.Builder builder = new FormBody.Builder();
		FormBody requestBody = builder.build();

		Set<String> keys = header.keySet();
		Request.Builder requestBuilder = new Request.Builder();
		if( crossOrigin ) {
			requestBuilder.url( requestUrl );
			if( covers.size() > 0 ) {
				for( int i = 0 ; i < covers.size() ; i++ ) {
					HttpHeaderCover cover = covers.get( i );
					cover.increseHeader(header);
				}
			}
			
			for( String key : keys ) {
				requestBuilder.addHeader( key, header.get( key ) );
			}
			Request optionRequest = requestBuilder.addHeader( "access-control-request-method", "POST" )
																		.method( "OPTIONS", requestBody ).build();
			
			Response optionResponse = null;
			try {
				optionResponse = connection.newCall( optionRequest ).execute();
				if( !optionResponse.isSuccessful() ) {
					logger.info( "if( !optionResponse.isSuccessful() )" );
					return null;
				}
			} catch (IOException e) {
				logger.info( e.getMessage(), e );
				return null;
			}
			
			Headers headers = optionResponse.headers();
			Set<String> names = headers.names();
			Iterator<String> iter = names.iterator();
			boolean checked = false;
			while( iter.hasNext() ) {
				String name = iter.next();
				String value = headers.get( name );
				if( name.equals( "Access-Control-Allow-Methods") && value.equals( "POST" ) ) {
					checked = true;
					break;
				}
			}
			
			if( !checked ) {
				Headers headers1 = optionResponse.headers();
				Set<String> names1 = headers1.names();
				Iterator<String> iter1 = names1.iterator();
				while( iter1.hasNext() ) {
					String name = iter1.next();
					String value = headers1.get( name );
					logger.info( "name : " + name + ", value : " + value );
				}
				return null;
			}
		}
		
		builder.add( "ixeb_request", order );
		requestBody = builder.build();
		requestBuilder.url( requestUrl );

		if( covers.size() > 0 ) {
			for( int i = 0 ; i < covers.size() ; i++ ) {
				HttpHeaderCover cover = covers.get( i );
				cover.increseHeader(header);
			}
		}
		
		for( String key : keys ) {
			requestBuilder.addHeader( key, header.get( key ) );
		}
		Request request = requestBuilder.post( requestBody ).build();
		try {
			return connection.newCall( request ).execute();
		} catch (IOException e) {
			logger.info( e.getMessage(), e );
			return null;
		}
	}
	
	public RequestWorker allowCrossOrigin( boolean allow ) {
		crossOrigin = allow;
		return this;
	}
	
	public RequestWorker setOrder( String o ) {
		orderString = o;
		return this;
	}
	
	public RequestWorker setOrder( WebOrder order ) {
		orderString = order.stringify();
		return this;
	}
	
	@Override
	public void run() {
		successful = false;
//		String order = "{\"params\":{},\"datasets\":[{\"io\":\"INPUT\",\"name\":\"sqlite_tbl_cctv\",\"params\":{},\"data\":[]},{\"io\":\"OUTPUT\",\"name\":\"sqlite_tbl_cctv\",\"params\":{},\"data\":[]}]}";
		response = request( orderString );
		if( null != response && response.isSuccessful() ) {
			successful = true;
			InputStream inputStream = response.body().byteStream();
			byte[] buffer = new byte[ 1024 ];
			long length = 0, total = 0;
			StringBuffer appender = new StringBuffer();
			try {
				while( (length = inputStream.read( buffer ) ) != -1 ) {
					total += length;
					if( total > 1024 ) {
						break;
					}
					appender.append( new String( buffer, "UTF-8" ) );
				}
			} catch (IOException e) {
				logger.error( e.getMessage(), e );
			}
			
			bodyString = appender.toString();
		} else {
			try {
				if( null != response ) {
					logger.info( response.body().string() );
				} else {
					logger.info( "response is null. ( Maybe server is too slow. ) " );
					successful = true;
				}
			} catch (IOException e) {
				logger.error( e.getMessage(), e );
			}
		}		
	}

	public boolean isSuccessful() {
		return successful;
	}
	
	public String contents() {
		return bodyString;
	}
	
	public void close() {
		if( null != response ) {
			response.close();
		}
	}
}

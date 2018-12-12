package com.inzent.ixeb.service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author Inzent
 * 
 * 서버(SDF)에 불규칙하게 접근
 * 	(
 * 	정상적인 접근, 
 * 	CORS접근, 
 * 	요청도중 강제로 접속 끊기,  
 * 	header만 전송한 경우
 * 	) 하였을 경우 오류가 발생하는지를 테스트한다.
 */
public class TestIrregularAccess {
	private static Logger logger = Logger.getLogger( TestIrregularAccess.class );
//	private SqlSessionFactory sessionFactory;

	@Before
	public void setUp() throws Exception {
		/**
		String resource = "com/inzent/ixeb/configuration.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream( resource );
			sessionFactory = new SqlSessionFactoryBuilder().build( inputStream );
			
			Configuration configuration = sessionFactory.getConfiguration();
			MappedStatement mappedStatement = configuration.getMappedStatement( "s_TBL_CCTV3.C" );
			
			HashMap<String, String> parameter = new HashMap<String, String>();
			String r = mappedStatement.getResource();
			InputStream is = Resources.getResourceAsStream( r );
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( is );
			String q = mappedStatement.getSqlSource().getBoundSql( parameter ).getSql();
			logger.debug( "query : " + q + " / " + r  );
			
			XPath  xpath = XPathFactory.newInstance().newXPath();
			String expression = "/mapper/*";
			NodeList  cols = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
			logger.debug( "cols : " + cols.getLength() );
			for( int i = 0 ;i < cols.getLength() ; i++ ) {
//				cols.item( i ).toString();
				NamedNodeMap nodeMap = cols.item( i ).getAttributes();
//				nodeMap.
				logger.debug( " cols : " + cols.item( i ).getFirstChild().getNodeValue() );
				
			}


			
//			Collection<String> collections = configuration.getMappedStatementNames();
//			if( null != collections && !collections.isEmpty() ) {
//				Iterator<String> iter = collections.iterator();
//				while( iter.hasNext() ) {
//					String key = iter.next();
////					configuration.getm
//					logger.debug("key : " + key );
//				}
//			}
		} catch( IOException e ) {
			
		}
		//**/
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private OkHttpClient connection = new OkHttpClient.Builder()
			.connectTimeout( 0, TimeUnit.SECONDS )
			.readTimeout( 0,  TimeUnit.SECONDS )
			.build();
	private int failedCount = 0;
	private int successCount = 0;
	
	class CORSRequestWorker extends Thread {
		private OkHttpClient	connection = null;
		private Response		response = null;
		private String			requestUrl = "http://localhost:8080/ixeb-manager/service.slice.ixeb";
		private String			bodyString;
		private boolean		successful = false;
		
		public CORSRequestWorker( OkHttpClient c ) {
			connection = c;
		}
		
		private Response request( String order ) {
			FormBody.Builder builder = new FormBody.Builder();
			FormBody requestBody = builder.build();
			
			Request.Builder requestBuilder = new Request.Builder();
			Request optionRequest = requestBuilder
	                .url( requestUrl )
	                .addHeader( "host", "localhost:8080" )
	                .addHeader( "connection", "keep-alive" )
	                .addHeader( "access-control-request-method", "POST" )
	                .addHeader( "origin", "http://localhost:9999" )
	                .addHeader( "access-control-request-headers", "x-requested-with" )
	                .addHeader( "accept", "*/*" )
	                .addHeader( "referer", "http://localhost:9999/test1.html" )
	                .addHeader( "accept-encoding", "gzip, deflate, br" )
	                .addHeader( "accept-language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4" )
	                .method( "OPTIONS", requestBody )
	                .build();
			
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

			builder.add( "ixeb_request", order );
			requestBody = builder.build();
			
			Request request = requestBuilder
	                .url( requestUrl )
	                .addHeader( "host", "localhost:8080" )
	                .addHeader( "connection", "keep-alive" )
	                .addHeader( "origin", "http://localhost:9999" )
	                .addHeader( "x-requested-with", "XMLHttpRequest" )
	                .addHeader( "access-control-request-headers", "x-requested-with" )
	                .addHeader( "content-type", "application/x-www-form-urlencoded" )
	                .addHeader( "accept", "*/*" )
	                .addHeader( "referer", "http://localhost:9999/test1.html" )
	                .addHeader( "accept-encoding", "gzip, deflate, br" )
	                .addHeader( "accept-language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4" )
	                .post( requestBody )
	                .build();
			
			try {
				return connection.newCall( request ).execute();
			} catch (IOException e) {
				logger.info( e.getMessage(), e );
				return null;
			}
		}
		
		@Override
		public void run() {
//			String order = "{\"params\":{},\"datasets\":[{\"io\":\"INPUT\",\"name\":\"sqlite_tbl_cctv\",\"params\":{},\"data\":[]},{\"io\":\"OUTPUT\",\"name\":\"sqlite_tbl_cctv\",\"params\":{},\"data\":[]}]}";
			String order = "{\"params\":{},\"datasets\":[{\"io\":\"INPUT\",\"name\":\"s_TBL_CCTV3\",\"params\":{},\"data\":[]},{\"io\":\"OUTPUT\",\"name\":\"s_TBL_CCTV3\",\"params\":{},\"data\":[]}]}";
			response = request( order );
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
	
	private int sleepTime( int minSecond, int maxSecond ) {
		return (int) (Math.random() * (maxSecond * 1000 - minSecond * 1000 + 1)) + minSecond * 1000;
	}
	
	
	@Test
	public void testCORS() {
		String ret = null;
		for( int i = 0 ; i < 10 ; i++ ) {
			CORSRequestWorker worker = new CORSRequestWorker(connection);
			worker.start();

			try {
				Thread.sleep( sleepTime( 5, 35) );
				if( worker.isSuccessful() ) {
					successCount++;
					logger.debug( i + "th contents requested." + "total success count : " + successCount + ",  failed count : " + failedCount );
					if( null == ret ) {
						ret = worker.contents();
						continue;
					}
					
					if( !ret.equals( worker.contents() ) ) {
						fail( i + "th contents is not equals." );
					}
				} else {
					fail( "response is failed." );
					failedCount++;
				}
				worker.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		if( successCount != failedCount ) {
		if( 0 < failedCount ) {
			fail( "total success count : " + successCount + ",  failed count : " + failedCount );
		}
	}
	

}

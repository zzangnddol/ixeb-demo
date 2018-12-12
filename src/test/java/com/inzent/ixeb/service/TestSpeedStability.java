package com.inzent.ixeb.service;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.inzent.ixeb.service.util.HttpHeaderCover;
import com.inzent.ixeb.service.util.RequestWorker;
import com.inzent.ixeb.service.util.TimeWatcher;
import com.inzent.ixeb.service.util.WebOrder;

/**
 * SDF의 속도 안정성을 테스트하기 위한 것
 * n개의 working thread로 최초 응답시간을 측정하여 허용된 시간내에 응답이 오는지 확인한다.
 * @author Inzent
 *
 */
public class TestSpeedStability {
	
	public class DefaultHeaderCover implements HttpHeaderCover {

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
	
	/**
	 * 단일 요청에서 요청 응답시간이 허용한 시간내에 동작하는지 테스트한다.
	 */
//	@Test
	public void TestSingleWorker() {
		RequestWorker worker = new RequestWorker(  );
		worker.addHeaderCover( new DefaultHeaderCover() );
		String order = "{\"params\":{},\"datasets\":[{\"io\":\"INPUT\",\"name\":\"s_TBL_CCTV3\",\"params\":{},\"data\":[]},{\"io\":\"OUTPUT\",\"name\":\"s_TBL_CCTV3\",\"params\":{},\"data\":[]}]}";
		worker.setRequestUrl( "http://localhost:8080/ixeb-manager/service.slice.ixeb" ).setOrder( order );
		TimeWatcher watch = new TimeWatcher();
		worker.run();
//		System.out.println( worker.contents() );
		System.out.println( "elapsed time : " + watch.elapsedTime() + " ms." );
	}

	class WatchThread extends Thread {
		private Runnable	runner = null;
		private TimeWatcher watch = new TimeWatcher();
		private String			name;
		private long			elapsedTime = 0;
		
		public WatchThread( Runnable r, String n ) {
			runner = r;
			name = n;
		}
		
		@Override
		public void run() {
			watch.reset();
			runner.run();
			elapsedTime = watch.elapsedTime();
			synchronized (atomic) {
				int finished_Thread = increaseFinishedThread();
				System.out.println( name + " thread's elapsed time : " + elapsedTime + " ms. ( finished thread number is " + finished_Thread + ". )" );
				if( elapsedTime > allowTime ) {
					setFail();
				}
			}
		}
	}

	private volatile Object atomic = new Object();
	private volatile int finishedThread = 0;
	private volatile boolean failed = false;
	
	private int increaseFinishedThread() {
		return finishedThread++;
	}
	
	private void setFail() {
		failed = true;
	}
	
	private long allowTime = 15 * 1000;
	
	@Test
	public void TestSimultaneous() throws Exception {
		finishedThread = 0;
		int number = 30;
		WatchThread[] threads = new WatchThread[ number ];
		for( int i = 0 ; i < number ; i++ ) {
			RequestWorker worker = new RequestWorker(  );
			worker.addHeaderCover( new DefaultHeaderCover() );
//			String order = "{\"params\":{},\"datasets\":[{\"io\":\"INPUT\",\"name\":\"s_TBL_CCTV3\",\"params\":{},\"data\":[]},{\"io\":\"OUTPUT\",\"name\":\"s_TBL_CCTV3\",\"params\":{},\"data\":[]}]}";
//			WebOrder order = ;
			worker.setRequestUrl( "http://localhost:8080/ixeb-manager/service.slice.ixeb" )
//			worker.setRequestUrl( "http://localhost:8080/mybatisEmbody/sample.ixeb" )
						.setOrder( new WebOrder()
													.addMenu( "s_TBL_CCTV3", "INPUT" )
													.addMenu( "s_TBL_CCTV3", "OUTPUT" ) );
			
			threads[ i ] = new WatchThread( worker, i + "th thread " );
		}
		
		for( int i = 0 ; i < number ; i++ ) {
			threads[ i ].start();
		}
		
		
		while( finishedThread != number ) {
			try {
				Thread.sleep( 100 );
				if( failed ) {
					fail( "다중요청 테스트 도중 실패하였습니다." );
					throw new Exception("" );
				}
				if( finishedThread == number ) {
					System.out.println( "if( finishedThread : " + finishedThread + " == number )" );
					return;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

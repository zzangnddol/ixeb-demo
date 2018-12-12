package test.com.inzent.ixeb.manager.sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.inzent.ixeb.IxebException;
import com.inzent.ixeb.io.IxebObject;
import com.inzent.ixeb.io.IxebObjectType;

public class SliceTransformTest {
  private static Logger logger = Logger.getLogger( SliceTransformTest.class );
  

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }
  
  static class MyResponseConsumer extends AsyncCharConsumer<Boolean> {
    private FileWriter fw = null;
    private BufferedWriter bw = null;
    private PrintWriter out = null;

    public MyResponseConsumer() {
      
      try {
        fw = new FileWriter("/home/paradigm/mybatis/slice.ixeb", true);
        bw = new BufferedWriter(fw);
        out = new PrintWriter(bw);
      } catch (IOException e) {
        logger.error( "raised exception : " + e.getMessage(), e );
      }
    }
    
    @Override
    protected void onCharReceived(CharBuffer charBuffer, IOControl ioControl) throws IOException {
      //charBuffer.
      logger.debug( "protected void onCharReceived(CharBuffer charBuffer, IOControl ioControl)" );
      logger.debug( "length : " + charBuffer.length() + "[" + charBuffer.toString() + "]" );
      if( null != out ) {
        out.print( charBuffer.toString() );
      }
    }

    @Override
    protected Boolean buildResult(HttpContext context ) throws Exception {
      logger.debug( "protected Boolean buildResult(HttpContext context )" );
      logger.debug( context.toString() );
      out.close();
      bw.close();
      fw.close();
      return true;
    }

    @Override
    protected void onResponseReceived(HttpResponse response ) throws HttpException, IOException {
      logger.debug( "protected void onResponseReceived(HttpResponse response )" );
    }
    
  }

  @Test
  public void test() {
    CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
    try {

      httpClient.start();
      IxebObject param = new IxebObject( IxebObjectType.MAP );
      String content = "ixeb_request=" + generateParameter( "OUTPUT", "tbl_mybatis", param );
      logger.debug( "=====================================================================================");
      logger.debug( "content : " + content );
      ///**
      Future<Boolean> future = httpClient.execute( 
          HttpAsyncMethods.createPost( "http://localhost:8080/ixeb-manager/SampleServiceCRUD", content, ContentType.APPLICATION_FORM_URLENCODED ),
          new MyResponseConsumer(), null );
      Boolean result = future.get();
      if( result != null && result.booleanValue() ) {
        logger.debug( "Request successfully executed." );
        logger.debug(  future.toString() );
      } else {
        logger.debug( "Request failed" );
      }
      logger.debug( "shutting down" );
      //**/
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (UnsupportedEncodingException e) {
      logger.error( "raised exception : " + e.getMessage(), e );
    } catch (InterruptedException e) {
      logger.error( "raised exception : " + e.getMessage(), e );
    } catch (ExecutionException e) {
      logger.error( "raised exception : " + e.getMessage(), e );
    } finally {
      try {
        httpClient.close();
      } catch (IOException e) {
        logger.error( "raised exception : " + e.getMessage(), e );
      }
    }
    logger.debug( "Done");
  }

  public String generateParameter( String io, String name, IxebObject d ) throws IxebException {
    IxebObject parameter = new IxebObject( IxebObjectType.MAP );
    IxebObject datasets = new IxebObject( IxebObjectType.ARRAY );

    {
      IxebObject dataset = new IxebObject( IxebObjectType.MAP );
      dataset.add( "io", io );
      dataset.add( "name", name );
      IxebObject data = new IxebObject( IxebObjectType.ARRAY );
      
      data.add( d );
      
      dataset.add( "data", data );
      
      datasets.add( dataset );
    }
    parameter.add( "datasets", datasets );
    
    return parameter.toJSONString();
  }
  
  
  
}

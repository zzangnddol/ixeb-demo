package test.com.inzent.ixeb.manager.sample;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.inzent.ixeb.IxebException;
import com.inzent.ixeb.io.IxebObject;
import com.inzent.ixeb.io.IxebObjectType;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
//import com.meterware.httpunit.;
import com.meterware.httpunit.WebResponse;

public class SampleMapperCRUDTest {
  private static Logger logger = Logger.getLogger( SampleMapperCRUDTest.class );
  
  private final static String url = "http://localhost:8080/ixeb-manager/service.manager.ixeb";

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
  
  @Test
  public void testServiceInsert() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", "tbl_mybatis4" );    // Create id - {"namespace" : "tbl_mybatis"}
      params.add( "tableName", "TBL_MYBATIS" );   // RDB Table Name
      
      req.add( "method", "service.create");
      req.add( "params", params );
      //"{ method : \"service.create\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      request.setParameter( "ixeb_request", req.toJSONString() );
//      con.addClientListener( this );
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
      
    }
  }
  
  @Test
  public void testServiceUpdate() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", "tbl_mybatis3" );
      
      String  strContent = "{\"storeType\" : \"dbms\", \"namespace\" :\"tbl_mybatis3\", \"queries\" : [{\"mapperId\" : \"select\", \"type\" : \"select\", \"resultType\" : \"java.util.HashMap\", \"query\" : \"SELECT USERNAME, PASSWORD, EMAIL FROM tbl_mybatis\"}, {\"parameterType\" : \"java.util.HashMap\", \"mapperId\" : \"insert\", \"type\" : \"insert\", \"query\" : \"INSERT INTO tbl_mybatis( USERNAME, PASSWORD, EMAIL)  VALUES ( #{USERNAME}, #{PASSWORD}, #{EMAIL} ) \"}, {\"parameterType\" : \"java.util.HashMap\", \"mapperId\" : \"update\", \"type\" : \"update\", \"query\" : \"UPDATE tbl_mybatis SET USERNAME = #{USERNAME}, PASSWORD = #{PASSWORD}, EMAIL = #{EMAIL}\"}, {\"parameterType\" : \"java.util.HashMap\", \"mapperId\" : \"delete\", \"type\" : \"delete\", \"query\" : \"DELETE FROM tbl_mybatis where\"}]}";
      IxebObject mapperObject = new IxebObject(  );
      JSONParser parser = new JSONParser();
      try {
        JSONObject jsonObject = (JSONObject) parser.parse( strContent );
        mapperObject.set(jsonObject);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      params.add( "mapper", mapperObject );
      //"{ method : \"service.create\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";

      req.add( "method", "service.update");
      req.add( "params", params );
      System.out.println("req.toJSONString() : " +req.toJSONString());
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
      
    }
  }
  
  @Test
  public void testServiceDelete() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", "tbl_mybatis2" );
      
      req.add( "method", "service.delete");
      req.add( "params", params );
      //"{ method : \"service.delete\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
    }
  }
  
  @Test
  public void testServiceSelect() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", "tbl_mybatis" ); // namespace
//      params.add( "tableName", "tbl_mybatis" );  // mapperId
//      params.add( "serviceId", "" );    
//      params.add( "tableName", "" );
      
      req.add( "method", "service.query");
      req.add( "params", params );
      //"{ method : \"service.delete\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( "response.getText() : " +response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
    }
  }
  
  @Test
  public void testServiceFields() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", "tbl_mybatis" ); // namespace
//      params.add( "tableName", "TBL_JSON_T" );  // mapperId
      
      req.add( "method", "service.fields");
      req.add( "params", params );
      //"{ method : \"service.delete\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      System.out.println("req : " +req.toJSONString());
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( "response.getText() : " +response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
    }
  }

//  public void requestSent(WebClient arg0, WebRequest arg1) {
//    // TODO Auto-generated method stub
//    
//  }
//
//  public void responseReceived(WebClient client, WebResponse response) {
//    // TODO Auto-generated method stub
//    //response.
//    
//  }
  
  @Test
  public void testServiceConfigurationDelete() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", 2 );
      
      req.add( "method", "service.configDelete");
      req.add( "params", params );
      //"{ method : \"service.create\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      System.out.println("req.toJSONString() : " +req.toJSONString());
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
      
    }
  }
  
  @Test
  public void testServiceConfigurationUpdate() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", 2 );
      
      IxebObject mapperObject = new IxebObject(  );
      String  strContent = "{\"environments\" : {\"default\" : \"development\", \"environment\" : [{\"id\" : \"development\", \"dataSource\" : {\"properties\" : [{\"name\" : \"password\", \"value\" : \"libeka\"}, {\"name\" : \"url\", \"value\" : \"jdbc:oracle:thin:@10.1.21.110:1521:XE\"}, {\"name\" : \"driver\", \"value\" : \"oracle.jdbc.driver.OracleDriver\"},{\"name\" : \"username\", \"value\" : \"LIBEKA_UPDATE\"}], \"type\" : \"POOLED\"}, \"transactionManager\" : {\"type\" : \"JDBC\"}}]}}";
      JSONParser parser = new JSONParser();
      try {
        JSONObject jsonObject = (JSONObject) parser.parse( strContent );
        mapperObject.set(jsonObject);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      params.add( "content", mapperObject );
      
      req.add( "method", "service.configUpdate");
      req.add( "params", params );
      //"{ method : \"service.create\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      System.out.println("req.toJSONString() : " +req.toJSONString());
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
      
    }
  }
  
  @Test
  public void testServiceConfigurationCreate() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      IxebObject mapperObject = new IxebObject(  );
      String  strContent = "{\"environments\" : {\"default\" : \"development\", \"environment\" : [{\"id\" : \"development\", \"dataSource\" : {\"properties\" : [{\"name\" : \"password\", \"value\" : \"libeka\"}, {\"name\" : \"url\", \"value\" : \"jdbc:oracle:thin:@10.1.21.110:1521:XE\"}, {\"name\" : \"driver\", \"value\" : \"oracle.jdbc.driver.OracleDriver\"},{\"name\" : \"username\", \"value\" : \"LIBEKA2\"}], \"type\" : \"POOLED\"}, \"transactionManager\" : {\"type\" : \"JDBC\"}}]}}";
      JSONParser parser = new JSONParser();
      try {
        JSONObject jsonObject = (JSONObject) parser.parse( strContent );
        mapperObject.set(jsonObject);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      params.add( "content", mapperObject );
      
      req.add( "method", "service.configCreate");
      req.add( "params", params );
      //"{ method : \"service.create\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      System.out.println("req.toJSONString() : " +req.toJSONString());
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
      
    }
  }
  
  @Test
  public void testServiceConfigurationSelect() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", 1 );
      
      req.add( "method", "service.configSelect");
      req.add( "params", params );
      
      //"{ method : \"service.create\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
      
    }
  }
  
  @Test
  public void testServiceConfigurationSelectAll() {
    WebConversation con = new WebConversation();
    try {
      WebRequest request = new PostMethodWebRequest( url );

      IxebObject req = new IxebObject( IxebObjectType.MAP );
      IxebObject params = new IxebObject( IxebObjectType.MAP );
      params.add( "serviceId", 1 );
      
      req.add( "method", "service.configSelectAll");
      req.add( "params", params );
      
      //"{ method : \"service.create\", params : { serviceId : \"tbl_mybatis\", tableName : \"tbl_mybatis\" } }";
      request.setParameter( "ixeb_request", req.toJSONString() );
      
      WebResponse response = con.getResponse( request );
      logger.debug( response.getText() );
    } catch( IxebException e ) {
        Assert.fail( e.getMessage() );
    } catch (IOException e) {
      Assert.fail( e.getMessage() );
    } catch (SAXException e) {
      Assert.fail( e.getMessage() );
    } finally {
      
    }
  }
  
  
}

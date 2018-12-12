package test.com.inzent.ixeb.manager.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
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
import com.meterware.httpunit.WebResponse;

public class SampleServiceCRUDTest {
  private static Logger logger = Logger.getLogger( SampleServiceCRUDTest.class );

  private final static String url = "http://localhost:8080/ixeb-manager/service.ixeb";
  
  
  private class IxebRequestGenerator {

	  public class Dataset {
		  private String mode;
		  private String name;
		  
		  private Dataset( String m, String n ) {
			  mode = m;
			  name = n;
		  }
		  
		  private HashMap<String, String> params = new HashMap<String, String>();
//		  private Vector<IxebObject> data = new Vector<IxebObject>();

		  public Dataset addParameter( String key, String value ) {
			  params.put( key, value);
			  return this;
		  }
		  
//		  public Dataset addData( IxebObject d ) {
//			  data.addElement( d );
//			  return this;
//		  }
		  
		  public String getMode() {
			  return mode;
		  }
		  
		  public String getName() {
			  return name;
		  }
		  
		  public Set<String> parameterKeySet() {
			  return params.keySet();
		  }
		  
//		  public Iterator<IxebObject> dataIterator() {
//			  return data.iterator();
//		  }
		  
		  public String getParameter( String key ) {
			  return params.get( key );
		  }
		  
	  }
	  
	  private Vector<Dataset> datasets  = new Vector<Dataset>();
	  
	  public Dataset newDataset( String mode, String name ) {
		  return new Dataset( mode, name );
	  }
	  
	  public IxebRequestGenerator addDataset( Dataset d ) {
		  datasets.add( d );
		  return this;
	  }
	  
	  public String toRequest() throws IxebException {
//		  String retval = new String();
//		  JSONObject
		    IxebObject parameter = new IxebObject( IxebObjectType.MAP );
		    IxebObject datasetArray = new IxebObject( IxebObjectType.ARRAY );

		    Iterator<Dataset> d = datasets.iterator();
		    while( d.hasNext() ) {
		    	Dataset data = d.next();
		    	IxebObject datasetElem = new IxebObject( IxebObjectType.MAP );
		    	datasetElem.add( "io", data.getMode() );
		    	datasetElem.add( "name", data.getName() );
		    	if( data.parameterKeySet().size() > 0 ) {
		    		IxebObject paramElem = new IxebObject( IxebObjectType.MAP );
			    	for( String key : data.parameterKeySet() ) {
			    		paramElem.add( key, data.getParameter( key ) );
			    	}
			    	datasetElem.add( "params", paramElem );
		    	}
		    	datasetArray.add( datasetElem );
		    	
//		    	Iterator<IxebObject> datas = data.dataIterator();
//		    	if( datas.hasNext() ) {
//		    		
//		    	}
		    }
		    parameter.add( "datasets", datasetArray );
		    return parameter.toJSONString();
	  }
  }
  
  
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
  public void testSelect() {
  	logger.debug(  "testSelect" );
	    WebConversation con = new WebConversation();
	    IxebRequestGenerator generator = new IxebRequestGenerator();
	    try {
	    	IxebRequestGenerator.Dataset dataset = generator.newDataset( "OUTPUT", "kkk" );
	    	dataset.addParameter( "TOTAL", "7" );
	    	generator.addDataset( dataset );
	    	
	    	logger.debug(  "ixeb_request : " + generator.toRequest() );
	    	
			WebRequest request = new PostMethodWebRequest( url );
			request.setParameter( "ixeb_request", generator.toRequest() );
			  
			WebResponse response = con.getResponse( request ); 
			logger.debug( response.getText() );
	    } catch( IxebException e ) {
	        Assert.fail( e.getMessage() );
	    } catch (IOException e) {
	        Assert.fail( e.getMessage() );
	      } catch (SAXException e) {
	        Assert.fail( e.getMessage() );
//	      } catch (ParseException e) {
//	       Assert.fail( e.getMessage() );
	      } finally {
	    	  
	      }
  }
  
//  @Test
//  public void testJar() {
//	  try {
//		JarFile jarfile = new JarFile( "C:\\Users\\Inzent\\Documents\\ixeb-server-api.jar" );
//	    Manifest manifest = jarfile.getManifest();
//	    Attributes att = manifest.getMainAttributes();
////	    return att.getValue("Implementation-Version");
//	    logger.debug( "version : " + att.getValue("Implementation-Version"));
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//  }

}

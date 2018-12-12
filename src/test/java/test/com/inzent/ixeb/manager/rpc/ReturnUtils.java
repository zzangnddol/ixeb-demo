package test.com.inzent.ixeb.manager.rpc;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ReturnUtils {
	  private static Logger logger = Logger.getLogger( ReturnUtils.class );

	public static List<MappedQuery> asMappedQueries( Object object ) throws IllegalArgumentException {
		Class<?> classType = object.getClass();
		if( !classType.equals( JSONArray.class ) ) {
			logger.debug( "if( !classType.equals( JSONArray.class ) )" );
			throw new IllegalArgumentException();
		}
		
		List<MappedQuery> retval = new Vector<MappedQuery>();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iter = ( (JSONArray) object ).iterator();
		while( iter.hasNext() ) {
			JSONObject o = iter.next();
			String q = (String) o.get( "query" );
			String mappedId = (String) o.get( "mappedId" );
			retval.add( new MappedQuery( mappedId, null, null,  q ) );
		}
		
		return retval;
	}
	
	public static List<IxebMapper> asMappers( Object object ) throws IllegalArgumentException {
		Class<?> classType = object.getClass();
		if( !classType.equals( JSONArray.class ) ) {
			logger.debug( "if( !classType.equals( JSONObject.class ) )" );
			throw new IllegalArgumentException();
		}
		
		List<IxebMapper> retval = new Vector<IxebMapper>();
		
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iter = ( (JSONArray) object ).iterator();
		while( iter.hasNext() ) {
			JSONObject o = iter.next();
			
			String storeType = (String) o.get( "storeType");
			String namespace = (String) o.get( "namespace");
			
			List<MappedQuery> mappedQueries = asMappedQueries( o.get( "queries") );
			
			IxebMapper mapper = new IxebMapper( storeType, namespace );
			
			mapper.setQueries( mappedQueries );
			
			retval.add( mapper );
		}
		return retval;
	}
	
	public static List<String> asFields( Object object ) throws IllegalArgumentException {
		Class<?> classType = object.getClass();
		if( !classType.equals( JSONArray.class ) ) {
			logger.debug( "if( !classType.equals( JSONObject.class ) )" );
			throw new IllegalArgumentException();
		}
		
		List<String> retval = new Vector<String>();

		@SuppressWarnings("unchecked")
		Iterator<String> iter = ( (JSONArray) object ).iterator();
		while( iter.hasNext() ) {
			String o = iter.next();
			retval.add( o );
		}

		return retval;
	}
	
}

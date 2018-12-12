package test.com.inzent.ixeb.manager.rpc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import test.com.inzent.ixeb.manager.RPCProtocolGenerator;

public class MethodBuilder {

	public static final String serviceCreate( String serviceId, String tableName ) {
		final String method = "SERVICE.CREATE";
		RPCProtocolGenerator generator = new RPCProtocolGenerator();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put( "serviceId", serviceId );
		parameters.put( "tableName", tableName );
		generator.makeMethod( method, parameters);
		return generator.stringify();
	}
	
	public static final String serviceUpdate( String serviceId, String storeType, String namespace, List<MappedQuery> mappedQueries ) {
		final String method = "SERVICE.UPDATE";
		RPCProtocolGenerator generator = new RPCProtocolGenerator();
		Map<String, Object> parameters = new HashMap<String, Object>();

		List<Map<String,Object>> mappedQuery = new Vector<Map<String,Object>>();
		Iterator<MappedQuery> iter = mappedQueries.iterator();
		while( iter.hasNext() ) {
			MappedQuery query = iter.next();
			mappedQuery.add( query.toMap() );
		}
		
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put( "storeType", storeType );
		mapper.put( "namespace", namespace);
		mapper.put( "queries", mappedQuery );
		
		parameters.put( "serviceId", serviceId );
		parameters.put( "mapper", mapper );
		
//		parameters.put( "namespace", namespace );
//		parameters.put( "queries", mappedQuery );
		
		generator.makeMethod( method, parameters);
		return generator.stringify();
	}
	
	public static final String serviceDelete( String serviceId ) {
		final String method = "SERVICE.DELETE";
		RPCProtocolGenerator generator = new RPCProtocolGenerator();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put( "serviceId", serviceId );
		
		generator.makeMethod( method, parameters );
		return generator.stringify();
	}
	
	public static final String serviceQuery(  ) {
		return serviceQuery( null );
	}
	
	public static final String serviceQuery( String serviceId ) {
		final String method = "SERVICE.QUERY";
		RPCProtocolGenerator generator = new RPCProtocolGenerator();
		Map<String, Object> parameters = new HashMap<String, Object>();
		if( null != serviceId )  {
			parameters.put( "serviceId", serviceId );
		}
		generator.makeMethod( method, parameters);
		return generator.stringify();
	}
	
	public static final String serviceFields( String serviceId ) {
		final String method = "SERVICE.FIELDS";
		RPCProtocolGenerator generator = new RPCProtocolGenerator();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put( "serviceId", serviceId );
		generator.makeMethod( method, parameters);
		return generator.stringify();
	}
	
}

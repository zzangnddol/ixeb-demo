package test.com.inzent.ixeb.manager.rpc;

import java.util.HashMap;
import java.util.Map;

public class MappedQuery {
	private String mappedId;
	private String resultType;
	private String parameterType;
	private String statementType = null;
	private String query;
	

	public MappedQuery( String id, String sType, String parameterType, String resultType, String query ) {
		mappedId = id;
		this.statementType = sType;
		this.parameterType = parameterType;
		this.resultType = resultType;
		this.query = query;
	}
	
	public MappedQuery( String id, String parameterType, String resultType, String query ) {
		mappedId = id;
		this.parameterType = parameterType;
		this.resultType = resultType;
		this.query = query;
	}
	
	public String toQuery() {
		return query;
	}
	

	public Map<String, Object> toMap() {
		Map<String, Object> retval = new HashMap<String, Object>();
		if( null != mappedId ) {
			retval.put( "mapperId", mappedId );
		}
		
		if( null != statementType ) {
			retval.put( "statementType", statementType );
		}

		if( null != parameterType  ) {
			retval.put( "type", parameterType);
		}
		
		if( null != resultType ) {
			retval.put( "resultType", resultType );
		}
		
		if( null != query ) {
			retval.put( "query", query );
		}
		
		return retval;
	}
	
}

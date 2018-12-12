package test.com.inzent.ixeb.manager.rpc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Return {
	private Object returnObject = null;
	private boolean error = false;
	
	public Return( String response ) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject result = (JSONObject) parser.parse( response );
			if( result.containsKey( "error") ) {
				error = true;
				returnObject = (JSONObject) result.get( "error" );
				return;
			}
			returnObject = (JSONObject) result.get( "result");
		} catch (ParseException e) {
			error = true;
		}
	}
	
	public Object getReturnObject() {
		return returnObject;
	}
	
	public boolean hasError() {
		return error;
	}
}

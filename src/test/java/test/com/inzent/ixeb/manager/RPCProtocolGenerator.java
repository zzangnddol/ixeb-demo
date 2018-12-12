package test.com.inzent.ixeb.manager;

import java.util.HashMap;
import java.util.Map;

import com.inzent.ixeb.io.util.ObjectStringifier;

public class RPCProtocolGenerator {
	private Map<String, Object> protocol = new HashMap<String, Object>();
	
	public RPCProtocolGenerator makeMethod( String name, Map<String, Object> parameters )
			throws IllegalArgumentException {
		if( protocol.containsKey( "method" ) ) {	// 예외 발생
			throw new IllegalArgumentException();
		}
		
		if( null != parameters ) {
			if( protocol.containsKey( "params") ) {	// 예외 발생
				throw new IllegalArgumentException();
			}
			
			protocol.put( "params", parameters );
		}
		protocol.put( "method", name );
		
		return this;
	}
	
	public String stringify() {
		ObjectStringifier stringifier = new ObjectStringifier( protocol );
		return stringifier.stringify();
	}
	
}

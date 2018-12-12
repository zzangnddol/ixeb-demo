package test.com.inzent.ixeb.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.inzent.ixeb.IxebException;
import com.inzent.ixeb.io.Bundle;
import com.inzent.ixeb.io.util.ObjectStringifier;

public class IxebProtocolGenerator {
	private List<Bundle>	bundles = new Vector<Bundle>();

	public Bundle newBundle( String name, String ioType, Map<String, Object> param, List<Object> headers )
			throws IxebException {
		Bundle.Builder builder = new Bundle.Builder( name, ioType );
		if( null != param ) {
			Set<String> keys = param.keySet();
			for( String key : keys ) {
				builder.addParameter( key, param.get( key ) );
			}
		}
	
		Bundle bundle = builder.build();
		bundles.add( bundle );
		return bundle;
	}

	@SuppressWarnings("unchecked")
	public IxebProtocolGenerator addBundleRow(Bundle bundle, Object row) throws IxebException {
		if( null == bundle ) {
			return this;
		}
		
		Class<?> clazz = row.getClass();
		if( clazz.getName().equals("org.springframework.util.LinkedCaseInsensitiveMap")
				|| clazz.equals( HashMap.class ) ) {
			bundle.addRecordRow( (Map<String, Object>) row );
		} else {
			throw new IxebException( new UnsupportedOperationException() );
		}
		return this;
	}

	public String stringify() {
		Map<String, Object> ixeb = new LinkedHashMap<String, Object>();
		List<Object> datasets = new Vector<Object>();
		{
			Iterator<Bundle> iter = bundles.iterator();
			while( iter.hasNext() ) {
				Bundle bundle = iter.next();
				Map<String, Object> elem = bundle.toMap();
				datasets.add( elem );
			}
		}
		ixeb.put( "datasets", datasets );
		ObjectStringifier stringifier = new ObjectStringifier( ixeb );
		
		return stringifier.stringify();
	}

}

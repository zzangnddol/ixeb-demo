package test.com.inzent.ixeb.manager.rpc;

import java.util.List;

public class IxebMapper {
	private String storeType = null;
	private String namespace = null;
	private List<MappedQuery> queries = null;

	public IxebMapper( String s, String n ) {
		storeType = s;
		namespace = n;
	}
	

	public String getStoreType() {
		return storeType;
	}

	public String getNamespace() {
		return namespace;
	}
	
	public void setQueries( List<MappedQuery> q ) {
		queries = q;
	}
	
}

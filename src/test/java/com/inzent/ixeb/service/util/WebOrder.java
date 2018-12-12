package com.inzent.ixeb.service.util;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class WebOrder {
	
	class menu {
		String name;
		String type;
		
		public menu( String n, String t ) {
			name = n;
			type = t;
		}
		
		public String getName() {
			return name;
		}
		
		public String getType() {
			return type;
		}
		
	}
	
	private List<menu> menus = new Vector<menu>();
	
	public WebOrder addMenu( String name, String type ) {
		menus.add( new menu( name, type ) );
		return this;
	}

	public String stringify() {
		StringBuffer retval = new StringBuffer();
		retval.append( "{\"params\":{},\"datasets\":[");
		Iterator<menu> iter = menus.iterator();
		while( iter.hasNext() ) {
			menu menu = iter.next();
			retval.append( "{\"io\":\"")
					.append( menu.getType() ).append( "\",\"name\":\"" )
					.append( menu.getName() ).append( "\",\"params\":{},\"data\":[]}" );
			if( iter.hasNext() ) {
				retval.append( ",");
			}
		}
		retval.append( "]}" );
		return retval.toString();
	}
	
}

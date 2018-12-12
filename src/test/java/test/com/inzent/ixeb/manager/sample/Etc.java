package test.com.inzent.ixeb.manager.sample;

import org.apache.log4j.Logger;
import org.junit.Test;

public class Etc {
	  private static Logger logger = Logger.getLogger( Etc.class );

	@Test
	public void test() {
//		fail("Not yet implemented");
		String query = "SELECT 	\rCOMPID, NAME, PARENT_ID, ID, LEVLCD, TITEXT1, TITEXT2, TITEXT3, TITEXT4, TITEXT5, TITEXT6, TITEXT7, TITEXT8, TITEXT9, TITEXT10, TITEXT11, TITEXT12, TITEXT13, TITEXT14, TITEXT15, TITEXT16, TITEXT17, TITEXT18, TITEXT19, TITEXT20, TITEXT21, TITEXT22, TITEXT23, TITEXT24, TITEXT25, TITEXT26, TITEXT27, TITEXT30, ULLTIME_TOTAL, TOTAL, sort, seq FROM TBL_PMS001";
		
		query = query.replaceAll( "\t", "\\\\t");
		logger.debug( query );
	}

}

package test.com.inzent.ixeb.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class DiffTest {

	public List<String> fileToLines( String filename ) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore ... any errors should already have been
					// reported via an IOException from the final flush.
				}
			}
		}
		return lines;
	}
	
	 static final String ORIGINAL = "D:\\workspace\\xebwser\\xebwser.20161020\\pane\\html\\index.html";
	 static final String RIVISED = "D:\\workspace\\xebwser\\xebwser.20161026\\pane\\html\\index.html";
	    
	@Test
	public void test() {
//		Assert.fail( "Not yet implemented" );
		List<String> original = fileToLines(ORIGINAL);
        List<String> revised  = fileToLines(RIVISED);

        // Compute diff. Get the Patch object. Patch is the container for computed deltas.
        Patch<String> patch = DiffUtils.diff(original, revised);

        for (Delta<String> delta: patch.getDeltas()) {
        	
        	System.out.print( delta.getType() );
        	Chunk<String> chunkes = delta.getRevised();
        	System.out.println( " : position : " + chunkes.getPosition() );
        	
        	ListIterator<String> iterator = chunkes.getLines().listIterator();
        	while( iterator.hasNext() ) {
        		System.out.println( iterator.next() );
        	}
            //System.out.println(delta.getRevised().);
        }
	}

}

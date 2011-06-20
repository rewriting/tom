package test;

import java.io.IOException;

import newparser.HostParser;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;

public class Test_ParseAndDrawTrees {

	public static void main(String args[]) throws IOException{
		
		final String TOM_HOME = System.getenv("TOM_HOME"); 
		if(TOM_HOME==null){
			throw new RuntimeException("$TOM_HOME is not set...");
		}

		String testedFile = TOM_HOME+"/../../applications/islandgrammar2/input/02_match_rec";
		String pngFile = TOM_HOME+"/../../applications/islandgrammar2/output/02_match_rec.png";
		
		CharStream input = new ANTLRFileStream(testedFile);
		Tree tree = new HostParser().parse(input);
		
		futil.ANTLRTreeDrawer.draw(pngFile, tree);
	}
}

package tom.mapping.dsl.tests;


import static org.junit.Assert.*;
import org.junit.Test;
import tom.mapping.dsl.TomMappingFrontEnd;


public class TomMappingParsingTest {
	
	private static final String FILENAME = "/home/steven/workspace-demo-alma/tom.mapping.dsl/src/tom/mapping/dsl/TomMapping.tmap";

	@Test
	public void test_read_gdf_0() {
		try {
			TomMappingFrontEnd.parseFile(FILENAME);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	

}

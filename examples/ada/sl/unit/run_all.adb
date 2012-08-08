with TestBehaviour, TestPosition, TestEnvironment, TestSLModes;
use  TestBehaviour, TestPosition, TestEnvironment, TestSLModes;


with Ada.Text_IO;
use  Ada.Text_IO;

procedure run_all is
begin

	put("TestPosition...");
	TestPosition.run_test;
	put_line("OK");
	
	
	put("TestBehaviour...");
	TestBehaviour.run_test;
	put_line("OK");
	
	put("TestEnvironment...");
	TestEnvironment.run_test;
	put_line("OK");
	
	put("TestSLModes...");
	TestSLModes.run_test;
	put_line("OK");
	

end;

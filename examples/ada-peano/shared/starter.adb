with Main; use Main;
with SharedObjectFactoryP;
with Ada.Text_IO; use Ada.Text_IO;

procedure Starter is
       proto : testie ;	
       factory : SharedObjectFactoryP.SharedObjectFactory ;
	
begin

Put_Line(proto.toString);

end Starter;

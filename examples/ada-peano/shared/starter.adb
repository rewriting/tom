with Main; use Main;
with SharedObjectFactoryP; use SharedObjectFactoryP;
with Ada.Text_IO; use Ada.Text_IO;

procedure Starter is
       proto : testie ;	
       factory : SharedObjectFactoryP.SharedObjectFactory ;
	address : SharedObjectFactoryP.SharedObjectPtr ;
	status : Integer;       
begin

Put_Line(proto.toString&"Hellloooooo! Test1: Insertion proto dans factory");
Put_Line("Avant:"&toString(factory));
build(factory,proto,address,status);
Put_Line("build a renvoyé: "&Integer'Image(status)) ;
Put_Line("Après:"&toString(factory));


end Starter;

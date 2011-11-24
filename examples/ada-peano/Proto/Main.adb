    with Ada.Text_IO; use Ada.Text_IO;
    with main.PeanoAbstractType.Nat.plus ; 
    with main.PeanoAbstractType.Nat.suc ; 
    with main.PeanoAbstractType.Nat.zero ;
    use main.PeanoAbstractType; 
    with SharedObjectFactoryP ; use SharedObjectFactoryP ; 
    package body Main is 

procedure Alpha is
begin

--Checking Factory
stats(factory.all) ;

end Alpha;

end Main; 

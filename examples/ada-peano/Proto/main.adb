    with Ada.Text_IO; use Ada.Text_IO;
   
    with main.PeanoAbstractType.Nat.plus ;
    use main.PeanoAbstractType.Nat.plus ; 
   
    with main.PeanoAbstractType.Nat.suc ; 
    use main.PeanoAbstractType.Nat.suc ;  
   
    with main.PeanoAbstractType.Nat.zero ; 
    use main.PeanoAbstractType.Nat.zero ;
    
    use main.PeanoAbstractType; use main.PeanoAbstractType.Nat;
    with SharedObjectFactoryP ; use SharedObjectFactoryP ;

    package body Main is 

	  void : ZeroPtr;
	  voidbis : ZeroPtr ;
	  voidter : ZeroPtr ;
	  voidquad : PlusPtr ;	
 
	  one : SucPtr ;
	  two : SucPtr ;
	  three : SucPtr ;
	  four : SucPtr ;
	  five : SucPtr ;
	  six : SucPtr ;
	  seven : SucPtr ;

	  twobis : SucPtr ; 
	  twoter : SucPtr ;

	  threebis : SucPtr ;

	  sevenplus : PlusPtr ;
	  sevenplusalt : PlusPtr ;

procedure Alpha is
begin

--Checking Factory
stats(factory.all) ;

-- Creating zero and duplicates
void := Main.PeanoAbstractType.Nat.zero.make ;
voidbis := Main.PeanoAbstractType.Nat.zero.make ;
voidter := Main.PeanoAbstractType.Nat.zero.make ;
voidquad := Main.PeanoAbstractType.Nat.plus.make(NatPtr(voidbis),NatPtr(void));

-- Creating 1 to seven by suc
one := Main.PeanoAbstractType.Nat.suc.make(NatPtr(void));
two :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(one));
three :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(two));
four :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(three));
five :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(four));
six :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(five));
seven :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(six));

-- Verifying again that the table doesn't create the same object twice. 
twobis :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(one));
twoter :=  Main.PeanoAbstractType.Nat.suc.make(NatPtr(one));
threebis := Main.PeanoAbstractType.Nat.suc.make(NatPtr(twoter));

sevenplus :=  Main.PeanoAbstractType.Nat.plus.make(NatPtr(three),NatPtr(four));
sevenplusalt :=  Main.PeanoAbstractType.Nat.plus.make(NatPtr(two),NatPtr(five));

Put_Line("") ;

-- Verifying factory state
stats(factory.all) ; 

Put_Line("");
Put_Line("");

Put_Line(four.toString) ;

Put_Line(seven.toString) ; Put_Line(sevenplus.toString) ; Put_Line(sevenplusalt.toString) ;

end Alpha;

end Main; 

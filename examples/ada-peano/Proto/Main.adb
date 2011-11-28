    with Ada.Text_IO; use Ada.Text_IO;
   
    with main.PeanoAbstractType.Nat.plus ;
    use main.PeanoAbstractType.Nat.plus ; 
   
    with main.PeanoAbstractType.Nat.suc ; 
    use main.PeanoAbstractType.Nat.suc ;  
   
    with main.PeanoAbstractType.Nat.zero ; 
    use main.PeanoAbstractType.Nat.zero ;
    
    use main.PeanoAbstractType; 
    with SharedObjectFactoryP ; use SharedObjectFactoryP ;


    package body Main is 

	  void : aliased zero ;
	 
	  one : aliased suc ;
	  two : aliased suc ;
	  three : aliased suc ;
	  four : aliased suc ;
	  five : aliased suc ;
	  six : aliased suc ;
	  seven : aliased suc ;

	  twobis : aliased suc ; 
	  twoter : aliased suc ;

	  threebis : aliased suc ;

	  sevenplus : aliased plus ;
	  sevenplusalt : aliased plus ;

procedure Alpha is
begin

--Checking Factory
stats(factory.all) ;

-- Creating zero
void := Main.PeanoAbstractType.Nat.zero.make ;

-- Creating 1 to seven by suc
one := Main.PeanoAbstractType.Nat.suc.make(void'Access);
two :=  Main.PeanoAbstractType.Nat.suc.make(one'Access);
three :=  Main.PeanoAbstractType.Nat.suc.make(two'Access);
four :=  Main.PeanoAbstractType.Nat.suc.make(three'Access);
five :=  Main.PeanoAbstractType.Nat.suc.make(four'Access);
six :=  Main.PeanoAbstractType.Nat.suc.make(five'Access);
seven :=  Main.PeanoAbstractType.Nat.suc.make(six'Access);

-- Verifying that the table doesn't create the same object twice. 
--twobis :=  Main.PeanoAbstractType.Nat.suc.make(one);
--twoter :=  Main.PeanoAbstractType.Nat.suc.make(one);

--threebis :=  Main.PeanoAbstractType.Nat.suc.make(twobis);

sevenplus :=  Main.PeanoAbstractType.Nat.plus.make(three'Access,four'Access);
sevenplusalt :=  Main.PeanoAbstractType.Nat.plus.make(two'Access,five'Access);


Put_Line("Injection terminée") ;

-- Verifying factory state
stats(factory.all) ; 

Put_Line("");
Put_Line("");

Put_Line(four.toString) ;

Put_Line(seven.toString) ; Put_Line(sevenplus.toString) ; Put_Line(sevenplusalt.toString) ;

end Alpha;

end Main; 

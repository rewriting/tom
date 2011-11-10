    with Ada.Text_IO; use Ada.Text_IO;
    with main.PeanoAbstractType.Nat.plus ; 
    with main.PeanoAbstractType.Nat.suc ; 
    with main.PeanoAbstractType.Nat.zero ;
    use main.PeanoAbstractType.Nat;  


    package body Main is
    procedure Proto is  
    
    function tom_equal_term_Nat(t1:  Nat ; t2:  Nat ) return Boolean is 
    begin 
        return  (t1=t2) ; 
    end tom_equal_term_Nat; 

    function tom_is_sort_Nat(t:  Nat ) return Boolean is 
    begin 
        return  (t in Nat) ; 
    end tom_is_sort_Nat; 

    function tom_is_fun_sym_zero(t:  Nat ) return Boolean is 
    begin 
        return  (t in Nat.zero) ; 
    end tom_is_fun_sym_zero; 

    function tom_make_zero return  Nat'Class  is 
    begin 
        return  Nat.zero.make ; 
    end tom_make_zero; 

    function tom_is_fun_sym_suc(t:  Nat ) return Boolean is 
    begin 
        return  (t in Nat.suc) ; 
    end tom_is_fun_sym_suc; 

    function tom_make_suc(t0:  Nat ) return  Nat'Class  is 
    begin 
        return  Nat.suc.make(t0) ; 
    end tom_make_suc; 

    function tom_get_slot_suc_pred(t:  Nat ) return  Nat'Class  is 
    begin 
        return  t.getpred ; 
    end tom_get_slot_suc_pred; 

    function tom_is_fun_sym_plus(t:  Nat ) return Boolean is 
    begin 
        return  (t in Nat.plus) ; 
    end tom_is_fun_sym_plus; 

    function tom_make_plus(t0:  Nat ; t1:  Nat ) return  Nat'Class  is 
    begin 
        return  Nat.plus.make(t0, t1) ; 
    end tom_make_plus; 

    function tom_get_slot_plus_x1(t:  Nat ) return  Nat'Class  is 
    begin 
        return  t.getx1 ; 
    end tom_get_slot_plus_x1; 

    function tom_get_slot_plus_x2(t:  Nat ) return  Nat'Class  is 
    begin 
        return  t.getx2 ; 
    end tom_get_slot_plus_x2; 

    
    
    function evaluate(n: Nat) return Nat'Class is
    begin
    
    if  (n in Nat)  then 
        if tom_is_fun_sym_plus( Nat (n)) then 
            if tom_is_fun_sym_zero(tom_get_slot_plus_x2( Nat (n))) then 
                 return tom_get_slot_plus_x1( Nat (n))                ;             end if; 
        end if; 
    end if; 
    if  (n in Nat)  then 
        if tom_is_fun_sym_plus( Nat (n)) then 
            declare tomMatch1_7:  Nat :=tom_get_slot_plus_x2( Nat (n)); begin 
                if tom_is_fun_sym_suc(tomMatch1_7) then 
                     return                     tom_make_suc(tom_make_plus(tom_get_slot_plus_x1( Nat (n)),tom_get_slot_suc_pred(tomMatch1_7)))                    ;                 end if; 
            end; 
        end if; 
    end if; 
    
    return n;
    
    end evaluate;
    
    
    
    two: Nat := tom_make_plus(tom_make_suc(tom_make_zero),tom_make_suc(tom_make_zero));
    
    begin
    
    Put_Line(two.toString);
    two := evaluate(two);
    Put_Line(two.toString);
    
    
    end proto; 

 	begin 
	     Proto;
	end Main;	     



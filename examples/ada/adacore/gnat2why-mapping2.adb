        with Ada; use Ada;
        with Types; use Types;
        with Sinfo; use Sinfo;
        with Nlists; use Nlists;
        
        package body Gnat2Why.Mapping is
        
        function tom_equal_term_int(t1: Integer; t2: Integer) return Boolean is 
        begin 
            return  t1=t2 ; 
        end tom_equal_term_int; 

        function tom_is_sort_int(t: Integer) return Boolean is 
        begin 
            return  True ; 
        end tom_is_sort_int; 

        
        
        
        --------------------------------------
        -- Mapping
        --------------------------------------
        function tom_equal_term_Tom_Node_Kind(n1:  Integer ; n2:  Integer ) return Boolean is 
        begin 
            return  n1 = n2 ; 
        end tom_equal_term_Tom_Node_Kind; 

        function tom_is_sort_Tom_Node_Kind(n:  Integer ) return Boolean is 
        begin 
            return  True    ; 
        end tom_is_sort_Tom_Node_Kind; 

        
        
        --------------------------------------
        function tom_equal_term_Tom_Node_Id(n1:  Integer ; n2:  Integer ) return Boolean is 
        begin 
            return  n1 = n2 ; 
        end tom_equal_term_Tom_Node_Id; 

        function tom_is_sort_Tom_Node_Id(n:  Integer ) return Boolean is 
        begin 
            return  True    ; 
        end tom_is_sort_Tom_Node_Id; 

        function tom_is_fun_sym_Tom_Assignment_Node(t:  Integer ) return Boolean is 
        begin 
            return False; 
        end tom_is_fun_sym_Tom_Assignment_Node; 

        function tom_get_slot_Tom_Assignment_Node_n(n:  Integer ) return  Integer  is 
        begin 
            return  Integer ( n ) ; 
        end tom_get_slot_Tom_Assignment_Node_n; 

        function tom_get_slot_Tom_Assignment_Node_name(n:  Integer ) return  Integer  is 
        begin 
            return  Integer ( Sinfo.Name(Node_Id(n)) ) ; 
        end tom_get_slot_Tom_Assignment_Node_name; 

        function tom_get_slot_Tom_Assignment_Node_expr(n:  Integer ) return  Integer  is 
        begin 
            return  Integer ( Sinfo.Expression(Node_Id(n)) ) ; 
        end tom_get_slot_Tom_Assignment_Node_expr; 

        function tom_is_fun_sym_Tom_Op_Add_Node(t:  Integer ) return Boolean is 
        begin 
            return False; 
        end tom_is_fun_sym_Tom_Op_Add_Node; 

        function tom_get_slot_Tom_Op_Add_Node_n(n:  Integer ) return  Integer  is 
        begin 
            return  Integer ( n ) ; 
        end tom_get_slot_Tom_Op_Add_Node_n; 

        function tom_get_slot_Tom_Op_Add_Node_left(n:  Integer ) return  Integer  is 
        begin 
            return  Integer (  Left_Opnd(Node_Id(n)) ) ; 
        end tom_get_slot_Tom_Op_Add_Node_left; 

        function tom_get_slot_Tom_Op_Add_Node_right(n:  Integer ) return  Integer  is 
        begin 
            return  Integer (  Right_Opnd(Node_Id(n)) ) ; 
        end tom_get_slot_Tom_Op_Add_Node_right; 

        function tom_is_fun_sym_Tom_Loop_Statement_Node(t:  Integer ) return Boolean is 
        begin 
            return False; 
        end tom_is_fun_sym_Tom_Loop_Statement_Node; 

        function tom_get_slot_Tom_Loop_Statement_Node_n(n:  Integer ) return  Integer  is 
        begin 
            return  Integer ( n ) ; 
        end tom_get_slot_Tom_Loop_Statement_Node_n; 

        function tom_get_slot_Tom_Loop_Statement_Node_stmts(n:  Integer ) return  Integer  is 
        begin 
            return  Integer ( Statements(Node_Id(n)) ) ; 
        end tom_get_slot_Tom_Loop_Statement_Node_stmts; 

        
        
        
        --------------------------------------
        function tom_equal_term_Tom_List_Node_Id(l1:  Integer ; l2:  Integer ) return Boolean is 
        begin 
            return  l1 = l2 ; 
        end tom_equal_term_Tom_List_Node_Id; 

        function tom_is_fun_sym_List_Node_Id(l:  Integer ) return Boolean is 
        begin 
            return  true ; 
        end tom_is_fun_sym_List_Node_Id; 

        function tom_empty_list_List_Node_Id return  Integer  is 
        begin 
            return  Integer ( New_List ); 
        end tom_empty_list_List_Node_Id; 

        function tom_cons_list_List_Node_Id(n:  Integer ; l:  Integer ) return  Integer  is 
        begin 
            return  Integer ( Insert_List(Node_Id(n),List_Id(l)) ); 
        end tom_cons_list_List_Node_Id; 

        function tom_get_head_List_Node_Id_Tom_List_Node_Id(l:  Integer ) return  Integer  is 
        begin 
            return  Integer ( First(List_Id(l)) ) ; 
        end tom_get_head_List_Node_Id_Tom_List_Node_Id; 

        function tom_get_tail_List_Node_Id_Tom_List_Node_Id(l:  Integer ) return  Integer  is 
        begin 
            return  Integer ( Last(List_Id(l)) )   ; 
        end tom_get_tail_List_Node_Id_Tom_List_Node_Id; 

        function tom_is_empty_List_Node_Id_Tom_List_Node_Id(l:  Integer ) return Boolean is 
        begin 
            return  Boolean (Is_Empty_List(List_Id(l))); 
        end tom_is_empty_List_Node_Id_Tom_List_Node_Id; 

        function tom_append_list_List_Node_Id(l1:  Integer ; l2:  Integer ) return  Integer  is 
        begin 
            if tom_is_empty_List_Node_Id_Tom_List_Node_Id(l1) then 
                return l2; 
            elsif tom_is_empty_List_Node_Id_Tom_List_Node_Id(l2) then 
                return l1; 
            elsif tom_is_empty_List_Node_Id_Tom_List_Node_Id(tom_get_tail_List_Node_Id_Tom_List_Node_Id(l1)) then 
                return tom_cons_list_List_Node_Id(tom_get_head_List_Node_Id_Tom_List_Node_Id(l1),l2); 
            else 
                return tom_cons_list_List_Node_Id(tom_get_head_List_Node_Id_Tom_List_Node_Id(l1),tom_append_list_List_Node_Id(tom_get_tail_List_Node_Id_Tom_List_Node_Id(l1),l2)); 
            end if; 
        end tom_append_list_List_Node_Id; 

        function tom_get_slice_List_Node_Id(begining:  Integer ; ending:  Integer ; tail:  Integer ) return  Integer  is 
        begin 
            if tom_equal_term_Tom_List_Node_Id(begining,ending) then 
                return tail; 
            else 
                return tom_cons_list_List_Node_Id(tom_get_head_List_Node_Id_Tom_List_Node_Id(begining),tom_get_slice_List_Node_Id(tom_get_tail_List_Node_Id_Tom_List_Node_Id(begining),ending,tail)); 
            end if; 
        end tom_get_slice_List_Node_Id; 

        
        
        function Insert_List (N : Node_Id; L : List_Id) return List_Id is
        begin
        Append (Node_Or_Entity_Id (N), L);
        return L;
        end Insert_List;
        
        --------------------------------------
        -- Instance of Matching 
        --------------------------------------
        function Process_Node_Id (Arg : Node_Id) return Node_Id is
        begin
                declare tomMatch1_0: Address:=Integer(Arg); begin 
                    if  True     then 
                        if tom_is_fun_sym_Tom_Loop_Statement_Node(tomMatch1_0) then 
                            declare tomMatch1_2:  Integer :=tom_get_slot_Tom_Loop_Statement_Node_stmts(tomMatch1_0); begin 
                                if tom_is_fun_sym_List_Node_Id(tomMatch1_2) then 
                                    declare tomMatch1_end_7:  Integer :=tomMatch1_2; begin 
                                        loop 
                                            if not (tom_is_empty_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7)) then 
                                                declare tomMatch1_14:  Integer :=tom_get_head_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7); begin 
                                                    if tom_is_fun_sym_Tom_Assignment_Node(tomMatch1_14) then 
                                                        declare tomMatch1_13:  Integer :=tom_get_slot_Tom_Assignment_Node_expr(tomMatch1_14); begin 
                                                            declare tom_x:  Integer :=tom_get_slot_Tom_Assignment_Node_name(tomMatch1_14); begin 
                                                                if tom_is_fun_sym_Tom_Op_Add_Node(tomMatch1_13) then 
                                                                    if tom_equal_term_Tom_Node_Id(tom_x, tom_get_slot_Tom_Op_Add_Node_left(tomMatch1_13)) then 
                                                                        declare tomMatch1_8:  Integer :=tom_get_tail_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7); begin 
                                                                            if not (tom_is_empty_List_Node_Id_Tom_List_Node_Id(tomMatch1_8)) then 
                                                                                declare tomMatch1_22:  Integer :=tom_get_head_List_Node_Id_Tom_List_Node_Id(tomMatch1_8); begin 
                                                                                    if tom_is_fun_sym_Tom_Assignment_Node(tomMatch1_22) then 
                                                                                        declare tomMatch1_21:  Integer :=tom_get_slot_Tom_Assignment_Node_expr(tomMatch1_22); begin 
                                                                                            if tom_is_fun_sym_Tom_Op_Add_Node(tomMatch1_21) then 
                                                                                                if tom_equal_term_Tom_Node_Id(tom_x, tom_get_slot_Tom_Op_Add_Node_left(tomMatch1_21)) then 
                                                                                                    if tom_equal_term_Tom_Node_Id(tom_get_slot_Tom_Op_Add_Node_right(tomMatch1_13), tom_get_slot_Tom_Op_Add_Node_right(tomMatch1_21)) then 
                                                                                                        declare tomMatch1_31: Boolean:=False; begin 
                                                                                                            if tom_equal_term_Tom_Node_Id(tom_x, tom_get_slot_Tom_Assignment_Node_name(tomMatch1_22)) then 
                                                                                                                tomMatch1_31:=True; 
                                                                                                            end if; 
                                                                                                            if not (tomMatch1_31) then 
                                                                                                                
                                                                                                                return Node_Id(tom_get_slot_Tom_Loop_Statement_Node_n(tomMatch1_0));

                                                                                                            end if; 

                                                                                                        end; 
                                                                                                    end if; 
                                                                                                end if; 
                                                                                            end if; 
                                                                                        end; 
                                                                                    end if; 
                                                                                end; 
                                                                            end if; 
                                                                        end; 
                                                                    end if; 
                                                                end if; 
                                                            end; 
                                                        end; 
                                                    end if; 
                                                end; 
                                            end if; 
                                            if tom_is_empty_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7) then 
                                                tomMatch1_end_7:=tomMatch1_2; 
                                            else 
                                                tomMatch1_end_7:=tom_get_tail_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7); 
                                            end if; 

                                            exit when not(not (tom_equal_term_Tom_List_Node_Id(tomMatch1_end_7, tomMatch1_2))); 
                                        end loop; 
                                    end; 
                                end if; 
                            end; 
                        end if; 
                    end if; 
                end; 


        
        end Process_Node_Id;
        
        end Gnat2Why.Mapping;

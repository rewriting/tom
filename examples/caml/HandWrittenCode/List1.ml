open TomExtension;;
open TomList ;;

exception Res of int list

let rec swapsort l =
  try (
    (
      
      let tom_match1_1 = l in
	
	( (* match1_pattern1 *)
	  begin (* 1 *)
	    if (tom_cmp_fun_sym_TomList(tom_get_fun_sym_TomList(tom_match1_1) ,  "conc" )) 
	    then 
	      begin
		let tom_match1_1_list1  = ref tom_match1_1 in
		let tom_match1_1_begin1 = ref tom_match1_1 in
		let tom_match1_1_end1   = ref tom_match1_1 in
		  while (not(tom_is_empty_TomList(!tom_match1_1_list1)))
		  do
		    let x1 = tom_get_slice_conc(!tom_match1_1_begin1,!tom_match1_1_end1)
		    in
		      begin  
			(if   (not(tom_is_empty_TomList(!tom_match1_1_list1)))
			 then 
			   let x = tom_get_head_TomList(!tom_match1_1_list1)
			   in 
			     begin 
			       tom_match1_1_list1 := tom_get_tail_TomList(!tom_match1_1_list1) ;
			       if   (not(tom_is_empty_TomList(!tom_match1_1_list1)))
			       then 
				 let y = tom_get_head_TomList(!tom_match1_1_list1)
				 in 
				   begin
				     tom_match1_1_list1 :=  tom_get_tail_TomList(!tom_match1_1_list1);
				     let x2 = !tom_match1_1_list1 
				     in
				       if (x>y) 
				       then raise (Res ( swapsort(tom_insert_list_conc(
						      x1, tom_make_insert_conc(
							y, tom_make_insert_conc(
							  x, tom_insert_list_conc(
							    x2, tom_make_empty_conc()
							  ) ) ) )) ))
				   end
			     end);
			(if (not(tom_is_empty_TomList(!tom_match1_1_end1)))
			 then tom_match1_1_end1 :=  tom_get_tail_TomList(!tom_match1_1_end1));
			tom_match1_1_list1 :=  !tom_match1_1_end1
		      end
		  done
	      end
	  end (* 1 *)
	)
	;
	l
    )
  )
  with Res r -> r
    
 

let rec string_of_int_list = function [] -> "\n"
  | i :: r -> (string_of_int i) ^ " " ^ (string_of_int_list r);;


print_string "Bonjour ;-)\n";;
let l1 = [10;9;8;7;6;5;];;
print_string(string_of_int_list l1);;
let l2 = swapsort(l1);;
print_string(string_of_int_list l1);;
print_string(string_of_int_list l2);;







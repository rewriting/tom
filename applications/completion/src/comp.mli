exception Res of Datatypes.tableau list

exception Res_eq_list of Datatypes.eq_list

exception Not_match

val pmatch : Datatypes.eq list -> Datatypes.eq_list

val unify : Datatypes.eq list -> Datatypes.eq_list

val rewrite :
  Datatypes.eq ->
  Datatypes.eq_list -> Datatypes.prop -> Datatypes.prop * Datatypes.eq_list

exception Solved

val closure : Datatypes.tableau -> unit

val tableau :
  Datatypes.eq list -> Datatypes.branch list -> Datatypes.branch list list

val unify_max : Datatypes.tableau -> Datatypes.tableau list

val sort_result :
  Datatypes.prop ->
  Datatypes.tableau ->
  Datatypes.branch list

exception Res_tab of Datatypes.tableau

val clean : Datatypes.branch list -> Datatypes.branch list

val critic : Datatypes.eq -> Datatypes.eq -> 
  Datatypes.tableau * Datatypes.tableau * Datatypes.prop

val get_rules : Datatypes.branch list -> Datatypes.eq list

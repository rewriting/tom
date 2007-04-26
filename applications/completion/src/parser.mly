%{
  let parse_error = Interp.error_handling
%}
%token EOL PIPE DASH COMMA LPAR RPAR AND OR DOT ARROW ALL EX IMP
%token TAB RULES QUIT UNIF ADDRULES COMP
%token <string> STRING
%right OR
%right AND 
%right IMP
%start rprop
%type <Datatypes.prop> rprop
%start rterm
%type <Datatypes.term> rterm
%start rbranch
%type <Datatypes.branch> rbranch
%start rtableau
%type <Datatypes.tableau> rtableau
%start req
%type <Datatypes.eq> req
%start command
%type <unit> command
%%
rprop:
  prop EOL { $1 }

rterm:
  term EOL { $1 }

rbranch:
  branch EOL { $1 }

rtableau:
  tableau EOL { $1 }

req:
  eq EOL { $1 }

prop:
  smallprop AND smallprop {Datatypes.And($1,$3) } 
| smallprop OR smallprop {Datatypes.Or($1,$3) } 
| smallprop IMP smallprop { Datatypes.Or(Datatypes.Not($1),$3) } /* classical */
| smallprop { $1 }
| ALL strings DOT prop { List.fold_left 
			   (fun a e -> Datatypes.All(e,a))
			   $4 $2
		       }
| EX strings DOT prop { List.fold_left 
			   (fun a e -> Datatypes.Ex(e,a))
			   $4 $2 }

strings:
  STRING { [$1] }
| STRING strings { $1::$2 }

smallprop:
  STRING { Datatypes.Atomic($1,[]) }
| STRING LPAR terms RPAR { Datatypes.Atomic($1,$3) }
| DASH smallprop { Datatypes.Not $2 }
| LPAR prop RPAR { $2 }


terms:
  term { [$1] } 
| term COMMA terms { $1::$3 }

term:
  STRING { Datatypes.Var $1 }
| STRING DOT { Datatypes.Fun($1,[]) }
| STRING LPAR terms RPAR { Datatypes.Fun($1,$3) }

branch:
  prop { [$1,([],[],[])],(true,true) }
| prop COMMA branch { let s,_ = $3 in ($1,([],[],[]))::s,(true, true) }

tableau:
  branch { [$1] }
| branch PIPE tableau { $1::$3 }

eq:
  term ARROW term { Datatypes.Term($1,$3) }
| prop DASH ARROW prop { Datatypes.Prop($1,$4) }

command:
  RULES rules EOL { Interp.rules := $2 }
| TAB tableau EOL { Interp.solve_and_print $2 }
| QUIT { raise Interp.Eof }
| UNIF rules EOL { Interp.unif $2 }
| UNIF tableau EOL { Interp.bunif $2 }
| COMP tableau EOL { Interp.comp $2 }
| COMP EOL { Interp.critical_proofs !Interp.rules }
| ADDRULES rules EOL { Interp.rules := List.rev_append $2 !Interp.rules }
| EOL { () }

rules:
  eq { [$1] }
| eq COMMA rules { $1::$3 }

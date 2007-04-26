{
  open Parser        (* The type token is defined in parser.mli *)

  let keyword_table = Hashtbl.create 10
  let _ =
    List.iter (fun (kwd, tok) -> Hashtbl.add keyword_table kwd tok)
      [ "rules", RULES;
	"quit", QUIT;
	"comp", COMP;
	"unif", UNIF;
	"addrules", ADDRULES;
	"tab", TAB ]

}
rule token = parse
    [' ' '\t']     { token lexbuf }     (* skip blanks *)
  | ['\n' ]        { let p = lexbuf.Lexing.lex_curr_p in
		       lexbuf.Lexing.lex_curr_p <- 
		       { p with
			   Lexing.pos_lnum = p.Lexing.pos_lnum + 1;
			   Lexing.pos_bol = p.Lexing.pos_cnum };
		     EOL }
  | '%'[^'\n']*'\n'
      { let p = lexbuf.Lexing.lex_curr_p in
	  lexbuf.Lexing.lex_curr_p <- 
	    { p with
		Lexing.pos_lnum = p.Lexing.pos_lnum + 1;
		Lexing.pos_bol = p.Lexing.pos_cnum };
	  token lexbuf }     (* skip blanks *)
  | eof            { QUIT }
  | '-'            { DASH }
  | "->"         { ARROW }
  | '.'            { DOT }
  | '|'            { PIPE }
  | ','            { COMMA }
  | '('            { LPAR }
  | ')'            { RPAR }
  | '/''\\'        { AND }
  | '\\''/'        { OR }
  | "=>"            { IMP }
  | "ALL"          { ALL }
  | "EX"           { EX }
  | ['a'-'z' 'A'-'Z']['a'-'z' 'A'-'Z' '_' '0'-'9']* as lxm
                   { try
                       Hashtbl.find keyword_table lxm
                     with Not_found ->
		       STRING lxm }

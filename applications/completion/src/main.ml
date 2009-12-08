open Datatypes

let _ =
  let lexbuf = Lexing.from_channel !Globals.in_chan in
    try
      while true do
	print_string "\n> "; flush stdout;
	try
	  Parser.command Lexer.token lexbuf
	with Parsing.Parse_error -> ()
	  | Failure "lexing: empty token" -> 
	      Interp.error_handling "unknown identifier"
      done
    with Interp.Eof -> exit 0

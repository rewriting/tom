<YYINITIAL> {
  "%typeterm"                    { return sym(Terminals.TYPEMAPPING); }
  "%op"                    { return sym(Terminals.OPMAPPING); }
  "`"                 { return sym(Terminals.BACKQUOTE); }
  "%match"                    { return sym(Terminals.MATCH); }
  "->" { return sym(Terminals.ARROW); }
}


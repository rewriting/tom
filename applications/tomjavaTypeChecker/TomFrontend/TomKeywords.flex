<YYINITIAL> {
  "%typeterm"                    { return sym(Terminals.TYPEMAPPING); }
  "is_sort"                    { return sym(Terminals.ISSORT); }
  "implement"                    { return sym(Terminals.IMPLEMENT); }
  "%op"                    { return sym(Terminals.OPMAPPING); }
  "`"                 { return sym(Terminals.BACKQUOTE); }
  "%match"                    { return sym(Terminals.MATCH); }
  "->" { return sym(Terminals.ARROW); }
}


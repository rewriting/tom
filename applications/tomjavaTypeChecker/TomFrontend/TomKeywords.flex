<YYINITIAL> {
  "%typeterm"                    { return sym(Terminals.TYPEMAPPING); }
  "implement"                    { return sym(Terminals.IMPLEMENT); }
  "make"                    { return sym(Terminals.MAKE); }
  "is_fsym"                    { return sym(Terminals.ISFSYM); }
  "get_slot"                    { return sym(Terminals.GETSLOT); }
  "%op"                    { return sym(Terminals.OPMAPPING); }
  "`"                 { return sym(Terminals.BACKQUOTE); }
  "%match"                    { return sym(Terminals.MATCH); }
  "->" { return sym(Terminals.ARROW); }
}


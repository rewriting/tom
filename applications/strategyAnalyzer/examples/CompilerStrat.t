abstract syntax
Code = Match(TermList) | Assign(Name,Exp) | If(Exp,Code,Code) | Nop()
TermList = ConsTerm(Term,TermList) | NilTerm()
Term = VarTerm(Name) | ApplTerm(Name,TermList)
Nat = Z() | S(Nat)
Name = Name(Nat)

strategies

compile() = [ Match(l) -> Nop() ]
rename() = [ Var(Name(n)) -> Var(Name(S(n))) ]

topdown(s) = mu x.(s ; all(x))
topdownstoponsucces(s) = mu x.(s <+ all(x))

//innermost(s) = mu x.(all(x) ; ((s ; x) <+ Identity))

mainStrat() = topdown(compile()) ; topdownstoponsucces(rename())


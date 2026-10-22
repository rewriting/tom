# Phase 4.F.10 — Patterns annotés `pat @ name`

Extension de `parsePattern` pour reconnaître la forme `pat '@' ID` qui
attache une contrainte `AliasTo(Variable(Name(ID), …))` au pattern (au
lieu d'un `concConstraint()` vide). C'est la notation TOM pour
nommer un sous-terme matché — utile pour récupérer la valeur d'une
position sans devoir la mentionner explicitement dans le body.

---

## 1. Spec exécutable (référence Java)

CST (`CstBuilder.java:448-449`) :
```java
if(ctx.AT() != null) {
    res = Cst_AnnotatedPattern.make((CstPattern)getValue(ctx.pattern()), ctx.ID().getText());
}
```

Lowering (`AstBuilder.java:817-830` + `ASTFactory.java:285`) :
```java
TomTerm pattern = convert(cst.getpattern());
int line = 0;
Constraint constraint = ASTFactory.makeAliasTo(
    Name.make(cst.getannotation()), line, "unknown file");
// makeAliasTo builds:
//   AliasTo(Variable(
//     concOption(OriginTracking(Name(annot), line=0, fileName="unknown file")),
//     Name(annot),
//     TYPE_UNKNOWN,
//     EmptyconcConstraint))

// pattern.setConstraints(ConsconcConstraint.make(constraint,
//     existingConstraints + EmptyconcConstraint))
return pattern.setConstraints(...);
```

Notes :
- Les positions `(line=0, file="unknown file")` sont **délibérément des
  placeholders**. Le typer les remplace plus tard par la position réelle.
- Java accepte `pat@name` sur les variantes `TermAppl`, `RecordAppl`,
  `Variable`, `VariableStar`. Pas sur les patterns anti — qui ont leur
  propre branche AstBuilder.java:826-830.

---

## 2. Implémentation Go

Refactor : `parsePattern` se sépare en deux fonctions :
1. `parseBasePattern()` — l'ancien corps, qui produit le pattern brut
   (Variable, VariableStar ou TermAppl).
2. `parsePattern()` = `parseBasePattern() + optional '@' ID`.

```go
func (p *parser) parsePattern() (tomast.TomTerm, error) {
    base, err := p.parseBasePattern()
    if err != nil { return nil, err }
    save := p.idx; saveCur := p.cur
    p.skipBlankInline()
    if p.atEnd() || p.peek(0) != '@' {
        p.idx = save; p.cur = saveCur
        return base, nil
    }
    p.advance() // '@'
    p.skipBlankInline()
    name, err := p.readIdent()
    if err != nil { return nil, fmt.Errorf("after '@': %w", err) }
    aliasVar := tomast.MakeVariable(
        tomast.MakeConcOption(tomast.MakeOriginTracking(
            tomast.MakeName(name), 0, "unknown file",
        )),
        tomast.MakeName(name),
        unknownType(),
        tomast.MakeConcConstraint(),
    )
    alias := tomast.MakeAliasTo(aliasVar)
    return addPatternConstraint(base, alias)
}
```

`addPatternConstraint(pat, c)` reconstruit le pattern avec la contrainte
ajoutée, par discrimination de type :

```go
func addPatternConstraint(pat tomast.TomTerm, c tomast.Constraint) (tomast.TomTerm, error) {
    cl := tomast.MakeConcConstraint(c)
    switch v := pat.(type) {
    case *tomast.VariableTomTerm:
        return tomast.MakeVariable(v.Options, v.AstName, v.AstType, cl), nil
    case *tomast.VariableStarTomTerm:
        return tomast.MakeVariableStar(v.Options, v.AstName, v.AstType, cl), nil
    case *tomast.TermApplTomTerm:
        return tomast.MakeTermAppl(v.Options, v.NameList, v.Args, cl), nil
    default:
        return nil, fmt.Errorf("cannot attach '@' annotation to %T", pat)
    }
}
```

Le clean-rebuild est correct ici car parseBasePattern produit toujours
des patterns avec `concConstraint()` vide ; "prepend constraint to empty
list" = "single-element list with this constraint". Quand on supportera
le `@` sur un pattern qui porte déjà des contraintes (ex. `pat@a@b`,
non géré pour le moment), il faudra étendre `addPatternConstraint` pour
préserver les contraintes existantes.

Grammaire en doc-comment :
```
pattern    : basePattern ('@' ID)?
basePattern: '_' '*'? | ID '*'? | ID '(' (pattern (',' pattern)*)? ')'
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0k_annot/scenario.t` exerce une annotation
  sur une **sous-position** d'une application — pour stresser la
  récursion :
  ```
  public class Match0k {
    public void f(Object t) {
      %match(t) {
        Foo(x@a) -> { }
      }
    }
  }
  ```
- AST produit pour le pattern `Foo(x@a)` :
  ```
  TermAppl(
    concOption(),
    concTomName(Name("Foo")),
    concTomTerm(
      Variable(
        concOption(),
        Name("x"),
        unknownType,
        concConstraint(
          AliasTo(Variable(
            concOption(OriginTracking(Name("a"),0,"unknown file")),
            Name("a"),
            unknownType,
            concConstraint()
          ))
        )
      )
    ),
    concConstraint()
  )
  ```
- `TestParseMatch0kAnnot` épingle l'AST byte-pour-byte.
- Entrée `"match0k_annot"` ajoutée au harnais cross-language.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
=== RUN   TestParseMatch0b           --- PASS
=== RUN   TestParseMatch0cNamed      --- PASS
=== RUN   TestParseMatch0dAppl       --- PASS
=== RUN   TestParseMatch0eApplArgs   --- PASS
=== RUN   TestParseMatch0fMulti      --- PASS
=== RUN   TestParseMatch0gRules      --- PASS
=== RUN   TestParseMatch0hBody       --- PASS
=== RUN   TestParseMatch0iExplicit   --- PASS
=== RUN   TestParseMatch0jStar       --- PASS
=== RUN   TestParseMatch0kAnnot      --- PASS
```

Tous les autres paquets verts.

---

## 5. Couverture courante du parser

**17 fixtures** validées (1 ajoutée) — `%match` désormais couvert pour :
patterns `_`, `_*`, `x`, `x*`, `Foo(...)`, `pat@name` (sous-pattern
inclus) ; sujets implicites + explicites `<<` ; multi-sujets ;
multi-rules ; body vide ou non-vide.

---

## 6. Suite logique

1. **Pattern anti `!pat`** (Cst_Anti, cf. `AstBuilder.java:780-792`) —
   `AntiTerm(pattern)`.
2. **Pattern OR `Foo() | Bar()`** (Cst_ConstantOr, cf.
   `AstBuilder.java:774-779`) — `TermAppl(opts, concTomName(Name("Foo"),
   Name("Bar")), concTomTerm(), concConstraint())`.
3. **Type annotation** sur le RHS du `<<` : `pattern << bqterm:Type`.
4. **AND/OR chaining** de contraintes (`&&` / `||`).
5. **Annotations multiples / sur pattern non-vierge** : nécessite
   d'étendre `addPatternConstraint` pour PRÉSERVER les contraintes
   existantes (au lieu de les remplacer).
6. **`%match` imbriqué** dans un body.

Au-delà : `%strategy`, `%gom`, backquote terms, metaquote.

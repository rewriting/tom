# Phase 4.F.9 — Patterns variable-star `x*` et `_*`

Extension de `parsePattern` pour reconnaître le suffixe `*` après un
identifier ou un wildcard, produisant un `VariableStar` au lieu d'un
`Variable`. C'est la notation TOM pour les patterns « zero-or-more »
(typiques sur les listes variadiques).

---

## 1. Spec exécutable (référence Java)

`stable/tom/engine/parser/antlr4/CstBuilder.java:456-463` — la grammaire
distingue 4 cas selon les flags `var`/`UNDERSCORE` + présence/absence
de `STAR` :

```java
} else if(ctx.var != null && ctx.STAR() == null) {
  res = Cst_Variable.make(ctx.var.getText())         // x
} else if(ctx.var != null && ctx.STAR() != null) {
  res = Cst_VariableStar.make(ctx.var.getText())     // x*
} else if(ctx.UNDERSCORE() != null && ctx.STAR() == null) {
  res = Cst_UnamedVariable.make()                    // _
} else if(ctx.UNDERSCORE() != null && ctx.STAR() != null) {
  res = Cst_UnamedVariableStar.make()                // _*
}
```

`AstBuilder.java:752-766` — chaque CST est abaissée :

```java
// Cst_Variable
return Variable.make(EmptyconcOption, Name(varname), TYPE_UNKNOWN,
                     EmptyconcConstraint);

// Cst_VariableStar
return VariableStar.make(EmptyconcOption, Name(varname), TYPE_UNKNOWN,
                         EmptyconcConstraint);

// Cst_UnamedVariable
return Variable.make(EmptyconcOption, EmptyName(), TYPE_UNKNOWN,
                     EmptyconcConstraint);

// Cst_UnamedVariableStar
return VariableStar.make(EmptyconcOption, EmptyName(), TYPE_UNKNOWN,
                         EmptyconcConstraint);
```

Donc `x*` ↔ `Variable(... Name("x") ...)` modulo le constructeur,
et `_*` ↔ `Variable(... EmptyName() ...)` idem.

Une application `Foo(...)` ne porte pas de `*` (la grammaire ne
l'autorise pas).

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — branche `_`/`_*` enrichie :

```go
if p.peek(0) == '_' && !isIdentChar(p.peek(1)) {
    p.advance() // '_'
    if !p.atEnd() && p.peek(0) == '*' {
        p.advance() // '*'
        return tomast.MakeVariableStar(
            tomast.MakeConcOption(),
            tomast.MakeEmptyName(),
            unknownType(),
            tomast.MakeConcConstraint(),
        ), nil
    }
    return tomast.MakeVariable(/* EmptyName */)
}
```

Et après lecture d'un `ID`, on guette d'abord `(` (application), puis
`*` (VariableStar), sinon fallback variable :

```go
save := p.idx; saveCur := p.cur
p.skipBlankInline()
if !p.atEnd() && p.peek(0) == '(' { /* application */ }
if !p.atEnd() && p.peek(0) == '*' {
    p.advance() // '*'
    return tomast.MakeVariableStar(
        tomast.MakeConcOption(),
        tomast.MakeName(name),
        unknownType(),
        tomast.MakeConcConstraint(),
    ), nil
}
p.idx = save; p.cur = saveCur
return tomast.MakeVariable(/* Name(name) */)
```

La fenêtre `skipBlankInline` avant le `*` tolère `x *` au cas où le
source aurait un espace (cohérent avec l'hidden-channel ANTLR).

Grammaire en doc-comment :
```
pattern : '_' '*'? | ID '*'? | ID '(' (pattern (',' pattern)*)? ')'
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0j_star/scenario.t` exerce les deux
  formes en multi-sujets pour amortir la fixture :
  ```
  public class Match0j {
    public void f(Object a, Object b) {
      %match(a, b) {
        x*, _* -> { }
      }
    }
  }
  ```
- Contraintes générées :
  ```
  AndConstraint(
    MatchConstraint(VariableStar(concOption(),Name("x"),...), BQVariable(...a...), T),
    MatchConstraint(VariableStar(concOption(),EmptyName(),...), BQVariable(...b...), T)
  )
  ```
- `TestParseMatch0jStar` épingle l'AST.
- Entrée `"match0j_star"` ajoutée au harnais cross-language.

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
ok    tom/tomgo/internal/tomparser    0.37s
```

Tous les autres paquets verts.

---

## 5. Couverture courante du parser

**16 fixtures** validées (1 ajoutée) :

| Fixture              | Construction validée                                                  |
| -------------------- | --------------------------------------------------------------------- |
| `skeleton`           | `%typeterm` minimal                                                   |
| `op_noargs`          | `%op` sans slots                                                      |
| `op_slots`           | `%op` avec slots                                                      |
| `typeterm_extends`   | `%typeterm X extends Y`                                               |
| `oplist_oparray`     | `%oplist` / `%oparray`                                                |
| `include_local`      | `%include`                                                            |
| `water_multi`        | water ANTLR-fidèle (3 visibles)                                       |
| `match0b`            | `%match` minimal — wildcard `_`                                       |
| `match0c_named`      | `%match` — variable nommée `x`                                        |
| `match0d_appl`       | `%match` — application nullaire `Foo()`                               |
| `match0e_appl_args`  | `%match` — application avec sous-patterns `Foo(x, Bar())`             |
| `match0f_multi`      | `%match` — multi-sujets                                               |
| `match0g_rules`      | `%match` — plusieurs rules                                            |
| `match0h_body`       | `%match` — body non-vide                                              |
| `match0i_explicit`   | `%match` — sujet explicite `<<`                                       |
| `match0j_star`       | `%match` — variable-star `x*`, `_*` **(nouveau)**                     |

---

## 6. Suite logique

Reste pour `%match` :
1. **Pattern annoté `pat@name`** (Cst_AnnotatedPattern, cf.
   `AstBuilder.java:817-830`) — ajoute une `AliasTo` constraint au
   pattern.
2. **Pattern anti `!pat`** (Cst_Anti, cf. `AstBuilder.java:780-792`) —
   `AntiTerm(pattern)`.
3. **Pattern OR `Foo() | Bar()`** (Cst_ConstantOr, cf.
   `AstBuilder.java:774-779`) — `TermAppl(opts, concTomName(Name("Foo"),
   Name("Bar")), concTomTerm(), concConstraint())`.
4. **Type annotation** sur le RHS du `<<` : `pattern << bqterm:Type`.
5. **AND/OR chaining** de contraintes (`&&` / `||`).
6. **`%match` imbriqué** dans un body.

Au-delà du `%match`, autres îlots :
- `%strategy ... extends ... { visit Sort { … } }`.
- `%gom { … }` inline.
- Backquote terms `` `Op(args) ``.
- Metaquote `%[ … ]%`.

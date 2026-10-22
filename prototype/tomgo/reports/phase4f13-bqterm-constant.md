# Phase 4.F.13 — Backquote constant `` `Foo() `` sur RHS de `<<`

Première étape vers la prise en charge des **backquote terms** côté
parser. Extension de la 4.F.8 (sujet explicite `<<`) : le RHS accepte
désormais une application backquote constante `` `Foo() `` en plus
du bare ID — production d'un `BQAppl(opts, Name(F), concBQTerm())`.

Le sujet de progression : commencer par la forme **la plus contenue**
(un seul nœud bqterm en RHS d'un constraint explicite, sans imbrication
dans le body, sans semantic de scope), de manière à valider l'AST
byte-pour-byte avant d'attaquer les cas plus difficiles (backquote
dans le body, scope, mode-island, etc.).

---

## 1. Spec exécutable (référence Java)

Grammaire ANTLR (`TomIslandParser.g4:122-128`) :
```antlr
bqterm
  : codomain=ID? BQUOTE? fsym=ID LPAREN (bqterm (COMMA bqterm)*)? RPAREN
  | codomain=ID? BQUOTE? fsym=ID LSQUAREBR (pairSlotBqterm (COMMA …)*)? RSQUAREBR
  | codomain=ID? BQUOTE? var=ID STAR?
  | codomain=ID? constant
  | UNDERSCORE
```

Note importante : le `BQUOTE?` est **optionnel**. `\`Foo()` et `Foo()`
sont syntaxiquement équivalents au niveau du bqterm.

CST (`CstBuilder.java:327-351`) produit `Cst_BQAppl(opts, name="Foo",
bqTermList=[])` pour `\`Foo()` sans args.

AST (`AstBuilder.java:551-554`) :
```java
return BQAppl.make(
  addDefaultModule(convert(optionList, name)),  // = concOption(OT(Name(name), line, file), ModuleName("default"))
  Name.make(name),
  convert(bqTermList)
);
```

`addDefaultModule(opts)` injecte `ModuleName("default")` si absent ;
`convert(optionList, name)` extrait/construit l'`OriginTracking`
positionné sur le symbole. Pour `\`Foo()` à la ligne 4 :
```
BQAppl(
  concOption(OriginTracking(Name("Foo"), 4, file), ModuleName("default")),
  Name("Foo"),
  concBQTerm()
)
```

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — deux nouvelles fonctions :

```go
// parseBQTerm : optionnel '`', puis ID, puis dispatch sur '(' ou rien.
func (p *parser) parseBQTerm() (tomast.BQTerm, error) {
    startLine := p.cur.line
    if !p.atEnd() && p.peek(0) == '`' {
        p.advance() // '`'
        p.skipBlankInline()
    }
    if p.atEnd() || !isIdentStart(p.peek(0)) {
        return nil, fmt.Errorf("expected bqterm identifier at %s", p.cur)
    }
    name, err := p.readIdent()
    if err != nil { return nil, err }
    options := tomast.MakeConcOption(
        tomast.MakeOriginTracking(tomast.MakeName(name), int64(startLine), p.filename),
        tomast.MakeModuleName("default"),
    )
    save := p.idx; saveCur := p.cur
    p.skipBlankInline()
    if !p.atEnd() && p.peek(0) == '(' {
        p.advance() // '('
        args, err := p.parseBQTermArgList()
        if err != nil { return nil, err }
        if p.atEnd() || p.peek(0) != ')' {
            return nil, fmt.Errorf("expected ')' to close bqterm application at %s", p.cur)
        }
        p.advance() // ')'
        return tomast.MakeBQAppl(options, tomast.MakeName(name), tomast.MakeConcBQTerm(args...)), nil
    }
    p.idx = save; p.cur = saveCur
    return tomast.MakeBQVariable(options, tomast.MakeName(name), unknownType()), nil
}

// parseBQTermArgList : bqterm (',' bqterm)* entre '(' et ')', récursion.
func (p *parser) parseBQTermArgList() ([]tomast.BQTerm, error) { … }
```

Le call-site dans `parseActionRule` change de `parseSubject` à
`parseBQTerm` :

```go
if !p.atEnd() && p.peek(0) == '<' && p.peek(1) == '<' {
    p.advance(); p.advance() // '<<'
    p.skipBlankInline()
    bq, err := p.parseBQTerm()
    // …
}
```

Notes :
- Le `BQUOTE` est consommé silencieusement (optionnel).
- `parseSubject` reste utilisé pour les sujets parens-implicites de
  `%match(...)`, où le bqterm est limité à un bare ID dans nos
  fixtures actuelles. À unifier plus tard.

**Limites volontaires** (à lever phase par phase) :
- Pas de `codomain=ID:Type` prefix.
- Pas de records `Foo[a=x, b=y]` (Cst_BQRecordAppl).
- Pas de `BQVariableStar` `x*`.
- Pas de constantes pures (Cst_BQConstant : `Cst_BQConstant.make(symbol)`).
- Pas de composite ITL.
- Pas de portée backquote dans le body d'action (`{ return \`x; }`).

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0n_bqappl/scenario.t` :
  ```
  public class Match0n {
    public void f(Object t) {
      %match(t) {
        x << `Foo() -> { }
      }
    }
  }
  ```
- AST attendu pour le RHS `\`Foo()` :
  ```
  BQAppl(
    concOption(OriginTracking(Name("Foo"),4,"__INPUT__"),ModuleName("default")),
    Name("Foo"),
    concBQTerm()
  )
  ```
- `TestParseMatch0nBqAppl` épingle l'AST complet.
- Entrée `"match0n_bqappl"` ajoutée au harnais cross-language.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
... 13 tests, tous PASS
ok    tom/tomgo/internal/tomparser    0.31s
```

Tous les autres paquets verts (`backend`, `equivtest`, `gom`,
`gomast`, `tomparseq`, `sharedobjects`, `cmd/tomgo`).

---

## 5. Couverture courante du parser

**20 fixtures** validées (1 ajoutée). Le RHS d'un `<<` accepte
maintenant `\`Id` (BQVariable) et `\`Id(args)` (BQAppl récursif).

---

## 6. Suite logique (backquote)

Par ordre d'effort croissant, en restant sur le RHS de `<<` (contexte
contenu) :

1. **`\`Id(args)` avec args bqterm** non vides — exercer la récursion
   `parseBQTermArgList` (typiquement `x << \`f(\`a(), \`b())`).
2. **`\`x` au lieu de bare `x`** sur RHS (la grammaire ANTLR accepte
   `BQUOTE?` donc déjà supporté par `parseBQTerm`).
3. **`\`x*`** (`BQVariableStar`) sur RHS.
4. **Type annotation `\`bqterm:Type`** (codomain optionnel) ou
   `pat << bqterm:Type`.
5. **`\`Foo[a=v, b=w]`** records (Cst_BQRecordAppl).

Puis, contexte plus large :

6. **Backquote dans le body d'action** : `lowerActionBody` doit
   switcher en mode-island à l'apparition d'un `\``, parser le bqterm
   en `Composite(CompositeTL("…"), CompositeBQTerm(...), CompositeTL("…"))`
   (cf. `AstBuilder.java:583-595` — `Cst_BQComposite`).
7. **Backquote comme sujet de `%match`** (déjà partiellement supporté
   via `parseSubject`).
8. **`%strategy ... visit Sort { … }`** body.

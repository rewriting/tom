# Phase 4.F.11 — Patterns anti `!pat`

Ajout de la reconnaissance du préfixe `!` devant un pattern : `!pat`
est abaissé en `AntiTerm(pat)`. C'est la notation TOM pour matcher
*tout ce qui n'est pas* `pat`.

---

## 1. Spec exécutable (référence Java)

CST (`CstBuilder.java:450-451`) :
```java
} else if(ctx.ANTI() != null) {
    res = Cst_Anti.make((CstPattern)getValue(ctx.pattern()));
}
```

Lowering (`AstBuilder.java:780-792`) :
```java
} else if (cst instanceof Cst_Anti) {
    return AntiTerm.make(convert(cst.getpattern()));
}
```

Donc `!pat` → `AntiTerm(convert(pat))`. Aucun traitement spécial sur
les options ou les contraintes — le `AntiTerm` enveloppe simplement
le pattern inner.

Le `!` peut s'enchaîner avec d'autres constructions : `!Foo(x@a)`
donnerait `AntiTerm(TermAppl(... avec x@a en sous-position ...))`.

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — au début de `parsePattern`, je guette
`!` (avec garde `peek(1) != '='` pour éviter de capturer `!=` quand
viendront les contraintes numériques) :

```go
func (p *parser) parsePattern() (tomast.TomTerm, error) {
    if !p.atEnd() && p.peek(0) == '!' && p.peek(1) != '=' {
        p.advance() // '!'
        p.skipBlankInline()
        inner, err := p.parsePattern()
        if err != nil {
            return nil, fmt.Errorf("after '!': %w", err)
        }
        return tomast.MakeAntiTerm(inner), nil
    }
    base, err := p.parseBasePattern()
    // … (annotation `@` optionnelle) …
}
```

La récursion via `parsePattern` (et non `parseBasePattern`) permet
d'enchaîner avec les autres constructions de pattern, notamment :

- `!!pat` — double anti, théoriquement légal (équivalent à `pat`,
  mais syntaxiquement reconnu).
- `!pat@name` — anti d'un pattern annoté (le `!` est consommé en
  premier, puis le recursive `parsePattern` reconstruit l'annotation
  sur l'inner, puis on wrappe le tout dans AntiTerm).

Grammaire en doc-comment :
```
pattern    : '!' pattern | basePattern ('@' ID)?
basePattern: '_' '*'? | ID '*'? | ID '(' (pattern (',' pattern)*)? ')'
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0l_anti/scenario.t` :
  ```
  public class Match0l {
    public void f(Object t) {
      %match(t) {
        !Foo() -> { }
      }
    }
  }
  ```
- AST produit pour le pattern `!Foo()` :
  ```
  AntiTerm(TermAppl(concOption(),concTomName(Name("Foo")),concTomTerm(),concConstraint()))
  ```
- `TestParseMatch0lAnti` épingle l'AST.
- Entrée `"match0l_anti"` ajoutée au harnais cross-language.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
... 11 tests, tous PASS
ok    tom/tomgo/internal/tomparser    0.32s
```

Tous les autres paquets verts (`backend`, `equivtest`, `gom`,
`gomast`, `tomparseq`, `sharedobjects`, `cmd/tomgo`).

---

## 5. Couverture courante du parser

**17 fixtures** validées (1 ajoutée) — `%match` couvre maintenant :
patterns `_`, `_*`, `x`, `x*`, `Foo(p1, …, pN)`, `pat@name`,
`!pat` ; sujets implicites + explicites `<<` ; multi-sujets ;
multi-rules ; body vide ou non-vide.

---

## 6. Suite logique

Reste pour `%match` :
1. **Pattern OR `Foo() | Bar()`** (Cst_ConstantOr, cf.
   `AstBuilder.java:774-779`) — `TermAppl(opts, concTomName(Name("Foo"),
   Name("Bar")), concTomTerm(), concConstraint())`.
2. **Type annotation** sur le RHS du `<<` : `pattern << bqterm:Type`.
3. **AND/OR chaining** de contraintes (`&&` / `||`).
4. **Annotations multiples / sur pattern non-vierge** : étendre
   `addPatternConstraint` pour préserver les contraintes existantes.
5. **`%match` imbriqué** dans un body.

Au-delà : `%strategy`, `%gom`, backquote terms, metaquote.

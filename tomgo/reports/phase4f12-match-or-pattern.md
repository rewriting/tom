# Phase 4.F.12 — OR-pattern `(Foo|Bar)(args)`

Reconnaissance des têtes de pattern de forme `(F1|F2|…|FN)` —
**disjonction de symboles** — suivies d'une liste d'arguments
explicites. Le pattern est abaissé en un seul `TermAppl` dont la
`nameList` contient les N candidats.

---

## 1. Spec exécutable (référence Java)

CST (`CstBuilder.java:445-471`) :
- `fsymbol` (méthode `exitFsymbol`) appelle `buildCstSymbolList(ctx.headSymbol())` et accumule plusieurs `Cst_Symbol` quand la grammaire ANTLR reconnaît `'(' headSymbol ('|' headSymbol)* ')'`.
- Puis `pattern` (méthode `exitPattern`, ligne 452-453) :
  ```java
  } else if(ctx.explicitArgs() != null) {
      res = Cst_Appl.make((CstSymbolList)getValue(ctx.fsymbol()),
                          (CstPatternList)getValue(ctx.explicitArgs()));
  }
  ```
- Donc `(Foo|Bar)(args)` ⇒ `Cst_Appl(symbolList=[Foo,Bar], patternList=args)`.

Lowering (`AstBuilder.java:793-804`, branche `Cst_Appl` déjà
utilisée par 4.F.3 / 4.F.4) :
```java
OptionList optionList = EmptyconcOption.make();
Theory theory = extractTheory(symbolList);    // vide si aucun symbole a ? / ??
if(theory.length() > 0) { /* ajoute MatchingTheory */ }
TomNameList nameList = convert(symbolList);   // → concTomName(Name("Foo"), Name("Bar"))
TomList argList = convert(patternList);       // récursion sur les args
return TermAppl.make(optionList, nameList, argList, EmptyconcConstraint.make());
```

Pour le fixture `(Foo|Bar)()` (args vides, aucun `?`/`??`) :
```
TermAppl(
  concOption(),
  concTomName(Name("Foo"), Name("Bar")),
  concTomTerm(),
  concConstraint()
)
```

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — au tout début de `parseBasePattern`,
nouvelle branche qui détecte `(` comme premier caractère du pattern.

```go
if p.peek(0) == '(' {
    p.advance() // '('
    p.skipBlankInline()
    var names []tomast.TomName
    for {
        id, err := p.readIdent()
        if err != nil { return nil, fmt.Errorf("in OR-pattern head: %w", err) }
        names = append(names, tomast.MakeName(id))
        p.skipBlankInline()
        if p.atEnd() {
            return nil, fmt.Errorf("unterminated OR-pattern head at %s", p.cur)
        }
        if p.peek(0) == '|' {
            p.advance() // '|'
            p.skipBlankInline()
            continue
        }
        break
    }
    if p.atEnd() || p.peek(0) != ')' {
        return nil, fmt.Errorf("expected ')' to close OR-pattern head at %s", p.cur)
    }
    p.advance() // ')'
    p.skipBlankInline()
    if p.atEnd() || p.peek(0) != '(' {
        return nil, fmt.Errorf("OR-pattern must be followed by '(' arg list at %s", p.cur)
    }
    p.advance() // '('
    args, err := p.parsePatternArgList()
    if err != nil { return nil, err }
    if p.atEnd() || p.peek(0) != ')' {
        return nil, fmt.Errorf("expected ')' to close OR-pattern arg list at %s", p.cur)
    }
    p.advance() // ')'
    return tomast.MakeTermAppl(
        tomast.MakeConcOption(),
        tomast.MakeConcTomName(names...),
        tomast.MakeConcTomTerm(args...),
        tomast.MakeConcConstraint(),
    ), nil
}
```

Note : `(Foo)(args)` avec un seul nom est accepté (dégénéré, même
encodage que `Foo(args)`). Le cas `(Foo|Bar)` sans args (qui
correspondrait à `Cst_ConstantOr`) n'est PAS supporté pour le moment
— le parser exige le `(` de l'arglist après la tête.

Grammaire en doc-comment :
```
pattern    : '!' pattern | basePattern ('@' ID)?
basePattern: '_' '*'? | ID '*'? | ID '(' (pattern (',' pattern)*)? ')'
           | '(' ID ('|' ID)* ')' '(' (pattern (',' pattern)*)? ')'
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0m_or/scenario.t` :
  ```
  public class Match0m {
    public void f(Object t) {
      %match(t) {
        (Foo|Bar)() -> { }
      }
    }
  }
  ```
- AST produit pour le pattern `(Foo|Bar)()` :
  ```
  TermAppl(concOption(),concTomName(Name("Foo"),Name("Bar")),concTomTerm(),concConstraint())
  ```
- `TestParseMatch0mOr` épingle l'AST.
- Entrée `"match0m_or"` ajoutée au harnais cross-language.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
... 12 tests, tous PASS
ok    tom/tomgo/internal/tomparser    0.32s
```

Tous les autres paquets verts.

---

## 5. Couverture courante du parser

**19 fixtures** validées (1 ajoutée). `%match` couvre maintenant :
- patterns : `_`, `_*`, `x`, `x*`, `Foo(args)`, `(F1|F2)(args)`,
  `pat@name`, `!pat`,
- sujets : implicites + explicites `<<`,
- multi-sujets, multi-rules,
- body vide ou non-vide.

---

## 6. Suite logique

1. **`Cst_ConstantOr` : `(Foo|Bar)` sans args** — petit complément
   à 4.F.12.
2. **Type annotation** sur le RHS du `<<` : `pattern << bqterm:Type`.
3. **AND/OR chaining** de contraintes (`&&` / `||`).
4. **Symboles à théorie `?`/`??`** : `(Foo?|Bar)` ajouterait un
   `MatchingTheory` dans la liste d'options (`extractTheory` côté Java).
5. **Annotations multiples** sur un pattern qui porte déjà des
   contraintes (`!Foo()@a@b`).
6. **`%match` imbriqué** dans un body.

Au-delà : `%strategy`, `%gom`, backquote terms, metaquote.

# Phase 4.A — Survey des hooks de `src/tom/engine/adt/`

Inventaire : 15 modules, 10 sans hooks, 5 avec — **9 hooks au total**,
regroupés en **4 catégories**. Avant de générer `internal/tomast/`, il
faut étendre `knownHookTable` (`internal/backend/backend.go`) pour
chacun ; sinon la fabrique Go fabriquerait des termes différents de
ceux que la référence Java produit (qui exécute la sémantique de chaque
hook au moment du `make`).

Conventions : "scope:operator" = `(scope=operator|absent)`, c'est la
règle par défaut de la grammaire Gom quand aucun mot-clé `sort`/`module`/`operator`
ne précède le pointcut.

---

## Catégorie 1 — `AU()` (Associative-Unit) — 3 hooks

```
AndConstraint:AU() { `TrueConstraint() }
OrConstraint:AU() { `FalseConstraint() }
OrConstraintDisjunction:AU() { }
```

**Contexte** : ces trois opérateurs sont des constructions variadiques
de la sort `Constraint` :

```gom
Constraint = ... | AndConstraint(Constraint*) | OrConstraint(Constraint*)
                 | OrConstraintDisjunction(Constraint*)
```

**Sémantique** :
- `Op:AU() { unit }` déclare `Op` comme **associatif** (les `Op`
  imbriqués sont aplatis) et donne l'**unité** : le `make` avec zéro
  argument retourne `unit` au lieu d'une liste vide.
- `Op:AU() { }` (corps vide) déclare l'opérateur juste **associatif**,
  sans unité.

**Traduction Go proposée** (modifie `MakeAndConstraint(args ...Constraint) Constraint`) :

```go
func MakeAndConstraint(args ...Constraint) Constraint {
    var flat []Constraint
    for _, a := range args {
        // Associativity: AndConstraint(AndConstraint(x,y), z) → AndConstraint(x,y,z)
        if nested, ok := a.(*AndConstraintConstraint); ok {
            flat = append(flat, nested.Slots...)
        } else {
            flat = append(flat, a)
        }
    }
    if len(flat) == 0 {
        // Unit
        return MakeTrueConstraint()
    }
    // ... usual hash-cons via factory.Build with flat as Slots
}
```

Pour `OrConstraint:AU() { FalseConstraint() }` : idem avec unit =
`MakeFalseConstraint()`.

Pour `OrConstraintDisjunction:AU() { }` : aplatissement seulement, on
ne court-circuite pas le `len == 0` (la liste vide reste valide).

---

## Catégorie 2 — `module M:rules() { rules… }` — 2 hooks (4 règles)

```
module Code:rules() {
  InstructionToCode(CodeToInstruction(t)) -> t
  CodeToInstruction(InstructionToCode(t)) -> t
}
module TomExpression:rules() {
  BQTermToExpression(ExpressionToBQTerm(t)) -> t
  ExpressionToBQTerm(BQTermToExpression(t)) -> t
}
```

**Sémantique** : ces règles sont des **simplifications appliquées au
moment de la construction**. Quand on construit
`InstructionToCode(x)` et que `x` est `CodeToInstruction(t)`, on
retourne directement `t` (les deux opérations s'annulent).

**Traduction Go proposée** : modifier les 4 `MakeXxx` correspondants
avec une garde en début de fonction.

```go
func MakeInstructionToCode(astInstruction Instruction) Code {
    // Rewrite InstructionToCode(CodeToInstruction(t)) -> t
    if inner, ok := astInstruction.(*CodeToInstructionInstruction); ok {
        return inner.AstCode  // <-- the t inside
    }
    // ... usual hash-cons
}
```

À faire pour : `InstructionToCode`, `CodeToInstruction`,
`BQTermToExpression`, `ExpressionToBQTerm`.

⚠ Question ouverte : `CodeToInstruction` n'apparaît pas explicitement
dans le `Code.gom` actuel — il est probablement dans `Il.gom`. Idem pour
`ExpressionToBQTerm` qui devrait être dans `TomTerm.gom` ou
`TomExpression.gom`. À localiser quand on implémente.

---

## Catégorie 3 — `Op:make(args)` — 2 hooks

### 3a — `Cast:make(type, exp) { reject "unknown type" }`

```
Cast:make(type,exp) {
  %match(type) {
    Type(concTypeOption(),"unknown type",EmptyTargetLanguageType()) -> {
      throw new RuntimeException("bad cast");
    }
  }
}
```

**Contexte** : `Cast(AstType:TomType, Source:Expression)` est une
expression de cast. Le hook rejette les casts dont le type est le
type-sentinel "unknown type" + Options vides + TL vide.

**Traduction Go** :

```go
func MakeCast(astType TomType, source Expression) Expression {
    if t, ok := astType.(*TypeTomType); ok {
        if _, optEmpty := t.TomTypeOptions.(*ConcTypeOptionTypeOptionList); /* and empty */
           t.TomType == "unknown type" {
            if _, tlEmpty := t.TlType.(*EmptyTargetLanguageTypeTargetLanguageType); tlEmpty {
                panic("bad cast")
            }
        }
    }
    // ... usual hash-cons
}
```

(Vérification du "vide" pour la liste d'options : `len(slots) == 0`.)

### 3b — `NameNumber:make(name) { simplify positional }`

```
NameNumber:make(name) {
  %match(name) {
    PositionName(concTomNumber(p@Position[])) -> { return `p; }
  }
}
```

**Contexte** : `NameNumber(AstName:TomName)` est une variante de
`TomNumber`. Le hook simplifie `NameNumber(PositionName(concTomNumber([Position(_)])))`
en retournant simplement la `Position`. Notez bien que `Position` est
elle-même un `TomNumber` — la signature reste cohérente.

**Traduction Go** :

```go
func MakeNameNumber(astName TomName) TomNumber {
    if pn, ok := astName.(*PositionNameTomName); ok {
        if list, ok := pn.NumberList.(*ConcTomNumberTomNumberList); ok &&
           len(list.Slots) == 1 {
            if _, ok := list.Slots[0].(*PositionTomNumber); ok {
                return list.Slots[0]
            }
        }
    }
    // ... usual hash-cons
}
```

---

## Catégorie 4 — `Op:make_insert(e, l)` — 2 hooks

### 4a — `concInstruction:make_insert(e, l) { splice AbstractBlock }`

```
concInstruction:make_insert(e,l) {
  %match(e) {
    AbstractBlock(l1) -> { return `concInstruction(l1*,l*); }
  }
}
```

**Contexte** : `concInstruction(Instruction*)` est la liste variadique
des instructions. Le hook **aplatit** : un `AbstractBlock(l1)` inséré
dans une liste fait splicer `l1` à sa place.

**Traduction Go** (modifie la boucle dans `MakeConcInstruction`) :

```go
func MakeConcInstruction(args ...Instruction) InstructionList {
    var flat []Instruction
    for _, e := range args {
        if ab, ok := e.(*AbstractBlockInstruction); ok {
            if inner, ok := ab.InstList.(*ConcInstructionInstructionList); ok {
                flat = append(flat, inner.Slots...)
                continue
            }
        }
        flat = append(flat, e)
    }
    // ... usual hash-cons with flat as Slots
}
```

### 4b — `concTomNumber:make_insert(e, l) { splice nested NameNumber }`

```
concTomNumber:make_insert(e,l) {
  %match(e) {
    NameNumber(PositionName(concTomNumber(p*))) -> { return `concTomNumber(p*,l*); }
  }
}
```

Aplatit `NameNumber(PositionName(concTomNumber(p*)))` en sa séquence
interne `p*` quand cet élément est inséré dans un `concTomNumber`.

**Traduction Go** : symétrique du précédent, type-asserts en cascade.

---

## Plan d'implémentation

### Architecture

`knownHookTable` doit gérer deux **types d'injection** distincts :

1. **Méthode supplémentaire sur la sort** (existant — utilisé par
   `HookList:block` → `ContainsTomCode()`). `emitImpl` injecte du code
   après le constructeur.
2. **Modification du smart-constructor** (nouveau). Avant l'émission
   du corps `Make<Op>`, on injecte une prélude (gardes, splicing,
   normalisation, court-circuit). C'est le nouveau champ proposé :
   `emitMakePrologue`.

Adaptation de la structure :

```go
type knownHook struct {
    module           string
    scope            string  // "sort" | "module" | "operator" | ""
    pointCut         string
    kind             string  // "block" | "AU" | "rules" | "make" | "make_insert"

    // sort-scope block hooks
    interfaceDecl    string
    emitImpl         func(buf *bytes.Buffer, g *gen, prod *gomast.SortTypeProduction, alt *gomast.AlternativeAlternative, structName string)

    // operator/module-scope hooks that modify the smart constructor
    appliesToAlt     func(g *gen, alt *gomast.AlternativeAlternative) bool   // filter on alt name etc.
    emitMakePrologue func(buf *bytes.Buffer, g *gen, alt *gomast.AlternativeAlternative, sortName, structName string, slots []struct{Name, GoType string; IsVar bool})
}
```

### Ordre des étapes

1. ✏️ **Documenter** (ce fichier) — fait.
2. **Étendre `knownHookTable`** avec les 9 entrées (en utilisant la
   nouvelle injection `emitMakePrologue`).
3. **Modifier `emitAlternative`** pour appeler les prologues quand un
   hook s'applique à l'alt courant.
4. **Générer `internal/tomast/`** depuis `src/tom/engine/adt/*.gom`.
5. **Smoke tests** : pour chaque hook, un mini-test Go qui vérifie le
   comportement attendu (par ex. `MakeAndConstraint() == MakeTrueConstraint()`,
   `MakeInstructionToCode(MakeCodeToInstruction(t)) == t`, …).
6. **Auto-amorce test** : `TestTomastSelfBootstrap` (analogue de
   `TestSelfBootstrap` pour gomast).

### Risques identifiés

- **Cross-module references** : les hooks de `Code.gom` (rules) référencent
  `CodeToInstruction` qui vit potentiellement dans `Il.gom`. Vérifier
  que la fonction `MakeCodeToInstruction` existe et a la bonne signature
  avant d'émettre le prologue.
- **`make` hooks et identité du résultat** : un hook `make` peut retourner
  un terme **différent** de l'opérateur déclaré (NameNumber:make retourne
  une Position, pas une NameNumber). Il faut que le type Go déclaré de
  retour soit la **sort** (interface), pas la struct concrète — c'est déjà
  le cas dans mon backend (`Make<Op>(args) Sort`).
- **Sharedobjects et `panic`** : `Cast:make` jette une exception côté
  Java. En Go on `panic` ; faut-il plutôt retourner un `error` ? Pour la
  parité de comportement avec Java (qui propage la RuntimeException),
  `panic` est le bon choix V1.
- **`OrConstraintDisjunction:AU() { }`** : corps vide. Faut-il quand
  même aplatir ? La convention Gom est "AU sans unité ⇒ associatif
  seulement". V1 : on n'aplatit pas si le corps est vide (parité avec
  l'implémentation Java à confirmer).

---

## Question(s) ouverte(s) — toutes tranchées par l'utilisateur

1. ✅ **`AU` à corps vide** : aplatissement seulement (sans unité).
2. ✅ **`Cast:make` ⇒ `panic("bad cast")`** (parité Java).
3. ✅ **Reconnaître `Type(_, "unknown type", _)`** : match strict —
   `TypeOptions == concTypeOption()` ET `TomType == "unknown type"`
   ET `TlType == EmptyTargetLanguageType()`.

---

## Implémentation livrée

Tout est dans `internal/backend/backend.go` :

- 9 entrées ajoutées à `knownHookTable` (les 9 hooks de l'engine ADT).
- Nouveau mécanisme `emitMakePrologue` qui injecte du code en tête du
  smart-constructor. Deux helpers généraux : `emitAUPrologue(op, sort, unit)`
  et `emitInversePairPrologue(lhsRoot, …, lhsInner, …)` qui couvrent les
  4 patterns (AU, rules, make, make_insert).

Fixes annexes du backend, nécessaires pour que le batch sur l'engine
ADT compile :

- **Mot-clé Go `else`** ajouté à `isGoKeyword` (slot `else` dans
  `Conditional(cond, then, else)` de TomExpression).
- **Détection de collision pour `Make<Op>`** : lorsque le même nom
  d'opérateur apparaît dans plusieurs sorts (`Subterm` est un alt à la
  fois de BQTerm et de Term), le générateur suffixe `Make<Op><Sort>`
  pour préserver l'unicité.
- **Slot vs méthode** : si le nom Go exporté d'un slot heurte une
  méthode auto-générée (`String`, `Hash`, `Equivalent`, `Duplicate`),
  le champ est suffixé d'un `_` (cf. `TomName.Name(String:String)` →
  champ `String_`).
- Le générateur écrit la source brute sur `gofmt` qui échoue, pour
  débugger facilement.

## Résultats (preuves)

```text
$ go test ./internal/backend/... -run TestHook -v
=== RUN   TestHook_AndConstraint_EmptyReturnsTrueConstraint        PASS
=== RUN   TestHook_AndConstraint_AssociativityFlattens             PASS
=== RUN   TestHook_OrConstraint_EmptyReturnsFalseConstraint        PASS
=== RUN   TestHook_OrConstraintDisjunction_NoUnit_FlattensOnly     PASS
=== RUN   TestHook_CodeRules_InstructionToCode_CancelsInverse      PASS
=== RUN   TestHook_CodeRules_CodeToInstruction_CancelsInverse      PASS
=== RUN   TestHook_ExpressionRules_BQTermToExpression_CancelsInverse  PASS
=== RUN   TestHook_ExpressionRules_ExpressionToBQTerm_CancelsInverse  PASS
=== RUN   TestHook_Cast_RejectsUnknownType                         PASS
=== RUN   TestHook_Cast_AcceptsKnownType                           PASS
=== RUN   TestHook_ConcInstruction_FlattensAbstractBlock           PASS
=== RUN   TestHook_NameNumber_SimplifiesNestedPosition             PASS
=== RUN   TestHook_NameNumber_DoesNotSimplifyOtherShapes           PASS
=== RUN   TestHook_ConcTomNumber_FlattensNestedNameNumber          PASS

$ go test ./internal/backend/... -run TestSelfBootstrap_Tomast
PASS  (regenerated tomast/ byte-identical to committed)

$ wc -l internal/tomast/*.go | tail -1
   17378 total
```

15 modules / 107 sorts / 9 hooks → tomast compile, smoke tests verts,
self-bootstrap byte-stable.

## Pièges initialement rencontrés (résolus)

- Les hooks **`module M:rules() { rules… }`** sont *déclarés* dans le
  module M, mais leurs règles s'appliquent à des opérateurs dont
  l'**alt** vit dans un autre module. Pour `CodeToInstruction` (qui
  vit dans TomInstruction.gom alors que la règle est déclarée dans
  Code.gom), le filtre `module` doit pointer vers le module de l'alt,
  pas celui de la déclaration. Idem pour `ExpressionToBQTerm`. Bug
  attrapé par le test `TestHook_ExpressionRules_BQTermToExpression_CancelsInverse`
  qui a échoué avant la correction.
- Les collisions de noms (`MakeSubterm` × 2, slot `String:String`) ont
  toutes été résolues par les fixes ci-dessus sans casser le
  self-bootstrap de gomast.

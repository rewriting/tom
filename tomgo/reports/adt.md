# Compilation de `src/tom/gom/adt/*.gom`

## Objectif

Compiler les 5 fichiers ADT du compilateur Gom lui-même, qui :

- s'importent mutuellement (`Code` imports `Gom Objects`, etc.) ;
- contiennent **1 hook** — `sort HookList:block()` dans `Objects.gom` —
  qui ajoute en Tom une méthode `containsTomCode()` à la liste de hooks.

Trois extensions au backend Phase 2 ont été nécessaires :

1. Parser : accepter les hooks (scope + pointcut + kind + corps brut
   balanced-brace).
2. Backend : un nouveau mode `gom-batch` qui compile N modules dans un
   **seul package Go** ; les sorts cross-module résolvent vers
   l'interface Go locale.
3. Backend : table `knownHookTable` qui mappe `(scope, pointcut, kind)`
   vers une fonction d'émission Go. Pour V1, une seule entrée :
   `("sort","HookList","block")` → `ContainsTomCode() bool`. Pour les
   autres hooks (inexistants ici), on laisse un commentaire et on
   continue.

## Lowering du hook `HookList:block()`

Source Tom (Objects.gom) :

```tom
sort HookList:block() {
  public boolean containsTomCode() {
    boolean containsTomCode = false;
    %match(this) {
      ConcHook(_*,(MakeHook|MakeBeforeHook|BlockHook)[HasTomCode=HasTomCode],_*)
        && true() << HasTomCode -> { containsTomCode = true; }
    }
    return containsTomCode;
  }
}
```

Sortie Go (objects.go généré) :

```go
type HookList interface {
    sharedobjects.Term
    isHookList()
    ContainsTomCode() bool
}

func (t *ConcHookHookList) ContainsTomCode() bool {
    for _, h := range t.Slots {
        switch ht := h.(type) {
        case *MakeHookHook:
            if ht.HasTomCode { return true }
        case *MakeBeforeHookHook:
            if ht.HasTomCode { return true }
        case *BlockHookHook:
            if ht.HasTomCode { return true }
        }
    }
    return false
}
```

Stratégie : faute de compilateur Tom, on a hard-codé la traduction Go
de cet idiome précis (boucle + switch-type), conforme à la sémantique
du `%match` Tom.

## Métriques

```text
$ tomgo gom-batch -o /tmp/adt-out --pkg adt src/tom/gom/adt/*.gom
wrote /tmp/adt-out/ (5 module(s), 63 sort(s), 1 hook(s))

$ wc -l /tmp/adt-out/*.go
   419 code.go
  3024 gom.go
   894 objects.go
  1066 rule.go
   761 symboltable.go
  6164 total

$ cd /tmp/adt-out && go build ./... && go test ./...
ok  	(passe)
```

Tests dans `internal/backend/backend_adt_test.go` (`TestGenerate_ADT`) :

- 5 sous-tests sur `ContainsTomCode` :
  - liste avec `BlockHook(_, false)` + `InterfaceHook(_)` → `false`
  - liste avec `BlockHook(_, true)` → `true`
  - liste avec `MakeHook(_, _, true)` → `true`
  - liste avec `MakeBeforeHook(_, _, true)` → `true`
  - liste avec seulement `InterfaceHook` / `ImportHook` / `MappingHook`
    (pas de champ `HasTomCode`) → `false`

Tous passent — la méthode Go reproduit fidèlement le comportement décrit
par le `%match` Tom.

## Cross-module : exemple

Dans `Objects.gom` :

```
Hook = MakeHook(HookArguments:SlotFieldList, Code:Code, HasTomCode:boolean)
```

Le slot `Code:Code` référence la sort `Code` définie dans `Code.gom`.
Sortie Go (extrait) :

```go
type MakeHookHook struct {
    HookArguments SlotFieldList   // sort interne à Objects
    Code          Code            // sort venant de Code.gom — même paquet
    HasTomCode    bool
    hash          uint32
}
```

La table `allSorts` calculée en début de `GenerateBatchToDir` recense
les 63 sorts ; lors de l'émission, `g.goType("Code")` reconnaît `Code`
comme un sort connu (peu importe le module d'origine) et émet
l'interface Go locale.

## Limites assumées (V1)

- Mode **un-seul-package** uniquement pour le batch : pas de paquets Go
  séparés par module. Les noms restent uniques sur l'ADT actuel, mais
  ça suppose qu'on étende plus tard pour gérer des collisions.
- Une seule entrée dans `knownHookTable`. Les autres hooks de
  l'écosystème (autres `block`, `make`, `make_insert`, etc.) émettent
  un commentaire « unsupported hook » et **rien** d'autre, donc la
  compilation peut planter sur des `.gom` plus exotiques (par ex. ceux
  des examples avec `Op:make()`). À étendre au cas par cas.
- Le hook `block` est implémenté SANS comprendre son corps Tom — c'est
  un mapping codé en dur. Une vraie solution attendra un mini-parser
  Tom (Phase 3+).
- Pas de cible équivalence Go ⇄ Java sur l'ADT pour l'instant — les
  API d'accès (Java : SingletonSharedObjectFactory + `.getHookArguments()`
  vs Go : `*MakeHookHook.HookArguments`) divergent assez pour mériter
  un scénario dédié.

## Commandes de reproduction

```bash
cd tomgo
go test ./internal/backend/... -run TestGenerate_ADT -v
# ou en CLI :
go run ./cmd/tomgo gom-batch -o /tmp/adt --pkg adt ../src/tom/gom/adt/*.gom
(cd /tmp/adt && go build ./... && go vet ./...)
```

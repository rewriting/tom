# Équivalence comportementale Go ⇄ Java (référence)

## Idée

Pour chaque `.gom` cible, on tourne **la même séquence d'opérations** sur
deux implémentations indépendantes — celle générée par mon `tomgo` (Go,
backed by `library/sharedobjects`) et celle générée par le compilateur
Gom Java historique (`tom.gom.Gom`, backed by `shared-objects.jar`) — et
on exige que `stdout` soit **byte-pour-byte identique**.

## Pipeline

```
.gom ─┬─ tomgo backend       → Go package → go run    → stdoutGo
      └─ tom.gom.Gom (Java)  → Java pkg   → javac+java → stdoutJava
            diff(stdoutGo, stdoutJava) doit être vide.
```

Implantation : `internal/equivtest` ; jeux de tests
`testdata/equiv/<target>/{scenario.go,Scenario.java}` ; instantanés
stdout persistés dans `reports/equiv-<target>/{go,java}-stdout.txt`.

Test Go : `TestEquivalence/<target>` ; skip propre si pas de JDK ou
des jars de référence absents.

## Outillage Java requis

| Élément                                  | Source                                                          |
| ---------------------------------------- | --------------------------------------------------------------- |
| JDK                                      | détecté via `JAVA_HOME`, puis `/opt/homebrew/opt/openjdk`, puis `$PATH` ; ce repo a été testé contre OpenJDK 25 |
| Compilateur Gom Java                     | `applications/prototype3D/lib/tom-compiler-full.jar`            |
| Runtime Gom Java (shared-objects, aterm) | `applications/prototype3D/lib/tom-runtime-full.jar` + `stable/lib/runtime/*.jar` |
| Config Gom XML                           | `utils/eclipse-plugin/plugin/config/Gom.xml`                    |

## Cibles couvertes (3)

| Cible    | `.gom`                | Ce qui est stressé                                          |
| -------- | --------------------- | ----------------------------------------------------------- |
| minimal  | `Minimal.gom`         | sort + plusieurs opérateurs typés, accesseurs de slot, sharing |
| leaf     | `Leaf.gom`            | slot built-in `String` (quoting `"..."`, accesseur)         |
| list     | `List.gom`            | variadique de built-in `int`, vide, longueur                |

## Comportements comparés

Pour chaque cible, on vérifie sur les 2 implantations :

- **`toString`** d'un terme construit (format canonique, p. ex. `BinaryNop(EmptyNop(),UnaryNop(EmptyNop()))`).
- **`equals` / `==`** : deux constructions structurellement égales
  retournent la même instance canonique (preuve de partage).
- **Distinction** : deux constructions différentes retournent des
  instances distinctes.
- **Accesseurs de slots** : pour `BinaryNop(a, b)`, lire `ls` et `rs`
  redonne `a` et `b` exactement.
- **Variadique** : `conc(1,2,3)` partagé, vide partagé, longueur correcte.

Non comparé (par design) :

- Valeurs de hash (les hashCodes Java/Go ne portent pas le même schéma
  d'encodage des scalaires).
- Représentation interne (Java utilise une cons-list pour les
  variadiques ; Go utilise un slice). Seul le comportement observable
  via l'API publique est comparé.

## Résultat (preuves)

```text
$ go test ./internal/equivtest/... -v
=== RUN   TestEquivalence
=== RUN   TestEquivalence/minimal
    identical stdout (both sides):
        t1=EmptyNop()
        t2=UnaryNop(EmptyNop())
        t3=BinaryNop(EmptyNop(),UnaryNop(EmptyNop()))
        shared-emptynop=true
        shared-binarynop=true
        different-terms=true
        binarynop-ls=EmptyNop()
        binarynop-rs=UnaryNop(EmptyNop())
=== RUN   TestEquivalence/leaf
    identical stdout (both sides):
        t1=Leaf()
        t2=Label("hello")
        shared-label=true
        diff-labels=true
        label-s=hello
=== RUN   TestEquivalence/list
    identical stdout (both sides):
        a=conc(1,2,3)
        shared=true
        diff=true
        empty=conc()
        shared-empty=true
        len=3
--- PASS: TestEquivalence (2.64s)
    --- PASS: TestEquivalence/minimal (0.80s)
    --- PASS: TestEquivalence/leaf (0.90s)
    --- PASS: TestEquivalence/list (0.94s)
PASS
```

**3 / 3 cibles : stdout byte-pour-byte identique entre Go (tomgo) et Java (référence).**

## Ajustements faits côté tomgo pour atteindre la conformité

- Format du `String()` Go pour les slots `string` et `rune` : passé de
  `%v` à `%q` afin de produire les guillemets Java-style
  (`Label("hello")` au lieu de `Label(hello)`).

## Limites assumées

- Couverture restreinte à 3 fichiers sur 10 (décision utilisateur). Les
  7 restants compilent (Phase 2) mais ne sont pas comparés
  comportementalement pour l'instant.
- Pas de cible variadique-de-sort (`Vary(Nop*)`). Java l'encode
  cons-list, mon Go la stocke en slice ; le `toString` produit la même
  chaîne plate mais la construction (`fromArray` côté Java vs
  `MakeVary(...)` côté Go) divergerait dans le scénario. Faisable mais
  pas indispensable pour le jalon.
- Cross-module (`imports`) non testé en équivalence : `LeafSlot(l:Leaf)`
  côté Go reçoit un `any`, côté Java un `leaf.types.Leaf` — l'API
  diffère sensiblement, donc cible à part.

## Comment ré-exécuter

```bash
cd tomgo
go test ./internal/equivtest/... -v          # 3 sub-tests, ~3s
ls reports/equiv-*/                            # snapshots stdout
diff reports/equiv-leaf/go-stdout.txt reports/equiv-leaf/java-stdout.txt
```

Si JDK ou jars absents, le test `Skip` proprement avec un message
explicatif (pas d'échec rouge).

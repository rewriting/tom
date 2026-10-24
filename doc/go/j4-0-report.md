# J4.0 — pont Java → Go (binaires, corrections gomgo, préfixe par fichier, `--goFrom`, identité P0)

Date : 2026-10-24. Branche `tom-go`.
Plan : `docs/superpowers/plans/2026-10-24-j4-0-bridge.md`.
Spec : `docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md`
(§4.1 pont, §4.2 paquet, §4.3 binaires et préfixe, §6 jalons J4.0a–J4.0d,
§8 décisions D1–D6).

Résultat : le compilateur Tom+Java peut passer la main à un processus Go
(`--goFrom <plugin>`, binaire `tomgo suffix`) ; le paquet texte (options,
marques, types, table des symboles, terme) et les 1139 dumps
`--intermediate` mesurés se relisent et se réécrivent à l'identique côté
Go (P0). Sans `--goFrom`, les sorties Java (T0) et Go (T1) sont inchangées
octet pour octet, hors les renommages voulus du préfixe par fichier. En
J4.0, `tomgo suffix` ne connaît aucune phase (il les refuse) : le pont
n'est vérifié que par l'aller-retour.

## 1. Ce qui a été fait, par sous-jalon

Commits : `git log --oneline 218f245e8..HEAD` (14 commits de travail après
le plan).

| Lot | Commits | Contenu principal |
|---|---|---|
| J4.0a — binaires | `8e01a47e9` | `mage installGo` installe `gomgo` (et `tomgo` quand son paquet existe) dans `<dist>/bin`, appelé par `build.xml` (`stable.dist`, `src.dist`). `TomParserTool.t` trouve gomgo depuis `tom.home` (`GOMGO_BIN` en surcharge seulement, jamais le `PATH`). Le harnais utilise les binaires de la dist ; `CheckFresh` couvre aussi `src/config/*.config` et refuse un `gomgo`/`tomgo` plus ancien que ses sources. |
| J4.0a — gomgo | `39913125d`, `8ecb29cb9`, `1ff285a70`, `5afa8c8c5` | `get_slot` du mapping généré lit le champ renommé (`String_`, `Hash_`…) : `slotFieldNames` est l'unique source des noms de champs Go. `ToATerm` d'une liste AU qui finit par un élément. Chaque correction est suivie de sa promotion dans l'amorce `stable/go/tom`. |
| J4.0a — ADT du moteur | `3ebcb3989`, `3f17d363d` | `mage j4` : programme Tom+Go (`test/go/j4/testdata/adtuse`) qui utilise l'ADT du moteur (`adt.tom`) ; `test/tools/j4`. |
| J4.0b — préfixe | `cd9021ac4`, `2fb5a815d`, `89287b07d`, `b4e41b823` | Toutes les déclarations de niveau paquetage générées pour Go sont préfixées `tom_<F>_` (F = nom du `.t`) : `goDeclPrefix` (`TomBase.t`), `declName` (`AbstractGenerator`, `GenericGenerator`, `GoGenerator`), `goHelperName` (`Compiler`, `ConstraintGenerator`, aides AC et `next_minimal_extract`), mappings `string`, `intarray`, `util/{ArrayList,LinkedList}` convertis en fermetures. Outil `test/tools/j4/renamecheck` : prouve que la diff des goldens se réduit à des renommages (`files 83, renames-only 83, other 0`, 80 goldens `.go` changés). |
| J4.0c — côté Go | `f3a11b2de` | `tom/engine/bridge` (lecture/écriture du paquet, `bridge.Error` avec ligne) et commande `tom/engine/cmd/tomgo` (`suffix [--echo]`). |
| J4.0c — côté Java | `ddf4b6177`, `829b0a607` | Options `goFrom`/`goCheck` (`Tom.config`), crochet dans `PluginPlatform`, `tom.engine.bridge.GoBridgePlugin` (voir ci-dessous), message d'erreur par cas, timeout 300 s, `--goFrom Starter|Parser` refusé, `--intermediate` écrit `<sortie>.tfix.bridge`. |
| J4.0d — P0 | `ffa432943` | `mage bridgeCheck` : T1 et smoke avec `--goFrom Backend --goCheck --intermediate`, puis relecture en Go de tous les dumps (`TestDumpsRoundTrip`). Tableau `doc/go/j4-dashboard.md`. |

Volume (`git diff --shortstat 218f245e8^..HEAD -- src test magefiles stable
build.xml`) : 133 fichiers, 4 970 insertions, 2 177 suppressions.

## 2. Vérifications finales

Lancées une fois, dans cet ordre, sur `src/dist` fraîche (JDK 8) :

| Commande | Résultat |
|---|---|
| `mage -d magefiles -w . all` | vert (`tom/engine/bridge`, `tom/engine/cmd/tomgo`, `gom/backend`, `tomtest/adt`, `tomtools/smoke` ok) |
| `mage … portJava` | `ok tomtools/javaparity 236.5s` ; T0 : `TestJavaOutputIdenticalToLegacy` et `…NewParser` PASS (le test échoue sous les planchers `minDefault = 102`, `minNP = 103` ; les comptes 102/764 et 103/765 ne sont pas imprimés, ils ont été relevés en Task 8) |
| `mage … portGo` | `ok tomtools/parity 234.4s` ; tableau de bord : 77 OK / 11 OK (diagnostics) / 9 échoue dans legacy / 2 non supporté / 4 support / 0 divergence / 0 non porté |
| `mage … coverage` | `0 construct(s) not yet covered`, 16 lignes |
| `mage … j4` | `ok tomtools/j4` (9.3 s), `ok tomtools/j4/renamecheck` |
| `mage … bridgeCheck` | vert ; 121 fichiers lus (T1 + smoke), 110 paquets pont dont 27 smoke ; `String()` identique : total 1139, identiques 1139, différents 0 |
| `cd test/tools && go test -count=1 ./smoke/` | `ok tomtools/smoke 24.4s` |

Seul fichier suivi modifié par les vérifications : `doc/go/coverage.md`
(chiffres des lignes « anti-patterns » 32 → 33 et « backquote terms »
3572 → 3573, dus aux fixtures ajoutées depuis la dernière génération) ;
`doc/go/port-dashboard.md` est inchangé.

## 3. Différences voulues

- Goldens T1 : les 80 `.go` modifiés ne diffèrent que par le préfixe
  `tom_<F>_` (vérifié par `renamecheck`) ; aucun `.log` changé.
- Les aides de mapping (`tom_stringHead`…) sont des fermetures de
  paquetage au lieu de `func` : le texte du helper disparaît, ses appels
  sont repliés sur `<nom>(` par `renamecheck`.
- `--help` du compilateur liste deux options de plus (`goFrom`, `goCheck`).

## 4. Points ouverts et limites connues

- **Non-terminaison AC** : `f??(_*,a(),_*) -> { n++ }` (variables AC
  anonymes ou inutilisées) boucle indéfiniment : le tableau de solutions
  est inlinisé et `next_minimal_extract(…, tom_empty_array_concInt(length))`
  reçoit un tableau neuf à chaque tour. Le compilateur Java génère le même
  appel (défaut de l'optimiseur / `ConstraintGenerator.buildACMatchLoop`,
  partagé). Les fixtures `A.t`/`B.t` de `test/go/j4` utilisent les variables
  comme contournement. À traiter dans une tâche à part.
- **Marques `used`** : le paquet porte les marques déjà posées par les phases précédentes (par exemple celles du compilateur pour `concInt` en présence de filtrage AC) ; le backend Go devra partir de l'ensemble du paquet et ajouter les siennes, comme `markUsedConstructorDestructor` (commentaire dans `bundle()` et README).
- **ATerm des opérateurs de liste** : un terme-liste ATerm ne nomme pas
  son opérateur, la relecture donne le premier opérateur variadique du sort
  (même limite côté Java, voir `doc/go/j4-dashboard.md` : 111/10
  identiques/différents côté `ToATerm` pour `.parsed`/`.transformed`,
  104/8 pour `.desugared`/`.typed`, informatif ; 110/0 pour
  `.compiled`/`.optimized`, 112/0 pour `.typed.table`). La comparaison
  bloquante est celle de `String()` (1139/1139).
- **`renamecheck`** : compare des jetons Go (`go/scanner`), commentaires
  ignorés, points-virgules automatiques ignorés, explicites comparés ; il
  ne prouve que « renommages seuls », pas l'absence de collision de noms.
  Deux `.t` de même nom de base dans un même paquetage Go se heurtent
  (préfixe identique, noté au README).
- `.parsed.table` est écrit par Java en syntaxe ATerm (échappement `\'`) :
  vérifié par relecture/réimpression modulo cet échappement.
- Le test `go run` de `TestTwoFilesOnePackage` a un timeout de 60 s
  (`harness.RunInTimeout`) ; `tomgo` est tué après 300 s côté Java.
- `stable/` lit encore `GOMGO_BIN` (jusqu'à la prochaine promotion Java).

## 5. Tout relancer

```bash
unset TOM_HOME
(cd legacy && ./build.sh stable && ./build.sh src.dist)   # oracle, JDK 8
./build.sh stable && ./build.sh src.dist
mage -d magefiles -w . all
mage -d magefiles -w . portGo && mage -d magefiles -w . coverage
mage -d magefiles -w . j4 && mage -d magefiles -w . bridgeCheck
mage -d magefiles -w . portJava          # ~4 min
(cd test/tools && go test -count=1 ./smoke/)
```

Prochaine étape : plan J4.1a (fondations Go, `mage regenTomGo`), spec §6.

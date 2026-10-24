# Tableau de bord J4 (pont Java -> Go)

Spec §5.2 : `docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md`.
Mesures du 2026-10-24 (J4.0d).

## J4.1a : fondations (mesures du 2026-10-24)

Commandes : `mage -d magefiles -w . j4` (5 paquets) et `bridgeCheck`
(110 paquets) ; rapport [`j4-1a-report.md`](j4-1a-report.md).

| Mesure | Valeur |
|---|---|
| Scripts d'écriture `OutputCode` (Java contre Go) | 407 ; identiques 407 / différents 0 |
| Paquets interrogés (`SymbolTable`/`TomBase`) | 115 (5 sous `j4`, 110 sous `bridgeCheck`) ; identiques 115 / différents 0 |
| Lignes de donnée des réponses (`symbol`, `usedEntry`, `termType` non erreur, `used` vrai) | total 2750 sous `j4` (plancher 2600), 37850 sous `bridgeCheck` (plancher 36000) ; plancher par paquet à table non vide : 1 |
| Sources Tom+Go compilées par `regenTomGo` | 3 (`astfactory.t`, `symboltable.t`, `tombase.t`) |

## J4.1b : backend Go (P2) (mesures du 2026-10-24)

Compteur in-process `TestBackendCorpus` (`test/tools/j4/backend_test.go`) : le
backend Go (`backend.Generate`) sur chaque paquet pont, comparé octet pour octet
à la sortie du backend Java (copiée en `<sortie>.generated` par le harnais), par
famille. Commandes : `mage -d magefiles -w . j4` (7 paquets), `mage -d magefiles
-w . bridgeCheck` (110 paquets). Planchers : `minIdenticalJ4 = 2`,
`minIdenticalBridge = 4`.

| Famille | `mage j4` paquets | identiques | différents | `bridgeCheck` paquets | identiques | différents |
|---|---|---|---|---|---|---|
| F1 squelette | 2 | 2 | 0 | 5 | 4 | 1 |
| F2 filtrage | 1 | 0 | 1 | 33 | 0 | 33 |
| F3 listes | 2 | 0 | 2 | 47 | 0 | 47 |
| F4 stratégies | 2 | 0 | 2 | 25 | 0 | 25 |
| Total | 7 | 2 | 5 | 110 | 4 | 106 |

Bouchons atteints (`bridgeCheck`) : `buildIsFsymDecl` 50, `genDeclMake` 49,
`buildUnamedBlock` 4, `buildMethodDef` 3 (le seul F1 différent,
`gom/TestTermgraph.t`, s'arrête sur `genDeclMake`). État post-`Collector` :
110 / 110 identiques (8311 lignes).

| Tâche | Famille portée | `mage j4` identiques | `bridgeCheck` identiques |
|---|---|---|---|
| 3 (compteur) | F1 (squelette, partiel) | 2 / 7 | 4 / 110 |

## P0 : identité du pont

Commande : `mage -d magefiles -w . bridgeCheck` (T1 `TestGoBackend` + smoke
`TestGoSmoke` avec `--goFrom Backend --goCheck --intermediate`, puis
`TestDumpsRoundTrip`). Goldens T1 inchangés.

| Mesure | Valeur |
|---|---|
| Entrées parsées (T1 + smoke, dumps `.parsed`) | 121 |
| Paquets `.tfix.bridge` (atteignent le backend ; dont 27 smoke) | 110, tous relus et réécrits à l'identique |
| `.parsed` / `.transformed` | 121 / 121 |
| `.desugared` / `.typed` | 112 / 112 |
| `.expanded` / `.compiled` / `.optimized` | 110 / 110 / 110 |
| `.parsed.table` / `.typed.table` | 121 / 112 |
| Total des dumps | 1139 |
| `String()` identique / différent | 1139 / 0 |
| `ToATerm` aller-retour (informatif) : identiques / différents | `.parsed` 111/10, `.transformed` 111/10, `.desugared` 104/8, `.typed` 104/8, `.expanded` 102/8, `.compiled` 110/0, `.optimized` 110/0, `.parsed.table` 121/0, `.typed.table` 112/0 |

Notes :
- Le total (1139) dépasse le relevé du spec (675) : les `.transformed`,
  `.desugared`, `.bridge` et `.parsed.table` n'y étaient pas comptés ; les cas
  smoke ont été ajoutés.
- `ToATerm` : un terme-liste ATerm ne nomme pas son opérateur, la relecture donne
  le premier opérateur variadique du sort ; ces écarts sont attendus (Java a la
  même limite) et ne font jamais échouer le test.
- `.parsed.table` est écrit par Java en syntaxe ATerm (`toATerm()`), pas en
  `String()` ; il est vérifié par relecture/réimpression ATerm (modulo
  l'échappement `\'` de l'écrivain Java).
- Planchers du test : 110 paquets et 121 entrées parsées.

| Jalon | Entrées | Identiques | Différents | Date |
|---|---|---|---|---|
| P0 | 121 | 1139 | 0 | 2026-10-24 |
| P1 | | | | |
| P2 | | | | |
| P3 | | | | |

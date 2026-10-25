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
-w . bridgeCheck` (110 paquets). Planchers finaux : `minIdenticalJ4 = 7`,
`minIdenticalBridge = 110` (clôture du jalon, rapport
[`j4-1b-report.md`](j4-1b-report.md) ; la progression par tâche est plus bas).

| Famille | `mage j4` paquets | identiques | différents | `bridgeCheck` paquets | identiques | différents |
|---|---|---|---|---|---|---|
| F1 squelette | 2 | 2 | 0 | 5 | 5 | 0 |
| F2 filtrage | 1 | 1 | 0 | 33 | 33 | 0 |
| F3 listes | 2 | 2 | 0 | 47 | 47 | 0 |
| F4 stratégies | 2 | 2 | 0 | 25 | 25 | 0 |
| Total | 7 | 7 | 0 | 110 | 110 | 0 |

(Mesures de clôture, 2026-10-25. Au départ de la Task 3 : 2 / 7 et 4 / 110, avec
les bouchons `buildIsFsymDecl` 50, `genDeclMake` 49, `buildUnamedBlock` 4,
`buildMethodDef` 3 ; à la fin : aucun bouchon atteint.)

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
| P2 (binaire, Task 7) | 110 (T1 83 + smoke 27) | 110 | 0 | 2026-10-24 |
| P3 | | | | |

### P2 (J4.1b) : compteur par famille

| Tâche | Mesure `bridgeCheck` (110 paquets) | Sous `mage j4` | Planchers |
|---|---|---|---|
| Task 3 (famille F1) | identiques 4 / 5 en F1 | 2 / 2 | `minIdenticalJ4 = 2`, `minIdenticalBridge = 4` |
| Task 4 (famille F2) | F1 4 / 5, F2 32 / 33 ; total identiques 36 / 110 | 3 / 7 (F1 2 / 2, F2 1 / 1) | `minIdenticalJ4 = 3`, `minIdenticalBridge = 36` |
| Task 5 (famille F3) | F1 5 / 5, F2 33 / 33, F3 47 / 47 ; total identiques 85 / 110 | 5 / 7 (F1 2 / 2, F2 1 / 1, F3 2 / 2) | `minIdenticalJ4 = 5`, `minIdenticalBridge = 85` |
| Task 6 (famille F4) | F1 5 / 5, F2 33 / 33, F3 47 / 47, F4 25 / 25 ; total identiques 110 / 110 ; bouchons atteints : aucun | 7 / 7 (F1 2 / 2, F2 1 / 1, F3 2 / 2, F4 2 / 2) | `minIdenticalJ4 = 7`, `minIdenticalBridge = 110` |

Task 4 : les deux cas F1/F2 encore différents (`gom/TestTermgraph.t`,
`sl/TestImportStrat.t`) s'arrêtent sur `buildGetHeadDecl` (famille F3,
Task 5), aucune méthode de F2 ; le plan en attendait 38.

### P2 par le binaire (Task 7, `mage backendCheck`)

`tom -go -np --goFrom Backend` : T1 83 / 83 goldens `.go` identiques
(`minGoldensGo = 83`, goldens non vides), smoke 27 / 27 identiques au
backend Java (`minP2Smoke = 27`) puis exécutés. La sortie standard de
`tomgo` est ignorée quand son code de sortie est non nul
(`GoBridgePlugin`). Contre-épreuves non commitées : un `tomgo` factice qui
ajoute un octet à la ligne `package`, ou écrit `package wrong`, fait échouer
`TestGoBackend/Peano.t` (golden différent) ; un `tomgo` factice qui sort avec
le code 3 fait échouer par `tomgo exited with status 3`.

Budget (spec 5.3), `TestGoBackend`, deux mesures chacune :

| Mode | Durées | Meilleure |
|---|---|---|
| tout Java | 73.398 s, 80.027 s | 73.398 s |
| `--goFrom Backend` | 74.431 s, 82.712 s | 74.431 s |

Rapport 74.431 / 73.398 = 1.014 (et 82.712 / 80.027 = 1.034) : sous 1.15.

Cas d'erreur construits (`TestGoFrom`) : « backend runtime error »
(`noTL()` : `Unhandled exception`, ligne -1) et « backend unknown option »
(`Option 'pretty' not found`, `did not return a PluginOption`,
`Unhandled exception`, dans cet ordre).

Écart `-np` : « labelled %match » (le parseur île ne supporte pas un
`%match` étiqueté, `matchBlock: { %match … break matchBlock; }`), à corriger
plus tard dans le compilateur. Occurrences sur le chemin Go du backend Java :
`GoGenerator.t:595` (`genDeclMake`, portée par `continue` dans la boucle
`for`) et `GoGenerator.t:722` (`buildMethod`, portée par l'indicateur
`matched`). Les autres occurrences (`JavaGenerator`, `CFamilyGenerator`,
`AdaGenerator`, `CamlGenerator`, `CSharpGenerator`, `PythonGenerator`) sont
hors chemin Go (D5). `AbstractGenerator`, `GenericGenerator` et
`BackendPlugin` n'en ont aucune.

# J4.1b — backend Go en Tom+Go (`Collector`, parcours `generate…`, générateur aplati, `tomgo suffix`)

Date : 2026-10-25. Branche `tom-go`.
Plan : `docs/superpowers/plans/2026-10-24-j4-1b-backend.md`.
Spec : `docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md`
(§4.4 structure du code porté, §5.1 P2, §5.3 budget, §6 ligne J4.1b, §8 D4–D5).

Résultat : le backend Go du compilateur existe en Tom+Go
(`src/go/tom/engine/backend` : pilote `Generate`, passe `Collector`, générateur
aplati `goGenerator`) et `tomgo suffix` l'exécute. Sur les 110 paquets pont
(83 ports T1 + 27 cas smoke), la sortie de `tom -go --goFrom Backend` est
identique octet pour octet à celle du backend Java : critère P2 atteint, 110 /
110 en process (`TestBackendCorpus`), 83 / 83 goldens T1 et 27 / 27 cas smoke
par le binaire (`mage backendCheck`). L'état laissé par le `Collector` (marques
« utilisé », inliners) est identique à Java sur 110 / 110 paquets. Le budget est
tenu (rapport 1.014 et 1.034, limite 1.15). `tom -go` a compilé tout le port
**sans modification du compilateur Java** ; T0, T1, T2, T3, smoke, P0 et les
requêtes sont inchangés.

## 1. Ce qui a été fait

| Tâche | Commit | Contenu principal |
|---|---|---|
| plan | `8bbcb711a` | Plan J4.1b. |
| 1 | `718fffc4f` | `collector.t` (passe `Collector` de `BackendPlugin`), `InlinerEntries` ; programme Java `CollectorState` et `TestCollectorState` : état post-`Collector` 110 / 110. |
| 2 | `6da06777f` | Pilote `Generate` (`backend.go`), `generator.t` (type `goGenerator`) et `generate.t` (parcours `generate…` d'`AbstractGenerator`) ; famille F1 ; `tomgo suffix` exécute `Backend`. |
| 3 | `018fa1403` | Compteur P2 en process par famille (`TestBackendCorpus`), harnais à locale fixe, `GoBridgePlugin` relaie les lignes `INFO`. |
| 4 | `5e87fe63c`, `1532b6f75` | Famille F2 : `declarations.t` (fonctions de mapping, termes), `filtrage.t` (instructions, expressions, comparaisons). |
| 5 | `87d7d2763` | Famille F3 : `listes.t` (listes et tableaux). |
| 6 | `4a12ca648` | Famille F4 : `strategies.t` (fonctions, `%strategy`, introspecteur, refus de `%transformation`). |
| 7 | `3a9a7081a` | P2 par le binaire (`mage backendCheck`), budget, cas d'erreur du backend, inventaire vérifié (`TestPortInventory`, `TestNoStubLeft`). |
| 8 | ce commit | Rapport et documentation. |

## 2. Disposition retenue

- Paquetage `tom/engine/backend` (`src/go/tom/engine/backend`, environ 2770
  lignes de `.t`) : `backend.go` (pilote, port de `BackendPlugin.run`),
  `collector.t`, `generator.t` (type `goGenerator` unique : champs des trois
  classes Java, accesseurs), `generate.t` (parcours d'`AbstractGenerator`),
  puis une famille par fichier : `declarations.t` et `filtrage.t` (F2),
  `listes.t` (F3), `strategies.t` (F4) ; F1 (squelette) est dans `generate.t`
  et `generator.t`.
- Aplatissement (spec §4.4, règles R1–R10 du plan) : pour chaque méthode, la
  version la plus dérivée sur le chemin Go (`GoGenerator`, sinon
  `GenericGenerator`, sinon `AbstractGenerator`) ; les méthodes masquées
  appelées par `super.` gardent leur nom suffixé par leur classe. Les autres
  cibles (`CGenerator`, `JavaGenerator`, …) sont omises (D5).
- **Bouchons supprimés** : `notported.go` (les méthodes non portées qui
  paniquaient) n'existe plus, ni dans `src` ni dans `stable/go/tom`
  (`TestNoStubLeft`). Les bouchons atteints au départ de la Task 3 étaient
  `buildIsFsymDecl` 50, `genDeclMake` 49, `buildUnamedBlock` 4,
  `buildMethodDef` 3.
- **Deux compteurs** : l'état post-`Collector` (comparé à Java par réflexion,
  `test/tools/j4/java/CollectorState.java`) localise un écart du `Collector`
  avant P2 ; le compteur P2 (`TestBackendCorpus`) compare `Generate` à la sortie
  Java, par famille, et refuse une sortie attendue vide.
- `tomgo suffix` : `ported = {"backend": true}` ; `tom -go --goFrom Backend`
  produit maintenant le code par `tomgo` ; la sortie standard de `tomgo` est
  ignorée quand son code de sortie est non nul.

## 3. Inventaire

La liste des méthodes portées, classe d'origine et fichier, est celle du plan
(« Inventaire ») ; `TestPortInventory` vérifie que chaque méthode inventoriée
existe dans le paquetage avec le commentaire `// <nom> is <Classe>.<nomJava>
(<Fichier>:<ligne>)`, et `TestNoStubLeft` qu'aucun bouchon ne reste.
Deviations de découpage : `buildInstructionSequence` (inventorié en Task 4) a
été porté en Task 2 (nécessaire à `gom/TestFrom.t`) ; `buildExpGetDefault`,
absent des bouchons, est porté en Task 4 (appelé par `buildTerm`).

## 4. Écarts `// port:` relevés

`grep -n 'port:' -r src/go/tom/engine/backend` :

- `backend.go:18` : l'option d'encodage et le fichier de sortie sont ceux de
  `GoBridgePlugin`.
- `generate.t` (lignes 33, 310, 457, 479, 688) : Java imprime aussi le message
  de diagnostic sur la sortie standard (R8), non porté.
- `generate.t:750` : méthode listée en famille F2 par le plan, portée avec la
  famille voisine.
- `collector.t:89` : `EmptyStackException` rattrapée en Java (« No moduleName
  in … »).
- Écart de langage : une règle `%match` Tom retombe sur la suivante, donc le
  `break matchBlock` Java devient un indicateur `matched` (`buildMethod`) ou un
  `continue` (`genDeclMake`) ; voir §7.

## 5. Compteur P2 par famille et par tâche

Mesures de clôture (`bridgeCheck`, 110 paquets ; `mage j4`, 7 paquets) :

| Famille | `bridgeCheck` | `mage j4` |
|---|---|---|
| F1 squelette | 5 / 5 | 2 / 2 |
| F2 filtrage | 33 / 33 | 1 / 1 |
| F3 listes | 47 / 47 | 2 / 2 |
| F4 stratégies | 25 / 25 | 2 / 2 |
| Total | 110 / 110 | 7 / 7 |

Progression (identiques `bridgeCheck` ; planchers `minIdenticalBridge` et
`minIdenticalJ4`) :

| Tâche | `bridgeCheck` | `mage j4` | Planchers |
|---|---|---|---|
| 3 (F1 partiel) | 4 | 2 | 4 / 2 |
| 4 (F2) | 36 | 3 | 36 / 3 |
| 5 (F3) | 85 | 5 | 85 / 5 |
| 6 (F4) | 110 | 7 | 110 / 7 |

Le plan attendait 5 en F1 puis 38 en Task 4 ; `gom/TestTermgraph.t` et
`sl/TestImportStrat.t` s'arrêtaient sur `buildGetHeadDecl` (F3), d'où 36 et un
rattrapage en Task 5.

## 6. État post-`Collector` et budget

- Post-`Collector` : 110 / 110 identiques (8311 lignes ; planchers 350 pour
  `j4`, mesuré 355, et 8300 pour `bridgeCheck`). Trois paquets ont un état vide
  en Java comme en Go (`Test.t`, `TestFromTerm.t`, `TestPosition.t`, table sans
  symbole) ; le test n'échoue sur un état vide que si la table a des symboles.
- Budget (spec §5.3, `TestGoBackend`, deux mesures) : tout Java 73.398 s et
  80.027 s ; `--goFrom Backend` 74.431 s et 82.712 s ; rapports 1.014 et 1.034
  (limite 1.15).
- Cas d'erreur construits (`TestGoFrom`) : erreur d'exécution du backend
  (`noTL()`), option inconnue (trois messages dans l'ordre).

## 7. Vérifications finales

Sur `src/dist` fraîche (JDK 8), 2026-10-25 :

| Commande | Résultat |
|---|---|
| `mage -d magefiles -w . all` | vert (`engine/{backend,bridge,cmd/tomgo,platform,tools}`, `gom/*`, `library/*`, `tomtest/adt`, `tomtools/smoke`) |
| `mage … backendCheck` | vert : T1 83 / 83 goldens `.go` (`minGoldensGo = 83`), smoke 27 / 27 (`minP2Smoke = 27`) |
| `mage … j4` | `ok tomtools/j4`, `ok tomtools/j4/renamecheck` ; P2 7 / 7, `TestCollectorState`, `TestGoFrom` |
| `mage … bridgeCheck` | P0 1139 / 1139 ; requêtes 110 / 110 ; Collector 110 / 110 (8311 lignes) ; P2 F1 5/5, F2 33/33, F3 47/47, F4 25/25, total 110 / 110 |
| `mage … portGo` | `ok tomtools/parity` ; `doc/go/port-dashboard.md` inchangé (77 OK / 11 OK diagnostics / 9 échoue dans legacy / 2 non supporté / 4 support / 0 divergence / 0 non porté) |
| `mage … coverage` | `ok tomtools/coverage` ; `doc/go/coverage.md` inchangé |
| `mage … portJava` | non relancé : `src/tom` est inchangé depuis la Task 3 (`018fa1403`, relais `INFO`), dont le `portJava` a réussi (238 s) ; planchers T0 102 / 103 inchangés |

## 8. Limites connues et points ouverts pour J4.1c

- **Écart `-np` : « labelled %match »** : le parseur île ne supporte pas un
  `%match` étiqueté (`matchBlock: { %match … break matchBlock; }`). Occurrences
  sur le chemin Go du backend Java : `GoGenerator.t:595` (`genDeclMake`, portée
  avec `continue`) et `GoGenerator.t:722` (`buildMethod`, portée avec
  l'indicateur `matched`). À corriger plus tard dans le compilateur ; pas
  touché ici.
- La sortie standard partielle d'un `tomgo` qui panique est ignorée quand le
  code de sortie est non nul.
- Options et messages : petits écarts avec Java (J4.1a).
- Le Go généré (`X.t.go`) n'est pas formaté par `gofmt`.
- Non-terminaison du filtrage AC (variables d'un `DoWhile` reconstruites
  vides) : défaut connu, partagé avec Java, à traiter à part.
- Les marques « utilisé » partent de celles du paquet pont.
- **J4.1c (prochain plan)** : faire du backend Go la référence : `promoteGo` du
  backend, P3 « self-T1 » (le compilateur construit avec le backend Go
  reproduit les goldens T1), `mage bootstrap` sans Java ; limites du parseur
  île rencontrées ; corrections éventuelles du `GoGenerator` Java.

## 9. Tout relancer

```bash
unset TOM_HOME
./build.sh stable && ./build.sh src.dist
mage -d magefiles -w . regenTomGo      # attendu : 0 written
mage -d magefiles -w . all
mage -d magefiles -w . j4
mage -d magefiles -w . bridgeCheck
mage -d magefiles -w . backendCheck    # P2 par le binaire tomgo
mage -d magefiles -w . portGo && mage -d magefiles -w . coverage
mage -d magefiles -w . portJava        # ~4 min, seulement si src/tom change
(cd test/tools && go test -count=1 ./smoke/)
(cd magefiles && go test -tags mage ./...)
```

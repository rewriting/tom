# Tableau de bord J4 (pont Java -> Go)

Spec §5.2 : `docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md`.
Mesures du 2026-10-24 (J4.0d).

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

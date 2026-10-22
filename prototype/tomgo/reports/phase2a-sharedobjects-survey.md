# Phase 2a — Survey de `shared-objects.jar` (cible du portage Go)

## Source

`stable/lib/runtime/shared-objects.jar` (CWI + INRIA, 2003-2007, BSD).
Le `.jar` embarque ses **propres sources** Java sous `src/shared/` — pas
besoin de décompiler. Extraction :

```bash
unzip -o stable/lib/runtime/shared-objects.jar -d /tmp/sharedobj
ls /tmp/sharedobj/src/shared/
# HashFunctions.java
# SharedObject.java
# SharedObjectFactory.java
# SharedObjectWithID.java
# SingletonSharedObjectFactory.java
```

## API à porter

### `SharedObject` (interface)

```java
public interface SharedObject {
    SharedObject duplicate();              // clone du prototype
    boolean      equivalent(SharedObject); // égalité structurelle
    int          hashCode();               // hash mémoïsé typiquement
}
```

### `SharedObjectWithID extends SharedObject`

```java
int  getUniqueIdentifier();
void setUniqueIdentifier(int id);
```

Optionnel pour l'amorce : nécessaire seulement pour les usages qui
demandent un identifiant entier dense (rares dans Gom). À ne pas porter
en V1.

### `SharedObjectFactory` (classe)

```java
SharedObject build(SharedObject prototype); // hash-cons : renvoie l'instance partagée
boolean      contains(SharedObject);
void         cleanup();                      // purge des entrées GC'd
```

Internes (à NE PAS reproduire à l'identique en Go) :

- 32 **segments** (5 bits de poids fort du hash) pour limiter la
  contention multi-thread.
- Chaque segment = hashtable à buckets chaînés, **WeakReference** pour
  laisser le GC nettoyer les termes non référencés. Rehash quand `load >
  threshold` (load factor 2.0). Cleanup déclenché par un
  `GarbageCollectionDetector` à finaliseur.
- `EntryWithID` pour les `SharedObjectWithID`, avec gestion d'IDs libres.

## Stratégie de portage Go (V1, minimaliste)

1. **Pas de WeakReference en V1** : Go n'expose pas de référence faible
   utilisable simplement. Les termes restent vivants tant que la factory
   vit. Pour l'amorce, c'est acceptable — les programmes générés sont des
   tests courts. Une variante avec `runtime.SetFinalizer` + table de
   `weak.Pointer` (Go 1.24+) pourra venir plus tard si besoin.
2. **Une seule table** (pas de segments) protégée par `sync.RWMutex`.
   La granularité fine est une optim avancée, non nécessaire au jalon.
3. **Interface Go** :
   ```go
   type Term interface {
       Equivalent(Term) bool
       Hash() uint32
       Duplicate() Term    // prototype → instance allouée
   }
   type Factory struct { /* … */ }
   func (f *Factory) Build(prototype Term) Term
   func (f *Factory) Contains(t Term) bool
   func (f *Factory) Stats() Stats
   ```
4. **Hash fonctionnel** : on inclut une `HashFunctions` Go avec la
   variante `oneAtATime` (la plus utilisée dans Gom) pour les
   constructeurs générés.
5. **Tests** : preuve de partage (`Build(a)==Build(b)` quand `a.Equivalent(b)`),
   non-partage entre symboles distincts, statistiques (nombre d'entrées,
   collisions, etc.).

## Hors V1 (différé)

- WeakReference / cleanup automatique.
- `SharedObjectWithID` + segments avec compteur d'IDs.
- `SingletonSharedObjectFactory` (un singleton par classe Java).

## Référence

- Sources extraites : `/tmp/sharedobj/src/shared/`.
- Implémentation cible : `tomgo/internal/library/sharedobjects/`.
- Algorithme `build` (Java l. 110-115 + l. 422-467) :
  1. `hash := prototype.hashCode()`
  2. lookup dans la bucket `hash & mask` — si trouvé via
     `prototype.equivalent(found)`, retourne l'existant
  3. sinon `result := prototype.duplicate()`, insère, retourne

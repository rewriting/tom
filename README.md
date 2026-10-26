# TOM - To One Matching Compiler

## What is it?

  Tom is a pattern matching compiler, developed at Universite de Lorraine and
  Inria. It is particularly well-suited for programming various transformations
  on trees/terms.


## Two versions in this repository

- [`legacy/`](legacy/README.md) — the original Tom+Java compiler, exactly as
  written by its human authors (frozen copy of commit `2918e95c7`).
  Start there to install the historical Tom; see its README.
- The repository root (`src/`, `stable/`) — the AI-assisted extension of
  that compiler with a Go target. Its Go side lives in
  [`src/go/tom`](src/go/tom) (module `tom`: runtime libraries and the Gom
  compiler for Go), [`test/java`](test/java) (the Tom+Java tests),
  [`test/go`](test/go) (Tom+Go ports and reference data),
  [`test/tools`](test/tools) (harnesses comparing Java, Go and `legacy/`) and [`doc/go`](doc/go) (reports); orchestration with
  `mage -d magefiles -w . -l`. [`prototype/tomgo`](prototype/tomgo/README.md)
  is the frozen hand-written Go prototype.

### Getting the last 100% human-written version from git

The last commit written entirely by the human authors of Tom is
`2918e95c7`, tagged `v3-human-final`. Everything after it was written with
the help of an AI assistant. To work on that version:

```bash
git clone git@github.com:rewriting/tom.git
cd tom
git switch -c human v3-human-final   # or: git checkout v3-human-final
```

The same sources are also kept, frozen, in [`legacy/`](legacy/README.md) of
the current tree, with their installation instructions.

## Documentation

The documentation is available [here](doc/README.md)

## Licensing

The Tom compiler is licensed under the GNU General Public License
(GNU GPL, available at <http://www.gnu.org/licenses/>).

The predefined mappings ($TOM_HOME/share/tom), the runtime library,
and the examples are licensed under the BSD License
(available at <http://www.opensource.org/licenses/bsd-license.php>).

Thanks for using Tom.

The Tom Project

# Tom — original human-written compiler (legacy snapshot)

This directory is a frozen copy of the Tom compiler (Tom+Java sources and
the bootstrapped Java compiler) exactly as written by its human authors.
It is taken verbatim from commit `2918e95c7` (tip of branch `v3`,
29 Aug 2019) and is **never modified**: it is kept as a witness of that
work and as the reference oracle for the Go extension developed at the
repository root.

The repository root contains the AI-assisted extension of this compiler
(Go target, see `../doc/go/README.md`).

## Requirements

- A JDK 8 (`java -version` must report `1.8`). The build bundles Ant
  (`stable/lib/compiletime/`), nothing else is needed.
  Recent JDKs (24 and later) cannot run this snapshot's build: Ant runs
  Tom in-process and relies on `System.setSecurityManager`, which these
  JDKs removed.
- A POSIX shell (Linux, macOS).

## Build

    git clone https://github.com/rewriting/tom.git
    cd tom/legacy
    unset TOM_HOME            # an existing TOM_HOME would shadow this build
    ./build.sh stable         # bootstrap compiler (pure Java) -> stable/dist
    ./build.sh src.dist       # Tom+Java sources compiled by stable -> src/dist

## Install

    export TOM_HOME=$PWD/src/dist
    export PATH=$TOM_HOME/bin:$PATH

`tom` and `gom` are now on the PATH.

## Check

    mkdir -p /tmp/tomcheck && cd /tmp/tomcheck
    cat > Hello.t <<'EOF'
    public class Hello {
      %include { string.tom }
      public static void main(String[] args) {
        String s = "world";
        %match(String s) {
          x -> { System.out.println("hello " + `x); }
        }
      }
    }
    EOF
    tom Hello.t
    javac -cp $TOM_HOME/lib/tom-runtime-full.jar Hello.java
    java  -cp $TOM_HOME/lib/tom-runtime-full.jar:. Hello     # hello world

## Layout

| Path | Content |
|---|---|
| `src/` | Tom+Java sources of the compiler (`src/tom/engine`, `src/tom/gom`, …) |
| `stable/` | bootstrapped Java compiler used to compile `src/` |
| `share/`, `utils/` | man pages, launcher templates |

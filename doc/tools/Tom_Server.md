---
title: Documentation:Tom Server
permalink: /Documentation:Tom_Server/
---

Tom server is an experimental way of running tom without paying the cost of the JVM and Tom library loading. It consists in a server that is meant to be run as a local deamon and a client that acts as the “tom” command.

Using Tom Server
================

-   Install [Jython ≥2.5b1](http://www.jython.org/Project)
-   \[Optional\] Change the PORT variable in `$TOM_HOME/bin/tom-server.py` and `$TOM_HOME/bin/tom-client`
-   Run the server: `tom-server`
-   Compile your tom files as usual using the client: `tom-client OPTIONS FILES`

Interfacing Tom with GNU Emacs' Flymake
=======================================

[Flymake](http://www.gnu.org/software/emacs/manual/html_node/emacs/Flymake.html) is a feature of Emacs 22 that enables on-the-fly compilation *a la* Eclipse with little effort. Tom server is sufficiently quick to allow flymake to reasonably interoperate with tom. Here comes the [mandatory screenshot](http://www.loria.fr/~brauner/emacs.png).

-   Add the following lines to your `.emacs` (provided that you already have added `jtom-mode.el` to site-lisp)

``` lisp
(require 'flymake)

(autoload 'jtom-mode "jtom-mode" "jtom mode" t)
(setq auto-mode-alist (append '(("\\.t$" . jtom-mode))
                              auto-mode-alist))

(setq flymake-allowed-file-name-masks
      (cons '(".+\\.t$"
              flymake-simple-make-init
              flymake-simple-cleanup
              flymake-get-real-file-name)
            flymake-allowed-file-name-masks))

(add-hook
 'jtom-mode-hook
 '(lambda ()
    (if (not (null buffer-file-name)) (flymake-mode))))
```

-   Add `jtom/utils/tom-server/tom-flymake.py` to your path
-   Run the server
-   In your working directory, add the following `Makefile`

`check-syntax:`
`   tom-flymake.py ${CHK_SOURCES}`

Customize the options if needed (`--noOutput` and `--wall` by default)

-   Edit a tom file in the same directory: that's it !

[Category:Documentation](/Category:Documentation "wikilink")
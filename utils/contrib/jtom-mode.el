(defconst jtom-version "1.0"
  "jtom mode version number.")

;;; Commentary:

;; This module derives an editing mode from java-mode.  The derived mode
;; is for editing jtom code.  

(require 'cc-mode)
(require 'font-lock)

(c-lang-defconst c-block-stmt-2-kwds 
	jtom (append '("%match" "match") 
							 (c-lang-const c-block-stmt-2-kwds) nil))

(c-lang-defconst c-type-prefix-kwds
	jtom (append '("typeterm" "%typeterm")
							 (c-lang-const c-type-prefix-kwds) nil))

(c-lang-defconst c-block-stmt-1-kwds
	jtom (append '("implement")
							 (c-lang-const c-block-stmt-1-kwds) nil))

(c-lang-defconst c-paren-nontype-kwds
	jtom (append '("get_fun_sym" "cmp_fun_sym" "get_subterm" "equals")
							 (c-lang-const c-paren-nontype-kwds) nil))

(font-lock-add-keywords 'jtom-mode
												'(("%match"    . font-lock-keyword-face)
													("%include"  . c-preprocessor-face-name)
													("%typeterm" . font-lock-keyword-face)
													("%typelist" . font-lock-keyword-face)
													("%op"       (1 font-lock-keyword-face) 
													 font-lock-match-c-style-declaration-item-and-skip-to-next
													 ))
													)

(define-derived-mode
  jtom-mode java-mode "JTom"
  "*Major mode for editing JTom code
jtom-mode is an extension of
java-mode.  Use the hook `jtom-mode-hook to execute code when
entering jtom mode.
\\{jtom-mode-map}"
  )

(add-to-list 'auto-mode-alist '("\\.t\\'" . jtom-mode))
(add-to-list 'auto-mode-alist '("\\.tom\\'" . jtom-mode))

(provide 'jtom-mode)

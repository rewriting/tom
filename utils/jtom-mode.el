(defconst jtom-version "1.0"
  "jtom mode version number.")

;;; Commentary:

;; This module derives an editing mode from java-mode.  The derived mode
;; is for editing jtom code.  

(require 'cc-mode)
(require 'font-lock)

(define-derived-mode
  jtom-mode java-mode "JTom"
  "*Major mode for editing JTom code
jtom-mode is an extension of
java-mode.  Use the hook `jtom-mode-hook to execute code when
entering jtom mode.
\\{jtom-mode-map}"
  )

(add-to-list 'auto-mode-alist '("\\.t\\'" . jtom-mode))

(provide 'jtom-mode)

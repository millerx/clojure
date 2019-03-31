(ns pegthing.print
  (require [clojure.string :as str]))
  (:gen-class))

; Types:
; pegStr: Letter + peg. Ex.) A0 B-

(defn pos-char [pos]
  (char (+ 65 pos)))

(defn peg-char [b]
  (if ? \0 \-))

; Turns a board into a seq of pegStrs.
; board -> [pegStr]
;(defn board->pegStrs [board]
;  (map #(str ) board))

; Prints the board to the screen
; board -> board
(defn print-board [board]
  (println board))
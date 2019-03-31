(ns pegthing.io
  (require [clojure.string :as str])
  (require [pegthing.core :as pt])
  (:gen-class))

; Prompts and then waits for input until newline is read.
; Input is then trimmed.
; If trimmed input is empty then default is returned.
; f('', '') -> ''
(defn prompt [pstr default]
  (println (format "%s [%s]" pstr default))
  (let [input (str/trim (read-line))]
    (if (empty? input)
      default
      input)))

(defn prompt-rows []
  (prompt "How many rows?" 5))

; Prompts for an initial board.
; nil -> board
(defn prompt-for-initial-board []
  (println "init board?")
  :board)

; Prompts for next move.
; nil -> move
(defn prompt-for-move []
  (println "move?")
  :move)

; Prints the board to the screen
; board -> board
(defn print-board [board]
  (println board))

(defn -main [& args]
  (println "Get ready to play peg thing!")
  (loop [board (prompt-for-initial-board)]
    (let [move (prompt-for-move)
          new-board (pt/make-move move board)]
      (print-board new-board)
      (if (pt/has-won? new-board)
        (println "You have won!")
        (recur new-board)))))
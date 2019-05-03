(ns pegthing.io
  (require [clojure.string :as str]
    [pegthing.core :refer :all]
    [pegthing.parsing :refer :all]))

; For testing.
(def pt-read-line clojure.core/read-line)
(def pt-println clojure.core/println)

; Prompts for input and validates it.
; If the validation-fn is falsy or throws then the prompt is repeated.
(defn- prompt [prompt-str validation-fn]
  (first (drop-while nil? (repeatedly #(do
    (pt-println prompt-str)
    (validation-fn (pt-read-line)))))))

; Parses a String into an Integer. Returns nil if parse fails.
(defn- parse-int [str]
  (try
    (Integer/parseInt str)
    (catch NumberFormatException _ nil)))

; Prompts for an initial board.
(defn prompt-for-initial-board []
  (let [DEFAULT-ROWS 5]
    (prompt (format "How many rows? [%d]" DEFAULT-ROWS)
        #(if (empty? %) DEFAULT-ROWS (parse-int %)))))

; Creates the initial board. Prompts for board size and removes a peg.
(defn- create-initial-board []
  (let [board (create-board (prompt-for-initial-board))]
    (remove-peg board [0 0]))) ; TODO: Random peg

; Prompts for a move.
; board is passed in to validate the move.
(defn prompt-for-move [board]
  (prompt "Move?" #(validate-move board (parse-move %))))

; Entry point.
(defn -main [& args]
  (pt-println "Get ready to play peg thing!")
  (loop [board (create-initial-board)]
    (pt-println)
    (pt-println (board-str board))
    (if (winner? board)
      (pt-println "You have won!")
      (recur (make-move board (prompt-for-move board))))))

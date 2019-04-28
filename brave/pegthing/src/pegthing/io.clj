(ns pegthing.io
  (require [clojure.string :as str])
  (require [pegthing.core :as pt]))

(def ASCII-A 65)
(def ASCII-0 48)

; Prompts for input and validates it.
; If the validation-fn is falsy or throws then the prompt is repeated.
(defn prompt [prompt-str validation-fn]
  (first (drop-while nil? (repeatedly #(do
    (println prompt-str)
    (validation-fn (read-line)))))))

; Parses a String into an Integer. Returns nil if parse fails.
(defn parse-int [str]
  (try
    (Integer/parseInt str)
    (catch NumberFormatException _ nil)))

; Prompts for an initial board.
(defn prompt-for-initial-board []
  (let [DEFAULT-ROWS 5]
    (pt/create-board
      (prompt (format "How many rows? [%d]" DEFAULT-ROWS)
        #(if (empty? %) DEFAULT-ROWS (parse-int %))))))

; Creates the initial board. Prompts for board size and removes a random peg.
(defn create-initial-board []
  (let [board (prompt-for-initial-board)]
    (pt/remove-peg board [2 2]))) ; TODO: Random peg

; Turns a peg into a string. Ex A1:*
(defn peg-str [row col peg]
  (let [rowChar (char (+ ASCII-A row))
        pegChar ([\_ \*] peg)]
      (format "%c%d:%c" rowChar (+ 1 col) pegChar)))

; Turns a board row into a string.
(defn board-row-str [row board-row base-size]
  (let [SPACES "    "]
    (str/join (concat
      (repeat (- base-size row 1) SPACES) ; Padding
      (str/join SPACES (map-indexed #(peg-str row %1 %2) board-row))))))

; Prints the board to the screen.
(defn print-board [board]
  (let [base-size (pt/base-size board)]
    (run! println (map-indexed #(board-row-str %1 %2 base-size) board))))

; Parses a string into a position. Ex.) C1 -> [2 0]
(defn- parse-pos [[row col]]
  [(- (int row) ASCII-A)
   (- (int col) ASCII-0 1)])

; Parses a string into a move. Ex.) "A1 C3" -> [[0 0] [2 2]]
; If the given string is not valid then nil is returned.
(defn parse-move [str]
  (if-let [match (re-matches #"([A-Z][0-9]) ([A-Z][0-9])" (.toUpperCase str))]
    (map parse-pos (rest match))))

; Makes a move provided by the user.
(defn make-move [board]
  (pt/make-move board (prompt "Move?"
    ; TODO: Propagte nil from parse-move
    #(pt/validate-move board (parse-move %)))))

; Entry point.
(defn -main [& args]
  (println "Get ready to play peg thing!")
  (loop [board (create-initial-board)]
    (println)
    (print-board board)
    (if (pt/winner? board)
      (println "You have won!")
      (recur (make-move board)))))

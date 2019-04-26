(ns pegthing.io
  (require [clojure.string :as str])
  (require [pegthing.core :as pt]))

; Prompts for input and validates it.
; If the validation-fn throws then the prompt is repeated.
(defn prompt [prompt-str validation-fn]
  (first (drop-while #{::validation-error} (repeatedly
    #(try
      (println prompt-str)
      (let [raw-input (read-line)
            valid-input (validation-fn raw-input)]
        ; Pad last input with a newline if user did not type anything visible.
        (if-not (empty? (str/trim raw-input)) (println))
        valid-input)
      (catch Exception _ ::validation-error))))))

; Prompts for an initial board.
(defn prompt-for-initial-board []
  (pt/create-board (let [default-rows 5]
    (prompt (format "How many rows? [%d]" default-rows)
      #(if (empty? %) default-rows (Integer/parseInt %))))))

; Creates the initial board. Prompts for board size and removes a random peg.
(defn create-initial-board []
  (let [board (prompt-for-initial-board)]
    (pt/remove-peg board [2 2]))) ; TODO: Random peg

; Prints the board to the screen.
; TODO: A stub.
(defn print-board [board]
  (println board))

; Parses a string into a move. Ex.) "A0 C2" -> [[0 0] [2 2]]
; If the given string is not valid then nil is returned.
(defn parse-move [str]
  (if-let [match (re-matches #"([A-Z][0-9]) ([A-Z][0-9])" (.toUpperCase str))]
    (map (fn [[row col]]
      [(- (int row) 65) ; ASCII "A"=65
       (- (int col) 48)]) ; ASCII "0"=48
      (rest match))))

; Makes a move provided by the user.
(defn make-move [board]
  (pt/make-move board (prompt "Move?"
    #(pt/validate-move board (parse-move %)))))

; Entry point.
(defn -main [& args]
  (println "Get ready to play peg thing!")
  (loop [board (create-initial-board)]
    (print-board board)
    (if (pt/winner? board)
      (println "You have won!")
      (recur (make-move board)))))

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

; Prints the board to the screen
; TODO: A stub.
(defn print-board [board]
  (println board))

; Prompts for next move.
(defn prompt-for-move []
  (prompt "move?" ""))

; TODO: A stub until prompt-for-move is imlemented.
(defn make-move [move board]
  (println "Performing move" move)
  board)

(defn -main [& args]
  (println "Get ready to play peg thing!")
  (loop [board (prompt-for-initial-board)]
    (print-board board)
    (if (pt/winner? board)
      (println "You have won!")
      (recur (make-move (prompt-for-move) board)))))

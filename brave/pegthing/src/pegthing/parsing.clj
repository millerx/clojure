;; Functions to convert the data structures from core.clj to and from strings.
(ns pegthing.parsing
  (require [clojure.string :as str]
    [pegthing.core :refer :all]))

(def ^:private ASCII-A 65)
(def ^:private ASCII-0 48)

; Converts a peg into a string. Ex A1:*
(defn- peg-str [row col peg]
  (let [rowChar (char (+ ASCII-A row))
        pegChar ([\_ \*] peg)]
      (format "%c%d:%c" rowChar (+ 1 col) pegChar)))

; Converts a board row into a string.
(defn- board-row-str [row board-row base-size]
  (let [SPACES "    "]
    (str/join (concat
      (repeat (- base-size row 1) SPACES) ; Padding
      (str/join SPACES (map-indexed #(peg-str row %1 %2) board-row))))))

; Converts a board into a string.
(defn board-str [board]
  (let [base-size (base-size board)]
    (str/join "\n" (map-indexed #(board-row-str %1 %2 base-size) board))))

; Parses a string into a position. Ex.) C1 -> [2 0]
(defn- parse-pos [[row col]]
  [(- (int row) ASCII-A)
   (- (int col) ASCII-0 1)])

; Parses a string into a move. Ex.) "A1 C3" -> [[0 0] [2 2]]
; If the given string is not valid then nil is returned.
(defn parse-move [str]
  (if-let [match (re-matches #"([A-Z][0-9]) ([A-Z][0-9])" (.toUpperCase str))]
    (map parse-pos (rest match))))

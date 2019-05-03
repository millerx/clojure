(ns pegthing.parsing-test
  (:require [clojure.test :refer :all]
    [clojure.string :as str]
    [pegthing.parsing :refer :all]
    [pegthing.core :refer :all]))

(deftest test-parse-move
  (is (= [[0 0] [2 2]] (parse-move "A1 C3")))
  (is (= [[0 0] [2 2]] (parse-move "a1 c3")) "Lower case")
  (is (nil? (parse-move "54 c5")) "Invalid input"))

(deftest test-print-board
  (is (= "A1:*" (board-str [[1]])) "One row")
  (is (= "" (board-str [[]])) "Zero rows")
  (is (= (board-str (remove-peg (create-board 4) [2 1]))  (str/join "\n"
["            A1:*"
"        B1:*    B2:*"
"    C1:*    C2:_    C3:*"
"D1:*    D2:*    D3:*    D4:*"]))))

(ns pegthing.io-test
  (:require [clojure.test :refer :all]
            [pegthing.io :refer :all]
            [pegthing.core :as pt]))

(def ^:dynamic *mockin*)
(def ^:dynamic *mockout*)

(defn mock-println [& more]
  (set! *mockout* (conj *mockout* more)))

(defn mock-read-line []
  (let [[line & rest] *mockin*]
    (set! *mockin* rest)
    line))

(defmacro with-io
  [inputs & body]
  `(binding [*mockin* ~inputs *mockout* []]
    (with-redefs [pegthing.io/pt-println mock-println
                  pegthing.io/pt-read-line mock-read-line]
      ~@body)))

(deftest test-parse-move
  (is (= [[0 0] [2 2]] (parse-move "A1 C3")))
  (is (= [[0 0] [2 2]] (parse-move "a1 c3")) "Lower case")
  (is (nil? (parse-move "54 c5")) "Invalid input"))

(deftest test-print-board
  (testing "Board with one row"
    (with-io nil
      (print-board [[1]])
      (is (= *mockout* [["A1:*"]]))))
  (testing "Board with zero rows"
    (with-io nil
      (print-board [[]])
      (is (= *mockout* [[""]]))))
  (testing "A standard board"
    (with-io nil
      (let [board (pt/remove-peg (pt/create-board 4) [1 1])]
        (print-board board)
        (is (= *mockout*) [
["            A1:*"]
["        B1:*    B2:*"]
["    C1:*    C2:_    C3:*"]
["D1:*    D2:*    D3:*    D4:*"]])))))

(deftest test-prompt-for-initial-board
  (let [prompt ["How many rows? [5]"]]
    (testing "Default value"
      (with-io '("")
        (is (= 5 (prompt-for-initial-board)) "Default value")
        (is (= [prompt] *mockout*))))
    (testing "Another value"
      (with-io '("3")
        (is (= 3 (prompt-for-initial-board)))))
    (testing "Bad input"
      (with-io '("foo" "") ; Enter bad input "foo" then "" for default value.
        (is (= 5 (prompt-for-initial-board)) "Default value")
        (is (= (repeat 2 prompt) *mockout*) "Prompted twice")))))

(deftest test-prompt-for-move
  (let [prompt ["Move?"]
        board (pt/remove-peg (pt/create-board 3) [2 2])]
    (testing "Valid move"
      (with-io '("a1 c3")
        (is (= [[0 0] [2 2]] (prompt-for-move board)))
        (is (= [prompt] *mockout*))))
    (testing "Invalid move"
      (with-io '("a1 d4" "a1 c3")
        (is (= [[0 0] [2 2]] (prompt-for-move board)))
        (is (= (repeat 2 prompt) *mockout*) "Prompted twice")))
    (testing "Invalid input"
      (with-io '("foo" "a1 c3")
        (is (= [[0 0] [2 2]] (prompt-for-move board)))
        (is (= (repeat 2 prompt) *mockout*) "Prompted twice")))))

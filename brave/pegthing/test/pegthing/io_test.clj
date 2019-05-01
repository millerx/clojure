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

(defmacro testing-io
  [string & body]
  `(binding [*mockout* [] ; *mockin* should be bound with test data.
             *testing-contexts* (conj *testing-contexts* ~string)]
     (with-redefs [pegthing.io/pt-println mock-println
                   pegthing.io/pt-read-line mock-read-line]
       ~@body)))

(deftest test-parse-move
  (testing "Parse move"
    (is (= [[0 0] [2 2]] (parse-move "A1 C3")))
    (is (= [[0 0] [2 2]] (parse-move "a1 c3"))) ; Lower case
    (is (nil? (parse-move "54 c5"))))) ; Invalid move string

(deftest test-print-board
  (testing-io "Board with one row"
    (print-board [[1]])
    (is (= *mockout* [["A1:*"]])))
  (testing-io "Board with zero rows"
    (print-board [[]])
    (is (= *mockout* [[""]])))
  (testing-io "A standard board"
    (let [board (pt/remove-peg (pt/create-board 4) [1 1])]
      (print-board board)
      (is (= *mockout*) [
        ["            A1:*"]
        ["        B1:*    B2:*"]
        ["    C1:*    C2:_    C3:*"]
        ["D1:*    D2:*    D3:*    D4:*"]]))))

(deftest test-prompt-for-initial-board
  (testing-io "Default value"
    (binding [*mockin* '("")]
      (is (= 5 (prompt-for-initial-board)))
      (is (= [["How many rows? [5]"]] *mockout*))))
  (testing-io "Another value"
    (binding [*mockin* '("3")]
      (is (= 3 (prompt-for-initial-board)))))
  (testing-io "Bad input"
    ; Enter bad input "foo". Then we are prompted again and take the default value.
    (binding [*mockin* '("foo" "")]
      (is (= 5 (prompt-for-initial-board)))
      (is (= (repeat 2 ["How many rows? [5]"]) *mockout*)))))

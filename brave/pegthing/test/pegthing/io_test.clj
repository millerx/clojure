(ns pegthing.io-test
  (:require [clojure.test :refer :all]
    [pegthing.io :refer :all]
    [pegthing.core :refer :all]))

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
        board (remove-peg (create-board 3) [2 2])]
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

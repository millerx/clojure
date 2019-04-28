(ns pegthing.io-test
  (:require [clojure.test :refer :all]
            [pegthing.io :refer :all]))

(deftest test-parse-move
  (testing "Parse move"
    (is (= [[0 0] [2 2]] (parse-move "A1 C3")))
    (is (= [[0 0] [2 2]] (parse-move "a1 c3"))) ; Lower case
    (is (nil? (parse-move "54 c5"))))) ; Invalid move string

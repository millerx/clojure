(ns pegthing.io-test
  (:require [clojure.test :refer :all]
            [pegthing.io :refer :all]))

(deftest test-parse-move
  (testing "Parse move"
    (is (= [[0 0] [2 2]] (parse-move "A0 C2")))
    (is (= [[0 0] [2 2]] (parse-move "a0 c2"))) ; Lower case
    (is (nil? (parse-move "54 c5"))))) ; Invalid move string

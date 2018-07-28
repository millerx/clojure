(ns read.tokenize-test
  (:require [clojure.test :refer :all]
            [read.tokenize :refer :all]))

;(deftest a-test
;  (testing "FIXME, I fail."
;    (is (= 0 1))))

; (tokenize "PRINT \"Hello World\"")

(deftest tokenize-test
	(testing "Cmd int arg."
    (is (= ["PRINT" "5"]
           (tokenize "PRINT 5"))))
  (testing "Cmd str"
    (is (= ["PRINT" "Hello"]
           (tokenize "PRINT \"Hello\""))))
  (testing "Cmd str with spaces"
    (is (= ["PRINT" "Hello World"]
           (tokenize "PRINT \"Hello World\""))))
  (testing "Cmd str exp"
           (is (= ["PRINT" "Hello" "2" "+" "3"]
                  (tokenize "PRINT \"Hello\" 2 + 3")))))
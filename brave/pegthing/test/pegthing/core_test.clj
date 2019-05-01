(ns pegthing.core-test
  (:require [clojure.test :refer :all]
            [pegthing.core :refer :all]))

(deftest test-create-board
  (is (= [] (create-board 0)))
  (is (= [[1]] (create-board 1)))
  (is (= [[1] [1 1]] (create-board 2))))

(deftest test-base-size
  (let [test-base-size (fn [size] (= (base-size (create-board size))))]
    (is (test-base-size 0))
    (is (test-base-size 1))
    (is (test-base-size 2))))

(deftest test-valid-move
  (testing "Valid moves"
    (let [board 
  [[1]
  [0 1]
 [0 0 0]
[0 0 1 1]]]
      (is (valid-move? board [[0 0] [2 2]]) "From first peg")
      (is (valid-move? board [[3 3] [3 1]]) "From last peg")))
  (testing "Invalid moves"
    (let [board
  [[1]
  [0 0]
 [0 0 1]
[0 0 1 1]]]
      (is (not (valid-move? board [[-1 -1] [1 1]])) "Start off board")
      (is (not (valid-move? board [[2 2] [4 4]])) "End off board")
      (is (not (valid-move? board [[1 0] [1 2]])) "Dest is off-board to the right")))
  (testing "More invalid moves"
    (let [board
  [[1]
  [0 0]
 [0 1 0]
[1 1 0 0]]]
      (is (not (valid-move? board [[0 0] [2 2]])) "No peg to jump")
      (is (not (valid-move? board [[2 0] [2 2]])) "No peg at starting pos")
      (is (not (valid-move? board [[3 0] [3 3]])) "Too far of a jump")
      (is (not (valid-move? board [[0 0] [0 0]])) "Not far enough to jump")
      (is (not (valid-move? board [[0 0] [1 1]])) "Not far enough to jump")))
  (testing "Zero-length board"
    (is (not (valid-move? [] [[0 0] [2 2]])) "[0 0] is not valid on a zero-length board")))

(deftest test-validate-move
  (let [move [[0 0] [2 2]]
        board [[1] [0 1] [0 0 0]]]
    (is (= move (validate-move board move)) "Valid move")
    (is (nil? (validate-move [] move)) "Invalid. Out of bounds")
    (is (nil? (validate-move board nil)) "Propagate nil")))

(deftest test-make-move
  (is (=
 [[0]
 [0 0] 
[0 0 1]]
    (make-move
 [[1]
 [0 1]
[0 0 0]]
      [[0 0] [2 2]]))))

(deftest test-winner?
  (is (winner?
 [[0]
 [0 0] 
[0 0 1]]) "Single peg")
  (is (not (winner?
 [[1]
 [0 1]
[0 0 0]])) "Multiple pegs")
  (is (winner? [[0] [0 0] [0 0 0]]) "No pegs")
  (is (winner? [[1]]) "1-peg board with peg")
  (is (winner? [[0]]) "1-peg board without peg")
  (is (winner? []) "zero-length board"))

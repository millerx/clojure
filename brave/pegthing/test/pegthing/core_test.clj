(ns pegthing.core-test
  (:require [clojure.test :refer :all]
            [pegthing.core :refer :all]))

(deftest test-create-board
  (testing "Create board"
    (is (= [] (create-board 0)))
    (is (= [[1]] (create-board 1)))
    (is (= [[1] [1 1]] (create-board 2)))))

(deftest test-base-size
  (testing "Calcualte board's base size"
    (let [test-fn #(= % (base-size (create-board %)))]
      (is (test-fn 0))
      (is (test-fn 1))
      (is (test-fn 2)))))

(deftest test-valid-move
  (testing "Valid moves"
    (let [board 
  [[1]
  [0 1]
 [0 0 0]
[0 0 1 1]]]
      (is (valid-move? board [[0 0] [2 2]])) ; From first peg
      (is (valid-move? board [[3 3] [3 1]])))) ; From last peg
  (testing "Invalid moves"
    (let [board
  [[1]
  [0 0]
 [0 0 1]
[0 0 1 1]]]
      (is (not (valid-move? board [[-1 -1] [1 1]]))) ; Start off board
      (is (not (valid-move? board [[2 2] [4 4]]))) ; End off board
      (is (not (valid-move? board [[1 0] [1 2]]))))) ; Dest is off-board to the right
  (testing "More invalid moves"
    (let [board
  [[1]
  [0 0]
 [0 1 0]
[1 1 0 0]]]
      (is (not (valid-move? board [[0 0] [2 2]]))) ; No peg to jump
      (is (not (valid-move? board [[2 0] [2 2]]))) ; No peg at starting pos
      (is (not (valid-move? board [[3 0] [3 3]]))) ; Too far of a jump
      (is (not (valid-move? board [[0 0] [0 0]]))) ; Not far enough to jump
      (is (not (valid-move? board [[0 0] [1 1]]))))) ; Not far enough to jump
  (testing "Zero-length board"
    (is (not (valid-move? [] [[0 0] [2 2]]))))) ; Even 0,0 is not valid on a zero-length board.

(deftest test-validate-move
  (testing "Test validation of a move"
    (let [move [[0 0] [2 2]]
          board [[1] [0 1] [0 0 0]]]
      (is (= move (validate-move board move))) ; Valid move.
      (is (nil? (validate-move [] move))) ; Invalid move. Position out of bounds.
      (is (nil? (validate-move board nil)))))) ; Propagate nil move.

(deftest test-make-move
  (testing "Test making some moves"
    (is (=
 [[0]
 [0 0] 
[0 0 1]]
      (make-move
 [[1]
 [0 1]
[0 0 0]]
        [[0 0] [2 2]])))))

(deftest test-winner?
  (testing "Is this a winning board?"
    (is (winner? ; board with single peg
 [[0]
 [0 0] 
[0 0 1]]))
    (is (not (winner? ; board with multiple pegs
 [[1]
 [0 1]
[0 0 0]])))
    (is (winner? [[0] [0 0] [0 0 0]])) ; Board with no pegs
    (is (winner? [[1]])) ; 1-peg board with peg
    (is (winner? [[0]])) ; 1-peg board without peg
    (is (winner? [])))) ; zero-length board

(ns pegthing-test
  (:require [clojure.test :refer :all]
            [pegthing.core :refer :all]))

(deftest test-create-board
  (testing "Create board"
    (is (= [] (create-board 0)))
    (is (= [[false]] (create-board 1)))
    (is (= [[false] [false false]] (create-board 2)))))

(deftest test-base-size
  (testing "Calcualte board's base size"
    (let [test-fn #(= % (base-size (create-board %)))]
      (is (test-fn 0))
      (is (test-fn 1))
      (is (test-fn 2)))))

(deftest test-valid-move
  (testing "Validate position"
    (let [board (create-board 4)]
      (is (valid-move? board [[0 0] [2 2]])) ; From first peg
      (is (valid-move? board [[3 3] [1 1]])) ; From last peg
      (is (not (valid-move? board [[-1 0] [1 2]])))
      (is (not (valid-move? board [[0 -1] [2 1]])))
      (is (not (valid-move? board [[1 0] [1 2]]))))) ; Dest is off-board to the right
  (testing "Zero-length board"
    (is (not (valid-move? [] [[0 0] [2 2]]))))) ; Even 0,0 is not valid on a zero-length board.

(deftest test-valid-move
  (testing "Valid moves"
    (let [board 
      [[true]
    [false true]
  [false false false]
[false false true true]]]
      (is (valid-move? board [[0 0] [2 2]])) ; From first peg
      (is (valid-move? board [[3 3] [3 1]])))) ; From last peg
  (testing "Invalid moves"
    (let [board
      [[true]
    [false false]
  [false false true]
[false false true true]]]
      (is (not (valid-move? board [[-1 -1] [1 1]]))) ; Start off board
      (is (not (valid-move? board [[2 2] [4 4]]))) ; End off board
      (is (not (valid-move? board [[1 0] [1 2]]))))) ; Dest is off-board to the right
  (testing "More invalid moves"
    (let [board
      [[true]
    [false false]
  [false true false]
[true true false false]]]
      (is (not (valid-move? board [[0 0] [2 2]]))) ; No peg to jump
      (is (not (valid-move? board [[2 0] [2 2]]))) ; No peg at starting pos
      (is (not (valid-move? board [[3 0] [3 3]]))) ; Too far of a jump
      (is (not (valid-move? board [[0 0] [0 0]]))) ; Not far enough to jump
      (is (not (valid-move? board [[0 0] [1 1]]))))) ; Not far enough to jump
  (testing "Zero-length board"
    (is (not (valid-move? [] [[0 0] [2 2]]))))) ; Even 0,0 is not valid on a zero-length board.

(deftest test-make-move
  (testing "Test making some moves"
    (is (=
    [[false]
  [false false] 
[false false true]]
      (make-move
    [[true]
  [false true]
[false false false]]
        [[0 0] [2 2]])))))

(deftest test-has-won?
  (testing "Is this a winning board?"
    (is (has-won? :board))))

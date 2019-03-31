(ns pegthing.core)

; Types:
; Board: Vector of vector of bools. True is peg is present else false.
;   Each vector is one element larger than the last, representing the triangular board.
;   Ex.)
;   [[false]
;   [false false]
;   [false false false]]
; Position (pos): Pair of integers. Row then column.
; Move: Pair of positions.

; Creates a board of the given base size.
; n -> board
(defn create-board [base-size]
  (vec (map #(vec (repeat (inc %) false))
    (range base-size))))

; Returns the size of the base of the given board.
; board -> n
(defn base-size [board]
  (count (peek board)))

; Validates the distance of the delta. Delta is [dr dc]
; delta -> bool
(defn- valid-delta [[drow dcol]]
  (or (= 2 (java.lang.Math/abs drow))
      (= 2 (java.lang.Math/abs dcol))))

; Gets the peg at given position.
; board pos -> bool
(defn- get-peg [board [row col]]
  ((board row) col))

; Returns true if the move is valid else false.
; board move -> bool
(defn valid-move? [board [startPos endPos]]
  (try
    (let [delta (map - endPos startPos)
          jumpedPos (map + startPos (map #(/ % 2) delta))]
      (and
        (get-peg board startPos)
        (not (get-peg board endPos))
        (valid-delta delta)
        (get-peg board jumpedPos)))
    (catch IndexOutOfBoundsException e
      false)))

; Adds or removes a peg.
; peg-state is a bool
; board pos bool -> board
(defn- change-peg [board [row col] peg-state]
  (let [row-vec (board row)
        new-row-vec (assoc row-vec col peg-state)]
    (assoc board row new-row-vec)))

; Makes the move on the board and returns the new board.
; board move -> board
(defn make-move [board move]
  (if (not (valid-move? board move))
    (throw (RuntimeException. "Move is not valid")))
  (let [[startPos endPos] move
        delta (map - endPos startPos)
        jumpedPos (map + startPos (map #(/ % 2) delta))]
    (-> board
      (change-peg startPos false)
      (change-peg jumpedPos false)
      (change-peg endPos true))))

; Given a board determines if the player has won.
; board -> bool
(defn has-won? [board]
  ; TODO: Implement this.
  true)
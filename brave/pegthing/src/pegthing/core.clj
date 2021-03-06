(ns pegthing.core)

; Types:
; Peg: Int. 1 if peg is present else 0.
; Board: Vector of vector of pegs.
;   Each vector is one element larger than the last, representing the triangular board.
;   Ex.)
;   [[1]
;   [1 1]
;   [1 1 1]]
; Position (pos): Pair of integers. Row then column.
; Move: Pair of positions.

; Creates a board full of pegs of the given base size.
; n -> board
(defn create-board [base-size]
  (vec (map #(vec (repeat (inc %) 1))
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
; board pos -> peg
(defn- get-peg [board [row col]]
  ((board row) col))

; Returns true if peg is present else false.
; board pos -> bool
(defn- peg? [board pos]
  (= 1 (get-peg board pos)))

; Adds or removes a peg.
; board pos peg -> board
(defn- change-peg [board [row col] peg]
  (assoc board row ; Change row vector on board.
    (assoc (board row) col peg))) ; Change peg in row vector.

; Removes a peg.
; board pos -> board
(defn remove-peg [board pos]
  (change-peg board pos 0))

; Returns true if the move is valid else false.
; board move -> bool
(defn valid-move? [board [startPos endPos]]
  (try
    (let [delta (map - endPos startPos)
          jumpedPos (map + startPos (map #(/ % 2) delta))]
      (and
        (peg? board startPos)
        (not (peg? board endPos))
        (valid-delta delta)
        (peg? board jumpedPos)))
    (catch IndexOutOfBoundsException e
      false)))

; Validates a move. Returns the move if it is valid else nil.
; Returns nil if move is nil.
(defn validate-move [board move]
  (if (and move (valid-move? board move)) move))

; Makes the move on the board and returns the new board.
; board move -> board
(defn make-move [board [startPos endPos]]
  (let [delta (map - endPos startPos)
        jumpedPos (map + startPos (map #(/ % 2) delta))]
    (-> board
      (change-peg startPos 0)
      (change-peg jumpedPos 0)
      (change-peg endPos 1))))

; Given a board determines if the player has won.
; board -> bool
(defn winner? [board]
  ; Board is a winner if there is only one peg.
  ; The board is also a winner if there are zero pegs even though that is technically not a valid board. 
  ; Add all the pegs and see if it's value is <= 1.
  (let [sum (reduce + (apply concat board))]
    (<= sum 1)))

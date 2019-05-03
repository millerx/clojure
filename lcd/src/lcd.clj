(ns lcd
	(require [clojure.set]))

(def parcel-width 3)

; Map of parcels to characters.
(def parcel-char-map {
  (map #(seq %)
    [" _ "
     "| |"
     "|_|"]) \0,
  (map #(seq %)
    ["   "
     "  |"
     "  |"]) \1,
  (map #(seq %)
    [" _ "
     " _|"
     "|_ "]) \2,
  (map #(seq %)
    [" _ "
     " _|"
     " _|"]) \3,
  (map #(seq %)
    ["   "
     "|_|"
     "  |"]) \4,
  (map #(seq %)
    [" _ "
     "|_ "
     " _|"]) \5,
  (map #(seq %)
    [" _ "
     "|_ "
     "|_|"]) \6,
  (map #(seq %)
    [" _ "
     "  |"
     "  |"]) \7,
  (map #(seq %)
    [" _ "
     "|_|"
     "|_|"]) \8,
  (map #(seq %)
    [" _ "
     "|_|"
     " _|"]) \9,
  (map #(seq %)
    ["   "
     "   "
     "   "]) \space,
  })

; Maps characters to parcels
(def char-parcel-map (clojure.set/map-invert parcel-char-map))

; If character cannot be mapped default is space.
(def default-char \space)
(def default-parcel (get char-parcel-map \space))

; Partitions each string in the LCD into arrays of chars. They are not parcels but in the form:
; ((top1 .. topN) (mid1 .. midN) (btm1 .. btmN))
; ("") -> (((\c)))
(defn- partition-lcd [lcd]
  (map #(partition-all parcel-width %) lcd))

; Columnize an ISeq of ISeq, returning a list of the first of each ISeq,
; then a list of the seconds of each ISeq, ect.
; For partitioned-lcds this returns a list of parcels.
; (((\c))) -> (parcel)
; (((\c))) -> (((\c)))
(defn- columnize [colls]
  (if (empty? colls)
    [] ; Without empty? check this unrealized form that failed downstream.
    (apply map #(identity %&) colls)))

; Maps a parcel to it's char. Maps unrecognised parcels to the default char.
; parcel -> \c
; ((\c)) -> \c
(defn- parcel->char [parcel]
  (get parcel-char-map parcel default-char))

; Reads the LCD and returns it's string form.
; ("") -> ""
(defn read-lcd [lcd]
  (->> lcd
    partition-lcd
    (take 3) ; Support only one-line LCD for now.
    columnize
    (map parcel->char)
    (apply str)))

; Given an LCD, appends a parcel to it.
; ("") ((\c)) -> ("")
(defn- append-parcel
  [lcd parcel]
  ; Map turns the string into (\c) so concat the lcd and parcel lists first.
  (map #(apply str (concat %1 %2)) lcd parcel))

; Maps a char to it's parcel. Maps unrecognised parcels to the default parcel.
; \c -> parcel
; \c -> ((\c))
(defn- char->parcel [chr]
  (get char-parcel-map chr default-parcel))

(defn write-lcd [in-str]
  (if (empty? in-str)
    []
    (reduce append-parcel [[] [] []] (map char->parcel in-str))))

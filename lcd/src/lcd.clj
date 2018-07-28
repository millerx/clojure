(ns lcd)

(def lcd ["111222333" "111222333" "111222333"])
(def parcel-width 3)

(def parcels {
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

(def charmap (clojure.set/map-invert parcels))

; For debugging steps of threading macro.
(defn tprintln [x] (println x) x)
(def debug false)

(defn read-lcd [lcd]
  (->> lcd
    ; Produces an ISeq of ISeq of parcel-lines in the order of top1 top2, mid1 mid2, ...
    (map #(partition-all parcel-width %))
    (if debug (tprintln))
    ; ISeq of parcels which are ISeqs of parcel-lines
    (#(partition 3 (apply interleave %)))
    (if debug (tprintln))
    ; Maps parcels to charcters. Unrecognised parcels become spaces.
    (map #(or (get parcels %) \space))
    (if debug (tprintln))
    ; Turn the char sequence into a string
    (apply str)))

(defn- append-parcel
  [lcd parcel]
  (map #(apply str (concat %1 %2)) lcd parcel))

(defn write-lcd [str]
  (reduce #(append-parcel %1 %2) (map #(get charmap % (get charmap \space)) str)))

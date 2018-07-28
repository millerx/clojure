(ns fwpd)
(require '[clojure.string :as str])

(defn str->int
	[str]
	(Integer. str))

(def filename "suspects.csv")
(def conversions {:name identity
				  :glitter-index str->int})
(def vamp-keys (map first conversions))

(defn convert
	[vamp-key value]
	((get conversions vamp-key) value))

(defn parse
	"Convert a CSV into rows of columns"  ; '(["Bob" "3"] ["Fred" "2"])
	[string]
	(map #(str/split % #",")
		(str/split string #"\n")))

(defn mapify
	"Return a seq of maps like {:name \"Ed\" :glitter-index 10}"
	[rows]
	(map (fn [unmapped-row]
			(reduce (fn [row-map [vamp-key value]]
						(assoc row-map vamp-key (convert vamp-key value)))
					{}
					(map vector vamp-keys unmapped-row)))
		 rows))

(defn glitter-filter
	[minimum-glitter records]
	(filter #(>= (:glitter-index %) minimum-glitter) records))

(defn names
	[maps]
	(map :name maps))

(defn append
	;;  See tests.
	[suspects new-suspect]
	(conj (into [] suspects) new-suspect))

(defn validate
	;;  See tests.
	[vamp-keys row]
	(nil? (some #(nil? (% row)) vamp-keys)))

(defn gets
	[row keys]
	(map #(% row) keys))

(defn csv
	;; Converts to csv string.
	[rows]
	(str/join "\n" (map
			#(str/join "," (gets % vamp-keys))  ; We use vamp-keys to enforce the order values are joined.
		rows)))

(defn -main
  "Finds vampires in a csv database."
  [& args]
  (let [result (glitter-filter 3 (mapify (parse (slurp filename))))]
  	(println result)
  	(println (names result))
  	(println (csv result))))

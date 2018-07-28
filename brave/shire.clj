(require '[clojure.string :as str])

;; Body parts of a hobit and their sizes.  Only left side is defined.
(def asym-hobbit-body-parts
	[{:name "head" :size 3}
	{:name "left-eye" :size 1}
	{:name "left-ear" :size 1}
	{:name "mouth" :size 1}
	{:name "nose" :size 1}
	{:name "neck" :size 2}
	{:name "left-shoulder" :size 3}
	{:name "left-upper-arm" :size 3}
	{:name "chest" :size 10}
	{:name "back" :size 10}
	{:name "left-forearm" :size 3}
	{:name "abdomen" :size 6}
	{:name "left-kidney" :size 1}
	{:name "left-hand" :size 2}
	{:name "left-knee" :size 2}
	{:name "left-thigh" :size 4}
	{:name "left-lower-leg" :size 3}
	{:name "left-achilles" :size 1}
	{:name "left-foot" :size 2}])

;; Returns the matching body part or the given part if there is no symetry.
(defn matching-part
	[part]
	{:name (str/replace (:name part) #"^left-" "right-")
	:size (:size part)})

;; Returns a symetric list of body parts from an asymetric list.
(defn symmetrize-body-parts
	"Expects a seq of maps that have a :name and :size"
	[asym-body-parts]
	(reduce (fn [% part] (into % (set [part (matching-part part)])))
		[] asym-body-parts))


;; More verbose symmetrize-body-parts that shows off a few more Clojure core functions.
(defn _symmetrize-body-parts
	"Expects a seq of maps that have a :name and :size"
	[asym-body-parts]
	(loop [remaining-asym-parts asym-body-parts
		   final-body-parts []]
		(if (empty? remaining-asym-parts)
			final-body-parts  ; then return final-body-parts else...
			(let [[part & remaining] remaining-asym-parts]  ; Decompose remaining-asym-parts
				(recur remaining
					   (into final-body-parts  ; This function is 2nd param of recur
							 (set [part (matching-part part)])))))))  ; If matching-part == part then just put part into final-body-parts.

(defn hit
	[asym-body-parts]
	(let [sym-parts (symmetrize-body-parts asym-body-parts)
		  body-part-size-sum (reduce + (map :size sym-parts))
		  target (rand body-part-size-sum)]
		(loop [[part & remaining] sym-parts
			   accumulated-size (:size part)]
			;(println "target" target "accum" accumulated-size "part" part)
			(if (> accumulated-size target)
				part
				(recur remaining (+ accumulated-size (:size (first remaining))))))))

(println (hit asym-hobbit-body-parts))

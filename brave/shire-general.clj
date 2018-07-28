(require '[clojure.string :as str])

(def hobit-symetry
	'("alpha", "beta", "gamma", "delta", "epsilon"))

;; Body parts of a hobit and their sizes.  Only left side is defined.
(def asym-hobbit-body-parts
	[{:name "head" :size 3}
	{:name "alpha-eye" :size 1}
	{:name "beta-ear" :size 1}])

;; Returns the matching body part or the given part if there is no symetry.
(defn matching-part
	[part body-symetry]
	(if-let [[_ part-suffix] (re-matches #".*-(.*)" (:name part))]
		(map (fn [part-prefix]
				{:name (str/join [part-prefix "-" part-suffix])
			 	 :size (:size part)})
			body-symetry)
		[part]))

;; Returns a symetric list of body parts from an asymetric list.
(defn symmetrize-body-parts
	"Expects a seq of maps that have a :name and :size"
	[asym-body-parts body-symetry]
	(reduce
		(fn [% part] (into % (matching-part part body-symetry)))
		[] asym-body-parts))

(println (symmetrize-body-parts asym-hobbit-body-parts hobit-symetry))

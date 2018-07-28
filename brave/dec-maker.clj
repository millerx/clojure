(defn dec-maker
	[dec-by]
	#(- % dec-by))

(def dec3
	(dec-maker 3))

(println (dec3 7))
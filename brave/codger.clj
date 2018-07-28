(require '[clojure.string :as s])

(defn codger-comm
	[whippernsnapper]
	(str "Get off my lawn, " whippernsnapper "!!!"))

(defn codger
	[& whippersnappers]
	(map codger-comm whippersnappers))

;(println (codger-comm "Billy"))
(println (s/join "\n"
	(codger "Billy" "Anne" "Incredible Hulk")))
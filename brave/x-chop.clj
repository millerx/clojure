(defn x-chop
	"Describe the kind of chop you are inflicting on someone."
	([name chop-type]
		(str "I " chop-type " chop " name "! Take that!"))
	([name]
		(x-chop name "karate")))

(println (x-chop "Kanye West" "slap"))
(println (x-chop "Kanye East"))
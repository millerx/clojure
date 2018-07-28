(defn gcd [x y]
	(if (= y 0) x (gcd y (mod x y))))

(println (gcd
	(read-string (first *command-line-args*))
	(read-string (second *command-line-args*))))

(require '[clojure.string :as s])

(println "Lists")
(def x (list 1 2 3 4 5))
(println (s/join "\n" (list 
	(first x) ; 1
	(rest x) ; (2 3 4 5)
	(conj x 100) ; (100 1 2 3 4 5)
	x ; (1 2 3 4 5)
	"")))

(println "Vectors")
(def dogs ['sparky 'spot 'spike 'max])
(println (s/join "\n" (list 
	(count dogs) ; 4
	(conj dogs 'rex) ; [sparky spot spike max rex]
	(peek dogs) ; max
	(get dogs 2) ; spike
	(vec '(1 2 3)) ; [1 2 3]
	"")))

(println "Sets")
(def dogs #{'sparky 'spot 'spike 'max})
(println (s/join "\n" (list 
	(= dogs #{'spike 'max 'spot 'sparky}) ; true
	(conj dogs 'rex) ; #{spike spot sparky rex max}
	(disj dogs 'sparky) ; #{spike spot max}
	(contains? dogs 'spot) ; true
	(contains? dogs 'sport) ; false
	(hash-set 5 8 10 2 1 6 -5) ; #{1 6 2 5 10 -5 8}
	(sorted-set 5 8 10 2 1 6 -5) ; #{-5 1 2 5 6 8 10}
	"")))

(println "Maps")
(def capitals {:ca "sacramento" :hi "honolulu" :ak "juneau"})
(def dueDate {:month 12 :day 16 :year 2015})
(println (s/join "\n" (list 
	capitals ; {:ca "sacramento", :hi "honolulu", :ak "juneau"}
	(capitals :ca) ; sacramento
	(keys capitals) ; (:ca :hi :ak)
	(assoc dueDate :day 26) ; {:month 12, :day 26, :year 2015}
	dueDate ; {:month 12, :day 16, :year 2015}
	"")))
; hash-map, array-map, and sorted-map

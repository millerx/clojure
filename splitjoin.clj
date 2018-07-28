(use '[clojure.string :only (join split)])
; (use 'clojure.string)
; clojure.string/join

(println (split "192.168.1.1" #"\."))
(println (join "\n"
           (split "The Quick Brown Fox" #"\s")))
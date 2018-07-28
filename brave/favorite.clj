(require '[clojure.string :as s])

(defn favorite-things
	[name & things]
	(str "Hi " name " here are my favorite things: " (s/join ", " things)))

(println (favorite-things "Dorean" "gum" "shoes" "kara-te"))
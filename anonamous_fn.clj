(println 
	(map #(str "Hi, " %) ["Darth Vader" "Mr. Magoo"]))
(println
	(#(str %1 " and " %2) "cornbread" "butter beans"))
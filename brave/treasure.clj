;; Deconstructs a map.
(defn receive-treasure
	[{:keys [lat lng] :as location}]
	(println (str "Lat: " lat))
	(println (str "Lng: " lng))
	(println (str "Steer ship! " location)))

(receive-treasure {:lat 28.22 :lng 81.33})

;; Deconstructs a map and provides default values.
(defn receive-treasure2
	[{:keys [lat lng] :or {lng 0.0} :as location}]
	(println (str "Lat: " lat))
	(println (str "Lng: " lng))
	(println (str "Steer ship! " location)))

(receive-treasure2 {:lat 28.22})
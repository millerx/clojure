(deftask fire
  "Announces that something is on fire"
  [t thing THING str "The thing that's on fire."
   p pluralize bool "Whether to pluralize"]
  (let [verb (if pluralize "are" "is")]
    (println "My" thing verb "on fire!")))

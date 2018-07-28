(ns basic-clj
  (:gen-class)
  (:require
    [read.core :as read]
    [evaluate :as eval]))

(defn read-eval-print
  "Single iteration of a REPL (Read Evaluate Print Loop)"
  []
  (-> (read/b-read)
    eval/b-eval
    println))

(defn -main
  "BASIC Interpreter written in Clojure"
  [& args]
  ;(dorun (repeatedly read-eval-print)))
  (read-eval-print))

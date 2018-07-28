(ns read.core
  "Functions to read lines of BASIC languge and transform then into an AST."
  (:require
    [clojure.string :as str]
    [read.tokenize :as t]))

(defn b-read
  "Read a line of BASIC from *in*, transforms it into an AST and returns that AST."
  []
  ; First prompt
  (println "OK")
  ;(str/split (read-line) #" ")
  (read-line))

(defn parse-command
  "Parses a line of BASIC into a command object."
  [line]
  ; TODO
  nil)

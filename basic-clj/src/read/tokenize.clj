(ns read.tokenize
  "Functions to read lines of BASIC languge and transform then into an AST."
  (:require [clojure.string :as str]))

(defn- split-rest-of-string
  "Given the beginning of a string, splits the rest of the string from the line.
   Returns the joined string token and the rest of the line."
  [ltoken line]
  ; If ltoken ends with " but not \" that means there were no spaces in the string and ltoken is the entire string token.
  (if (and
       (str/ends-with? ltoken "\"")
       (not (str/ends-with? ltoken "\\\"")))
    [(.substring ltoken 1 (- (count ltoken) 1)) line] ; Remove qoutes around the token.
    (let [[rtoken rest] (str/split line #"\"$|\" +" 2)
          token (str/join [(.substring ltoken 1) " " rtoken])] ; Remove quote that ltoken starts with.
      ;(println "rtoken" rtoken)
      [token rest])))

(defn- split-token
  "Spits the first token from the rest of the string."
  [line]
  (let [[token rest] (str/split line #" +" 2)]
    ;(println "st" token)
    (if (= \" (get token 0))
      (split-rest-of-string token rest)
      [token rest])))

(defn tokenize
  "Parses a line of BASIC into a list of tokens."
  [line]
  (loop [line line tokens []]
    (if (empty? line)
      tokens
      (let [[token rest] (split-token line)]
        ;(println "token" token "rest" rest)
        (recur rest (conj tokens token))))))
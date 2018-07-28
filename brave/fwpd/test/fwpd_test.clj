(ns fwpd-test
  (:require [clojure.test :refer :all]
            [fwpd :refer :all]))

(deftest str->int-test
  (testing "str->int"
    (is (= (str->int "3") 3))))

(deftest convert-test
  (testing "convert"
  	(is (= (convert :name "Bob") "Bob"))
    (is (= (convert :glitter-index "3") 3))))

(deftest parse-test
  (testing "parse"
    (is (= (parse "Bob,3\nFred,2")
    	   '(["Bob" "3"] ["Fred" "2"])))))

(deftest mapify-test
  (testing "mapify"
    (is (= (mapify '(["Bob" "3"] ["Fred" "2"]))
    	   '({:name "Bob" :glitter-index 3}
    	   	 {:name "Fred" :glitter-index 2})))))

(deftest glitter-filter-test
  (testing "glitter-filter"
    (is (= (glitter-filter 2 '({:name "Nope" :glitter-index 1}
    						   {:name "Bob" :glitter-index 3}
    						   {:name "Fred" :glitter-index 2}))
    		'({:name "Bob" :glitter-index 3}
    		  {:name "Fred" :glitter-index 2})))))

(deftest names-test
  (testing "names"
    (is (= (names '({:name "Bob" :glitter-index 3}
    				{:name "Fred" :glitter-index 2}))
    		'("Bob" "Fred")))))

(deftest append-test
  (testing "append"
    (is (= (append '({:name "Bob" :glitter-index 3} {:name "Fred" :glitter-index 2})
    			   {:name "Alice" :glitter-index 5})
    	   '({:name "Bob" :glitter-index 3} {:name "Fred" :glitter-index 2} {:name "Alice" :glitter-index 5})))))

(deftest validate-test
  (testing "validate"
    (is (validate vamp-keys {:name "Bob" :glitter-index 3}))
    (is (not (validate vamp-keys {:name "Fred"})))
    (is (not (validate vamp-keys {:glitter-index 2})))
    (is (validate vamp-keys {:name "Bob" :glitter-index 3 :other-key "stuff"}))))

(deftest csv-test
	(testing "csv")
		(is (= (csv '({:name "Bob" :glitter-index 3} {:name "Fred" :glitter-index 2}))
			"Bob,3\nFred,2"))
		(is (let [input "Bob,3\nFred,2"]
			(= (csv (mapify (parse input))) input))))

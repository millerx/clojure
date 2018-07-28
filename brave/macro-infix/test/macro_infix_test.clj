(ns macro-infix-test
  (:require [clojure.test :refer :all]
            [macro-infix :refer :all]))

(deftest test-infix-core
  (testing "infix-core"
    ; #1 If you see non-op then move it over.
    ;    [1] [+ 1] -> [1 +] [1]
    (is (= (infix-core '* [1] ['+ 1])   '([1 +] [1])))
  	; #2 If you see a number and it's next is not op then move it over.
    ;    [1 +] [1 + 1] -> [1 + 1] [+ 1]
    (is (= (infix-core '* [1 '+] [1 '+ 1])   '([1 + 1] [+ 1])))
    ; __ Application of #2 on first number
    ;    [] [1 + 1] -> [1] [+ 1]
    (is (= (infix-core '* [] [1 '+ 1])   '([1] [+ 1])))
    ; __ Application of #2 on last number
    ;    [1 +] [1] -> [1 + 1] _
    (is (= (infix-core '* [1 '+] [1])   '([1 + 1] nil)))
    ; #3 If you see a "number op" then add a single element that is a list (op number).  Rest is [op ...]
    ;    [_] [1 * 1] -> [_ [* 1]] [* 1]
    (is (= (infix-core '* [1 '+] [1 '* 1])   '([1 + [* 1]] [* 1])))
    ; #4 If you see "op number" then conj number to last of accum.
    ;    [_ [* 1]] [* 1 _] -> [_ [* 1 1]] _
    (is (= (infix-core '* [1 '+ ['* 1]] ['* 1 '+ 1])   '([1 + [* 1 1]] [+ 1])))
    ; __ Application of #4 on chain of "op number"'s
    ;    [_ [* 1 1]] [* 1 _] -> [_ [* 1 1 1]] _
    (is (= (infix-core '* [1 '+ ['* 1 1]] ['* 1 '+ 1])   '([1 + [* 1 1 1]] [+ 1])))
    ; #5 If you see non-op and last is a vector, then convert that last vector to a list.  Append non-op.
    ;    [1 + [* 1 1]] [+ 1] -> [1 + (* 1 1) +] [1]
    (is (let [[raccum rrest] (infix-core '* [1 '+ ['* 1 1]] ['+ 1])]
      (and (= [raccum rrest] '([1 + (* 1 1) +] [1]))
           (list? (get raccum 2)))))
    ; __ Start with op
    (is (= (infix-core '* [] [1 '* 1])   '([[* 1]] [* 1])))
    ; __ End with op.  Note that this returns empty list instead where the non-op case returns nil
    (is (= (infix-core '* [['* 1]] ['* 1])   '([[* 1 1]] [])))
    ; Convert empty list to nil
    (is (let [[raccum rrest] (infix-core '* [['* 1 1]] [])]
      (and (= [raccum rrest] '([(* 1 1)] nil))
           (list? (get raccum 0)))))
    ))

(deftest test-infix-core-recurse
  (testing "infix-core-recurse"
    ; Move stuff until we encounter the * op
    (is (= (infix-core '* [] [1 '+ 1 '* 1])   '([1] [+ 1 * 1])))
    (is (= (infix-core '* [1] ['+ 1 '* 1])   '([1 +] [1 * 1])))
    ; Handle the * op
    (is (= (infix-core '* [1 '+] [1 '* 1])   '([1 + [* 1]] [* 1])))
    (is (= (infix-core '* [1 '+ ['* 1]] ['* 1])   '([1 + [* 1 1]] [])))
    (is (= (infix-core '* [1 '+ ['* 1 1]] [])   '([1 + (* 1 1)] nil)))
    ; Rest is nil so move to the + op
    (is (= (infix-core '+ [] [1 '+ '(* 1 1)])   '([[+ 1]] [+ (* 1 1)])))
    (is (= (infix-core '+ [['+ 1]] ['+ '(* 1 1)])   '([[+ 1 (* 1 1)]] [])))
    (is (= (infix-core '+ [['+ 1 '(* 1 1)]] [])   '([(+ 1 (* 1 1))] nil)))
    ; Last op.  rest is nil
    ; Pull the one item out of the return vec:  (+ 1 (* 1 1))
    ))

(deftest test-infix-replace-op
  (testing "infix-replace-op"
    (is (= (infix-replace-op '* [1 '+ 1]) '(1 + 1)))
    (is (= (infix-replace-op '* [1 '* 1]) '((* 1 1))))
    (is (= (infix-replace-op '* [1 '+ 1 '* 1]) '(1 + (* 1 1))))
    ))

(deftest test-infix2-fn
  (testing "infix2-fn"
    (is (= (infix2-fn [1 '+ 1 '* 1]) '(+ 1 (* 1 1))))
    ))

(deftest test-infix2
  (testing "infix2"
    (is (= (infix2 [1 + 2 * 3]) 7))
    ))

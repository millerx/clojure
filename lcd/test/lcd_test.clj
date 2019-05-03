(ns lcd-test
  (:require [clojure.test :refer :all]
    [lcd :refer :all]))

(deftest test-read-lcd
  (is (= "123" (read-lcd [
"    _  _ "
"  | _| _|"
"  ||_  _|"])) "Read LCD with a few numbers on it.")

  (testing "Reading an empty screen should produce an empty string."
    (is (= "" (read-lcd [])))
    (is (= "" (read-lcd [
""
""
""]))))

  (is (= " " (read-lcd [
" _ "
"|_|"
"| |"])) "Read unrecognised parcel gets turned into a space.")

  (testing "Invalid screen dimensions"
    (is (= "8 " (read-lcd [
" _ _"
"|_|_"
"|_|_"])) "Screen data beyond the last even parcel width becomes a space character.")

    (is (= " " (read-lcd [
"_"
"_"
"_"])) "Screen width less then one parcel is treated as one unrecognised parcel.")

    (is (= "8" (read-lcd [
" _ "
"|_|"
"|_|"
" _ "])) "Rows of a screen greater then one parcel height are ignored.")

    (is (= "  " (read-lcd [" __ __"]))
      "Screen height less then one parcel is treated as one unrecognised parcel.")))

(deftest test-write-lcd
  (is (= (write-lcd "123") [
"    _  _ "
"  | _| _|"
"  ||_  _|"]) "Write string of numbers to LCD text.")

  (is (= (write-lcd "") []) "Empty string as input produces an empty screen.")

  (is (= (write-lcd "X") [
"   "
"   "
"   "]) "Unprintable character produces 'space' parcel."))

(deftest test-read-write-duality
  ; We start with an invalid parcel so we prove that the replacement character also satisfies 
  ; duality when fed back in.
  (let [read-out-str (read-lcd [
"    _  _  _ "
"  | _||_| _|"
"  ||_ | | _|"])]
    (is (= "12 3" read-out-str))
    (is (= read-out-str (read-lcd (write-lcd read-out-str))))))

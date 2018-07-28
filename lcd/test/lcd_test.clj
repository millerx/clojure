(ns lcd-test
  (:use midje.sweet)
  (:use [lcd]))

(facts "read-lcd"
  (fact "Read LCD with a few numbers on it."
    (read-lcd ["    _  _ "
               "  | _| _|"
               "  ||_  _|"]) => "123")

  (fact "Reading an empty screen should produce an empty string."
    (read-lcd []) => ""
    (read-lcd [""
               ""
               ""]) => "")

  (fact "Read unrecognised parcel gets turned into a space."
    (read-lcd [" _ "
               "|_|"
               "| |"]) => " ")

  (facts "Invalid screen dimensions"
    (fact "Screen data beyond the last even parcel width becomes a space character."
      (read-lcd [" _ _"
                 "|_|_"
                 "|_|_"]) => "8 ")
    (fact "Screen width less then one parcel is treated as one unrecognised parcel."
          (read-lcd ["_"
                     "_"
                     "_"]) => " ")
    (fact "Rows of a screen greater then one parcel height are ignored."
      (read-lcd [" _ "
                 "|_|"
                 "|_|"
                 " _ "]) => "8")
    (fact "Screen height less then one parcel are ignored."
      (read-lcd [" __ __"]) => "")))

(facts "write-lcd"
  (fact "Write string of numbers to LCD text."
    (write-lcd "123") => ["    _  _ "
                          "  | _| _|"
                          "  ||_  _|"])

  (fact "Empty string as input produces an empty screen."
    (write-lcd "") => [])

  (fact "Unprintable character produces 'space' parcel."
    (write-lcd "X") => ["   "
                        "   "
                        "   "]))

(fact "Duality between read-lcd and write-lcd."
  ; We start with an invalid parcel so we prove that the replacement character also satisfies duality
  ; when fed back in.
  (let [read-out-str (read-lcd ["    _  _  _ "
                                "  | _||_| _|"
                                "  ||_ | | _|"])]
    read-out-str => "12 3"
    (read-lcd (write-lcd read-out-str)) => read-out-str))

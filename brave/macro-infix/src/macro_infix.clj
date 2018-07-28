(ns macro-infix
  (:gen-class))

(defmacro infix
	[[p1 op p2]]
	(list op p1 p2))

(defn vreplace-last
	[v x]
	(conj (pop v) x))

(defn list-vlast
	"Takes a vector, who's last element is a vector, and replaces that last element with list.  If the last element is not a vector then given vector is returned unmodified."
	[v]
	(let [vlast (peek v)]
		(if (vector? vlast)
			(vreplace-last v (apply list vlast))
			v)))

; Goal of the method is to be recursive
; TODO: Stick inside a resurisve call
; ASSUMES: Wrapper method has converted given ISeq to vector
(defn infix-core
	"Core of the loop over infix macro."
	[op accum [ix & irest]]
	; OUT: [accum rest]
	; irest is a PersistentVector#ChunkedSeq and has Seq but not Vector semantics.
	; Elements added to accum are either just copied from the given list,
	; or accum is given a vector that converts infix to prefix notation where "op" is encountered.
	; After a successive set of "op" operations end, the prefix notation vector is converted to a list
	; so it is executable.
	(cond ; If num op ... then conj [op num].  Leave [op ...]
		  (= op (first irest))  [(conj accum (vector op ix)) irest]
		  ; If op num ... then assume the last item in accum is a vector and put num in it.
		  ; Recurse will start on next op
		  (= op ix)  [(vreplace-last accum
		  					; Assume that last accum is a vector [op num num ...].  Append number after ix=op to that vector.
		  					(conj (peek accum) (first irest)))
		  			  (rest irest)]  ; Recurse starting at next op
		  ; Terminal condition.  Replace the last vector with a list if necessary.
		  (nil? ix)  [(list-vlast accum) nil]
		  ; If the last item in accum is a vector then convert it to alist.  conj ix to accum.
		  :else  [(conj (list-vlast accum) ix) irest]))

(defn infix-replace-op
	[op infix-list]
	(loop [[accum irest] [[] infix-list]]
		(if (nil? irest)
			accum
			(recur (infix-core op accum irest)))))

(defn infix2-fn
	[infix-list]
	; We (first) because after running through all the operators what should be left is a list of a single function.
	(first (->> infix-list
				(infix-replace-op '/)
				(infix-replace-op '*)
				(infix-replace-op '-) ; TODO: Minus is the same level as plus just with neg operators.
				(infix-replace-op '+))))

(defmacro infix2
	[infix-list]
	(infix2-fn infix-list))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

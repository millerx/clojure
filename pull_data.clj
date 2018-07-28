; Pulls data from something like our paging micro-services.
;
; (load-file "pull-data.clj")

; Simulate an httpGet.
; Three pages of results ((1 2 3 4) (5 6 7 8) (9 10))
; Else results empty result.
(defn httpGet
  [pageNum]
  (cond
    (= pageNum 1) {:lastPage 3, :pageNum 1, :contents [0 1 2 3]}
    (= pageNum 2) {:lastPage 3, :pageNum 2, :contents [4 5 6 7]}
    (= pageNum 3) {:lastPage 3, :pageNum 3, :contents [8 9]}
    :else {}))

; Calls pageFn on each page of items returned from a paged endpoint.
(defn dump-endpoint-page
  [pageFn]
  (loop [currPage 1]
    (pageFn ((httpGet currPage) :contents))
    (if (< currPage 3) ; Hardcoded 3 pages
      (recur (+ 1 currPage)))))

(defn dump-endpoint
  [itemFn]
  (dump-endpoint-page #(dorun (map itemFn %))))

; main
(dump-endpoint println)

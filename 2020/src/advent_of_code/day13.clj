(ns advent-of-code.day13
  (:require
    [clojure.string :as str]))


(def depart-time 1006726)

; Part 1
#_(->> "23,x,x,x,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,647,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,13,19,x,x,x,x,x,x,x,x,x,29,x,557,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,17"
     (#(str/split % #","))
     (remove #{"x"})
     (map (fn [bus-num]
            (let [bus-num (read-string bus-num)]
              [bus-num (- bus-num (rem depart-time bus-num))])))
     (sort-by second)
     (first)
     (#(* (first %) (second %))))


(defn build-inputs
  "inputs -> [input indx] skipping x's"
  [in-string]
  (for [[i x] (map-indexed (fn [idx itm] [idx itm]) (str/split in-string #","))
        :when (not= "x" x)]
    [(read-string x) i]))

;; Part 2:
;; Uses a 'lock-picking style' algorithm where we solve the problem pairwise, and each pair makes the search for the next one easier.
;; We know it has to be a multiple of the first item - mod 0. We then search for the next number that matches both the first and second.
;; Now that we have those locked into place, we know we have to increment by first * second to keep them both in sync. Repeat 
#_(->> "23,x,x,x,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,647,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,13,19,x,x,x,x,x,x,x,x,x,29,x,557,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,17"
     (build-inputs)
     (reduce
       (fn [acc x]
         (let [[value multiple] acc
               [bus-num time-offest] x]
           ;; New min match
           [(->> (range)
                 (map #(+ (* multiple (inc %)) value))
                 (filter #(= 0 (mod (+ time-offest %) bus-num)))
                 (first))
            ;; increment search by this much
            (* multiple bus-num)]))
       [1 1])
     (first))

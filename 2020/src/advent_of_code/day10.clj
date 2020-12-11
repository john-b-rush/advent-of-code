(ns advent-of-code.day10
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))


(->> "inputs/day10.txt"
     (slurp)
     (str/split-lines)
     (map read-string)
     (#(conj % 0))  ; Haxin': need to count the first difference from zero
     (sort)
     (partition 2 1)
     (map #(- (second %) (first %)))
     (frequencies)
     ((fn [diffmap]
        (* (get diffmap 1) (inc (get diffmap 3)))))) ;; haxin': get the answer

(defn- partition-increasing
  [items]
  (reduce (fn [acc [a b]]
            (if (< 1 (- b a))
              (conj acc [b])
              (update-in acc [(dec (count acc))] conj b)))
          [[(first items)]]
          (partition 2 1 items)))


(def tribonacci
  [1 1 2 4 7 13 24 44 81 149 274 504 927 1705 3136 5768 10609])


(->> "inputs/day10.txt"
     (slurp)
     (str/split-lines)
     (map read-string)
     (#(conj % 0))  ; Haxin': need to count the first difference from zero
     (#(conj % (+ 3 (reduce max %))))  ; Haxin': need to count the first difference from zero
     (sort)
     (partition-increasing)
     (filter #(< 2 (count %)))
     (map count)
     (map #(nth trib (dec %)))
     (reduce *))

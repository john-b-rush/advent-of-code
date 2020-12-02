(ns advent-of-code.day2
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))


(defn- paper-needed
  [[x y z]]
  (+ (* 2 x y)
     (* 2 y z)
     (* 2 x z)
     (reduce * (butlast (sort [x y z])))))


(defn- ribbon-needed
  [[x y z]]
  (+ (* x y z) ; bow
     (reduce + (map #(* 2 %) (butlast (sort [x y z]))))))

;; Part 1:
#_(->> "inputs/day2.txt"
     (slurp)
     (str/split-lines)
     (map
       (comp
         #(map read-string %)
         #(str/split % #"x")))
     (map paper-needed)
     (reduce +))


;; Part 2:
#_(->> "inputs/day2.txt"
     (slurp)
     (str/split-lines)
     (map
       (comp
         #(map read-string %)
         #(str/split % #"x")))
     (map ribbon-needed)
     (reduce +))

(ns advent-of-code.day5
  (:require
    [clojure.string :as str]))


(->> "inputs/day5.txt"
     (slurp)
     (str/split-lines)
     (map
       (comp
         #(Integer/parseInt % 2)
         (fn [seat-str]
           (->> seat-str
                (map #(if (contains? #{\F \L} %) "0" "1"))
                (str/join)))))
     (apply max))



(->> "inputs/day5.txt"
     (slurp)
     (str/split-lines)
     (map
       (comp
         #(Integer/parseInt % 2)
         (fn [seat-str]
           (->> seat-str
                (map #(if (contains? #{\F \L} %) "0" "1"))
                (str/join)))))
     (sort)
     (reduce
       (fn [x y]
         (if (= 1 (- y x))
           y
           x)))
     (inc))

(ns advent-of-code.day1
  (:require
    [clojure.string :as str]))


;; Part 1:
(->> "inputs/day1.txt"
     (slurp)
     (seq)
     (frequencies)
     (reduce
       (fn [acc [k v]]
         (case k
           \) (- acc v)
           \( (+ acc v)
           acc))
       0))


;; Part 2
(->> "inputs/day1.txt"
     (slurp)
     (seq)
     (reduce
       (fn [acc k]
         (let [{:keys [floor pos]} acc]
           (if (not= -1 floor)
             (assoc
               acc
               :floor
               (case k
                 \( (inc floor)
                 \) (dec floor)
                 floor)
               :pos (inc pos))
             acc)))
       {:floor 0
        :pos 0})
     :pos)

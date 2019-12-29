(ns adventofcode.day1
  (:require
    [adventofcode.utils :as utils]))


(defn fuel-needed
  "Day 1:
  Calculate the fuel needed to launch the ship. 
  Mass/3 round down, then subtract 2"
  [mass]
  (-> mass
      (quot 3)
      (int)
      (- 2)
      (max 0)))


;; TODO: Use loop/recur
(defn totes-fuel-needed
  "Day 1 part 2:
  Need fuel for the fuel. 
  Calc fuel need + fuel for fuel itself
  "
  [mass]
  (let [fuel (fuel-needed mass)]
    (if (pos? fuel)
      (+ fuel (totes-fuel-needed fuel))
      fuel)))


(defn part1
  []
  (->> (utils/day1-input)
       (map fuel-needed)
       (reduce +)))


(defn part2
  []
  (->> (utils/day1-input)
       (map totes-fuel-needed)
       (reduce +)))

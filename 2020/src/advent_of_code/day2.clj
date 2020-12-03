(ns advent-of-code.day2
  (:require
    [clojure.string :as str]))


(defn- pw-char-count?
  [word]
  (let [[str-range charr pass] (str/split word #" ")
        [start end] (map read-string (str/split str-range #"-"))]
    (<=
      start
      (get (frequencies pass) (first charr) -1)
      end)))

;; Part 1
#_(->> "inputs/day2.txt"
     (slurp)
     (str/split-lines)
     (filter pw-char-count?)
     (count))


(defn- pw-char-loc?
  [word]
  (let [[str-range charr pass] (str/split word #" ")
        [start end] (map read-string (str/split str-range #"-"))]
    (->>
      [(nth pass (dec start)) (nth pass (dec end))]
      (filter #{(first charr)})
      (count)
      (= 1))))

;; Part 2
#_(->> "inputs/day2.txt"
     (slurp)
     (str/split-lines)
     (filter pw-char-loc?)
     (count))


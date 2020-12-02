(ns advent-of-code.day2
  (:require
    [clojure.string :as str]))


(defn- pw-char-count?
  [word]
  (let [[str-range charr pass] (->
                                 word
                                 (str/split #" "))
        [start end]  (str/split str-range #"-")]
    (<=
      (Integer/parseInt start)
      (get (frequencies pass) (first charr) -1)
      (Integer/parseInt end))))

;; Part 1
#_(->> "inputs/day2.txt"
     (slurp)
     (str/split-lines)
     (filter pw-char-count?)
     (count))


(defn- pw-char-loc?
  [word]
  (let [[str-range charr pass] (->
                                 word
                                 (str/split #" "))
        [start end]  (str/split str-range #"-")]
    (->>
      [(nth pass (dec (Integer/parseInt start))) (nth pass (dec (Integer/parseInt end)))]
      (filter #{(first charr)})
      (count)
      (= 1))))

;; Part 2
#_(->> "inputs/day2.txt"
     (slurp)
     (str/split-lines)
     (filter pw-char-loc?)
     (count))


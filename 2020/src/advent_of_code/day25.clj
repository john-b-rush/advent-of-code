(ns advent-of-code.day25
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))

;; Inputs:
;; 14222596
;; 4057428

;; loop size = 3616052
#_(loop [size 0
       value 1]
  (if (= 14222596 value)
    size
    (recur 
      (inc size)
      (rem (* 7 value) 20201227))))

;; Key = 3286137
(loop [size 0
       value 1]
  (if (or (= 3616052 size))
    value
    (recur 
      (inc size)
      (rem (* 4057428 value) 20201227))))

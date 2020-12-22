(ns advent-of-code.day15
  (:require
    [clojure.string :as str]))


;; Part 1 naive
#_(loop [numbers [6 4 12 1 20 0 16]
       idx 8]
  (if (= idx 2021)
    (print (last numbers))
    (let [prev-num (last numbers)
          prev-idx (- (count numbers) (.indexOf (reverse (butlast numbers)) prev-num))]
      (if (= -1 prev-idx)
        (recur (conj numbers 0) (inc idx))
        (recur (conj numbers (- idx prev-idx)) (inc idx))))))

;; Part 2: lots-o-numbers
#_(loop [numbers {6 1
                4 2
                12 3
                1 4
                20 5
                0 6
                16 7}
       last-number 16
       idx 8]
  (if (= idx 30000001)
    last-number
    (let [prev-idx (get numbers last-number (dec idx))]
      (if (= (dec idx) prev-idx)
        (recur
          (assoc
            numbers
            last-number
            (dec idx))
          0
          (inc idx))
        (recur
          (assoc
            numbers
            last-number
            (dec idx))
          (- idx prev-idx 1)
          (inc idx))))))

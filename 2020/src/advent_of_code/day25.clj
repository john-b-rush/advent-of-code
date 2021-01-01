(ns advent-of-code.day25)

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
#_(loop [size 0
       value 1]
  (if (= 3616052 size)
    value
    (recur 
      (inc size)
      (rem (* 4057428 value) 20201227))))

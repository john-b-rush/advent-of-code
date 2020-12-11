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


(def items
  (->>
    "28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3"
    (str/split-lines)
    (map read-string)
    (#(conj % 0))  ; Haxin': need to count the first difference from zero
    (#(concat % [(+ 3 (reduce max %))]))
    (sort)))


(def items2
  (->>
    "16
10
15
5
1
11
7
19
6
12
4"
    (str/split-lines)
    (map read-string)
    (#(conj % 0))  ; Haxin': need to count the first difference from zero
    (#(concat % [(+ 3 (reduce max %))]))
    (sort)))


(defn- partition-increasing
  [items]
  (reduce (fn [acc [a b]]
            (if (< 1 (- b a))
              (conj acc [b])
              (update-in acc [(dec (count acc))] conj b)))
          [[(first items)]]
          (partition 2 1 items)))


(->> items2
     (partition 3 1))


(->> items
     (partition-increasing)
     (filter #(< 2 (count %)))
     (map valid-perms))
        

  
(map #(+ (quot % 2) (quot % 3)))
    ;(filter #(< 2 (count %))))
     ;(map count))
     ;(map #(if (= 4 %) 6 2))
     ;(reduce +))


;(->> items2
     ;(partition 2 1)
     ;(filter #(= 1 (- (last %) (first %)))))
     ;(count)

;(->> items
    ;(partition 2 1)

    ;(filter #(= 1 (- (second %) (first %))))
    ;(map second)
    ;(partition-increasing)
    ;(filter #(< 2 (count %)))
    ;(map #(combo/count-combinations % 2))
    ;(reduce *))



    ;(map count)
    ;(frequencies)
    ;((fn [diffsmap]
        ;(+ (* 2 (get diffsmap 2 0)) (* 6 (get diffsmap 3 0))))))

(defn valid-perms
  [items]
  (let [start (first items)
        end (last items)]
    (->> items
         (combo/permutations)
         (filter #(= start (first %)))
         (map
           #(reduce
              (fn [acc x]
                (cond
                  (< 3 (- x acc))
                  (reduced nil)

                  (= end x)
                  (reduced %)

                  :else
                  x))
              %))
        ;(count) 
         )))


(valid-perms [7 8 9 10])


[4 5 6 7]

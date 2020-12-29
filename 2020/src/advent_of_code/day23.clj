(ns advent-of-code.day23
  (:require
    [clojure.string :as str]))


(defn build-cup-map
  [input]
  (->> input
       (#(conj % (first %)))
       (partition 2 1)
       (map (fn [[x y]] {x y}))
       (reduce merge)))


(defn get-3-cups
  [cups curr-cup]
  (let [x (get cups curr-cup)
        y (get cups x)
        z (get cups y)
        next-cup (get cups z)]

    [[x y z] next-cup]))


(defn make-moves
  [max-moves input]
  (let [max-cup (reduce max input)]
    (loop [cups (build-cup-map input)
           curr-cup (first input)
           moves 0]
      (if (= moves max-moves)
        cups
        (let [[three-cups next-cup] (get-3-cups cups curr-cup)
              dest-cup (loop [dest-cup (dec curr-cup)]
                         (cond
                           (= 0 dest-cup)
                           (recur max-cup)

                           (contains? (set three-cups) dest-cup)
                           (recur (dec dest-cup))

                           :else
                           dest-cup))

              new-cups (assoc
                         cups

                         curr-cup
                         next-cup

                         (last three-cups)
                         (get cups dest-cup)

                         dest-cup
                         (first three-cups))]
        ;(print cups curr-cup dest-cup three-cups next-cup"\n")
          (recur
            new-cups
            next-cup
            (inc moves)))))))


(defn part1-answer
  [cup-map]
  (loop [cup-map cup-map
         curr 1
         all []]
    (let [next-one (get cup-map curr)]
      (if (and (seq all) (= 1 curr))
        all
        (recur
          cup-map
          next-one
          (conj all next-one))))))

;; Part 1 - "Linked List" style
(->>
  (vec (map read-string (str/split "389125467" #"")))
  (make-moves 100)
  (part1-answer))


(defn part2-answer
  [cup-map]
  (let [x (get cup-map 1)
        y (get cup-map x)]
    (* x y)))

;; Part 2 - Lots of moves!
(->> (vec (map read-string (str/split "398254716" #"")))
     (#(concat % (range (inc (reduce max %)) 1000001)))
     (vec)
     (make-moves 10000000)
     (part2-answer))

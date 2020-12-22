(ns advent-of-code.day17
  (:require
    [clojure.string :as str]))


(defn inital-matrix
  [input]
  (reduce
    merge
    (for [[x row] (map-indexed (fn [idx item] [idx item]) input)
          [y value] (map-indexed (fn [idx item] [idx item]) row)]
      {[x y 0] value})))


(defn adj
  [[x y z]]
  (for [x-diff (range -1 2)
        y-diff (range -1 2)
        z-diff (range -1 2)
        :when (not (and (= 0 x-diff)
                        (= 0 y-diff)
                        (= 0 z-diff)))]
    [(+ x x-diff) (+ y y-diff) (+ z z-diff)]))


(defn add-external-points
  [matrix]
  (let [bounds (->> matrix
                    (keys)
                    (flatten)
                    (distinct))
        min-val (reduce min bounds)
        max-val (reduce max bounds)]
    (reduce
      merge
      matrix
      (for [x (range (dec min-val) (+ 2 max-val))
            y (range (dec min-val) (+ 2 max-val))
            z (range (dec min-val) (+ 2 max-val))
            :when (not (contains? matrix [x y z]))]
        {[x y z] "."}))))


(defn check-point
  [matrix [coord value]]
  (let [adj-active-nodes (->> (adj coord)
                              (map #(get matrix % "."))
                              (filter #{"#"})
                              (count))]
    (cond
      (= "#" value)
      (if (<= 2 adj-active-nodes 3)
        {coord "#"}
        {coord "."})

      (= "." value)
      (if (= 3 adj-active-nodes)
        {coord "#"}
        {coord "."})

      :else
      {coord "."})))


(defn process
  [matrix]
  (->> matrix
       (add-external-points)
       (map (partial check-point matrix))
       (reduce merge)))

;; Part 1
#_(->> "inputs/day17.txt"
     (slurp)
     (str/split-lines)
     (map #(str/split % #""))
     (inital-matrix)
     (iterate process)
     (take 7)
     (last)
     (map second)
     (filter #{"#"})
     (count))


(defn inital-matrix-4d
  [input]
  (reduce
    merge
    (for [[x row] (map-indexed (fn [idx item] [idx item]) input)
          [y value] (map-indexed (fn [idx item] [idx item]) row)]
      {[x y 0 0] value})))


(defn adj-4d
  [[x y z w]]
  (for [x-diff (range -1 2)
        y-diff (range -1 2)
        z-diff (range -1 2)
        w-diff (range -1 2)
        :when (not (and (= 0 x-diff)
                        (= 0 y-diff)
                        (= 0 z-diff)
                        (= 0 w-diff)))]
    [(+ x x-diff) (+ y y-diff) (+ z z-diff) (+ w w-diff)]))


(defn add-external-points-4d
  [matrix]
  (let [bounds (->> matrix
                    (keys)
                    (flatten)
                    (distinct))
        min-val (reduce min bounds)
        max-val (reduce max bounds)]
    (reduce
      merge
      matrix
      (for [x (range (dec min-val) (+ 2 max-val))
            y (range (dec min-val) (+ 2 max-val))
            z (range (dec min-val) (+ 2 max-val))
            w (range (dec min-val) (+ 2 max-val))
            :when (not (contains? matrix [x y z w]))]
        {[x y z w] "."}))))


(defn check-point-4d
  [matrix [coord value]]
  (let [adj-active-nodes (->> (adj-4d coord)
                              (map #(get matrix % "."))
                              (filter #{"#"})
                              (count))]
    (cond
      (= "#" value)
      (if (<= 2 adj-active-nodes 3)
        {coord "#"}
        {coord "."})

      (= "." value)
      (if (= 3 adj-active-nodes)
        {coord "#"}
        {coord "."})

      :else
      {coord "."})))


(defn process-4d
  [matrix]
  (->> matrix
       (add-external-points-4d)
       (map (partial check-point-4d matrix))
       (reduce merge)))

;; Part 2 - why be clever?
#_(->> "inputs/day17.txt"
     (slurp)
     (str/split-lines)
     (map #(str/split % #""))
     (inital-matrix-4d)
     (iterate process-4d)
     (take 7)
     (last)
     (map second)
     (filter #{"#"})
     (count))


(ns advent-of-code.day11
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))


(def input
  (->> "inputs/day11.txt"
       (slurp)
       (str/split-lines)))


(defn adj
  [[x y]]
  (for [x-diff (range -1 2)
        y-diff (range -1 2)
        :when (not (and (= 0 x-diff)
                        (= 0 y-diff)))]
    [(+ x x-diff) (+ y y-diff)]))


(defn build-seat-graph
  "Build graph w/aajanecy list"
  [input]
  (reduce
    merge
    (for [[y row] (map vector (range) input)
          [x col] (map vector (range) row)
          :when (not= col \.)]
      {[x y] {:val col
              :adj (adj [x y])}})))


(defn build-seat-map
  "Builds simple map of seats"
  [input]
  (reduce
    merge
    (for [[y row] (map vector (range) input)
          [x col] (map vector (range) row)]
      {[x y] col})))


(defn adj-occupied-seats
  [graph adj-seats]
  (->> adj-seats
       (map (comp
              :val
              #(get graph %)))
       (filter #{\#})
       (count)))


(defn change-seat
  [graph [k v]]
  (case (:val v)
    \L
    (when (= 0 (adj-occupied-seats graph (:adj v)))
      {k (assoc v :val \#)})
    \#
    (when (<= 4 (adj-occupied-seats graph (:adj v)))
      {k (assoc v :val \L)})

    nil))

;; Part 1
#_(loop [seat-graph (build-seat-graph input)
       iters 0]
  (let [changes  (->> seat-graph
                      (map (partial change-seat seat-graph))
                      (remove nil?))]
    (if (or (not (seq changes)) (= 100 iters)) ;; 100 iters to not lock up
      (->> seat-graph
           (map (fn [[_ v]] (:val v)))
           (filter #{\#})
           (count))
      (recur (reduce merge seat-graph changes) (inc iters)))))


(def trace-base
  "the base deltas for ray tracing, e.g. [0 -1] [-1 -1] etc"
  (for [x-diff (range -1 2)
        y-diff (range -1 2)
        :when (not= 0 x-diff y-diff)]
    [x-diff y-diff]))


(defn trace
  "Simple lazy ray trace"
  [graph [x y] [x-delt y-delt]]
  (->> (range)
       (map
         (fn [factor]
           (let [x' (+ x (* (inc factor) x-delt))
                 y' (+ y (* (inc factor) y-delt))]
             (get graph [x' y']))))
       (take-while (complement nil?))
       (remove #{\.})
       (first)))


(defn trace-occupied-seats
  [graph k]
  (->> trace-base
       (map (partial trace graph k))
       (filter #{\#})
       (count)))


(defn change-seat-los
  "Uses Line of sight for changing seats"
  [graph [k v]]
  (case v
    \L
    (when (= 0 (trace-occupied-seats graph k))
      {k \#})
    \#
    (when (<= 5 (trace-occupied-seats graph k))
      {k \L})
    nil))


;; Part 2
(loop [seat-graph (build-seat-map input)
       iters 0]
  (let [changes  (->> seat-graph
                      (map (partial change-seat-los seat-graph))
                      (remove nil?))]
    (if (or (not (seq changes)) (= 100 iters)) ;; 100 iters to not lock up
      (->> seat-graph
           (vals)
           (filter #{\#})
           (count))
      (recur (reduce merge seat-graph changes) (inc iters)))))

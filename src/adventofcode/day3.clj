(ns adventofcode.day3
  (:require [adventofcode.utils :as utils]))


(defn line-segments
  [start len direction-fn]
   (rest (take (inc len) (iterate direction-fn start))))


(defn instuctions
  [instr]
  [(subs instr 0 1) (read-string (subs instr 1))])

(defn direction-fn
  [direction]
  (cond
    (= direction "R")
    (fn [[a b]] [(inc a) b])
    (= direction "L")
    (fn [[a b]] [(dec a) b])
    (= direction "D")
    (fn [[a b]] [a (dec b)])
    (= direction "U")
    (fn [[a b]] [a (inc b)])))

(defn gen-points
  [start instruction]
  (let [[direction len] (instuctions instruction)] 
    (line-segments start len (direction-fn direction))))

(defn all-points
  [path]
  (loop [path path
         start [0 0]
         points []]
    (if path
      (let [path-points (gen-points start (first path))]
        (recur (next path) (last path-points) (concat points path-points)))
      points)))

(defn closest-intersection
  [path1 path2]
  (->> (clojure.set/intersection (set (all-points path1)) (set (all-points path2)))
      (map (fn [[a b]] (+ (max a (- a)) (max b (- b)))))
      (sort)
      (first)))


(defn least-signal-delay
  [path1 path2]
  (let [path1-points (all-points path1)
        path2-points (all-points path2)
        intersections (clojure.set/intersection (set path1-points) (set path2-points))]
    (->> intersections
         (map #(+ (.indexOf path1-points %) (.indexOf path2-points %)))
         (sort)
         (first)
         (+ 2))))



(defn part1
  []
  (let [[path1 path2] (utils/day3-input)]
    (closest-intersection path1 path2)))

(defn part2
  []
  (let [[path1 path2] (utils/day3-input)]
    (least-signal-delay path1 path2)))


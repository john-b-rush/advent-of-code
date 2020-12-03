(ns advent-of-code.day3
  (:require
    [clojure.string :as str]))


(def example
  (->>
  "..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#"
  (str/split-lines)))


(->>  "inputs/day3.txt"
      (slurp)
      (str/split-lines)
      (map-indexed
        (fn [idx row]
          (nth
            (cycle row)
            (* 3 idx))))
      (filter #{\#})
      (count))


(defn- count-trees
  [rows right down]
  (->> rows
       (take-nth down)
       (map-indexed
        (fn [idx row]
          (nth
            (cycle row)
            (* right idx))))
      (filter #{\#})
      (count)))


(def inputs
(->>  "inputs/day3.txt"
      (slurp)
      (str/split-lines)))
(* 
  (count-trees inputs 1 1)
  (count-trees inputs 3 1)
  (count-trees inputs 5 1)
  (count-trees inputs 7 1)
  (count-trees inputs 1 2)
 )


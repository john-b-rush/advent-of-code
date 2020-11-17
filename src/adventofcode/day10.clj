(ns adventofcode.day10
  (:require
    [adventofcode.utils :as utils]
    [clojure.string :as str]))


(def example
  (str ".#..#\n"
       ".....\n"
       "#####\n"
       "....#\n"
       "...##"))


(defn astroid-locations
  [astroid-map]
  (->> astroid-map
       (str/split-lines)
       (map-indexed
         (fn [y-idx row]
           (->> row
                (seq)
                (map str)
                (map-indexed
                  (fn [x-idx v]
                    (when (= "#" v)
                      [x-idx y-idx])))
                (remove nil?))))
       (mapcat identity)))


(defn angle-between
  [a b]
  (let [arctan (Math/toDegrees (Math/atan2
                                 (- (second b) (second a))
                                 (- (first b) (first a))))]
    (if (> 0 arctan)
      (+ 360 arctan)
      arctan)))


(defn detected
  [locs location]
  [(->> locs
        (map (partial angle-between location))
        (distinct)
        (count))
   location])


(defn max-detected
  [astroid-locations]
  (->> astroid-locations
       (map (partial detected astroid-locations))
       (reduce
         (fn [m x]
           (if (< (first m) (first x))
             x
             m)))))


#_(-> example2
    (astroid-locations)
    (max-detected))


(def example2
  (str
    "......#.#.\n"
    "#..#.#....\n"
    "..#######.\n"
    ".#.#.###..\n"
    ".#..#.....\n"
    "..#....#.#\n"
    "#..#....#.\n"
    ".##.#..###\n"
    "##...#..#.\n"
    ".#....####\n"))


(defn day10-input
  []
  (->> "input/day10.txt"
       (slurp)))


#_(-> (day10-input)
    (astroid-locations)
    (max-detected))


(def basic [[0 2] [0 1] [1 1] [-1 -1]])


(def basic2
  (str
    ".#.\n"
    "###\n"
    ".#.\n"))


(defn- square
  [x]
  (* x x))


(defn distance
  [location [_ x]]
  (+
    (square (- (first location)
               (first x)))
    (square (- (second location)
               (second x)))))


(defn split-angle
  [angle x]
  (>= angle (first x)))


(defn get-angles2
  [locs location]
  (->> locs
       (map (fn [loc] [(angle-between location loc) loc]))
       (group-by first)
       (mapcat (fn [[_ v]] (sort-by (partial distance location) < v)))
       (sort-by first >)))


(defn sort-next-astroid
  [locs-angles angle]
  (->> locs-angles
       (sort-by first)  ;; I prob don't need to keep sorting
       (split-with (partial split-angle angle))
       (reverse)
       (reduce concat)))


(sort-next-astroid agls 269)
(sort-next-astroid agls 0)


(defn get-angles
  [locs location angle]
  (->> locs
       (map (fn [loc] [(angle-between location loc) loc]))
       (group-by first)
       (mapcat (fn [[_ v]] (sort-by (partial distance location) < v)))
       (sort-by first)
       (split-with (partial split-angle angle))
       (reverse)
       (reduce concat)))


(defn shoot-astroid
  "Shoots the next nearest astroid clockwise from the previous astroid"
  [locs location angle]
  (let [astroids (get-angles locs location angle)]
    [(first astroids) (mapv second (rest astroids))]))


(defn shoot-astroids
  [locs location shots]
  (loop [locs (get-angles2 (disj (set locs) location) location)
         prev-angle 269.99999;; almost 270
         shot-count 1]
    (let [astroids (sort-next-astroid locs prev-angle)]
      (print "\n\n\n --" (first astroids) " --\n")
      (if (= shot-count shots)
        (first astroids)
        (recur (rest astroids) (first (first astroids)) (inc shot-count))))))


#_(-> basic2
    (astroid-locations)
    (shoot-astroids [1 1] 4))


(def example3
  (str ".#..##.###...#######\n"
       "##.############..##.\n"
       ".#.######.########.#\n"
       ".###.#######.####.#.\n"
       "#####.##.#.##.###.##\n"
       "..#####..#.#########\n"
       "####################\n"
       "#.####....###.#.#.##\n"
       "##.#################\n"
       "#####.##.###..####..\n"
       "..######..##.#######\n"
       "####.##.####...##..#\n"
       ".#####..#.######.###\n"
       "##...#.##########...\n"
       "#.##########.#######\n"
       ".####.#.###.###.#.##\n"
       "....##.##.###..#####\n"
       ".#.#.###########.###\n"
       "#.#.#.#####.####.###\n"
       "###.##.####.##.#..##\n"))


#_(-> (day10-input)
    (astroid-locations)
    (shoot-astroids [20 19] 200))


#_(-> example3
    (astroid-locations)
    (shoot-astroids [11 13] 1))

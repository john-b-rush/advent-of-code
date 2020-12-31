(ns advent-of-code.day20
  (:require
    [clojure.string :as str]))


(defn map-tile-edges
  [tile]
  (let [[_ number rows] (re-find #"(?is)Tile (\d+):(.*)" tile)
        rows (filter seq (str/split-lines rows))
        top (first rows)
        bottom (last rows)
        rotate (map str/join (apply map list rows))
        left (first rotate)
        right (last rotate)]
    {number [top right left bottom]}))


(defn tile-edges
  []
  (->> "inputs/day20.txt"
       (slurp)
       (#(str/split % #"\n\n"))
       (map map-tile-edges)
       (into {})))


(defn other-edges
  [other-tiles]
  (->> other-tiles
       vals
       (reduce concat)
       (mapcat (juxt identity str/reverse))
       (into #{})))

;; Part 1: just find missing edges
#_(let [all-tile-edges (tile-edges)]
  (->> all-tile-edges
       (map (fn [[tile edges]]
              (let [o-edges (other-edges (dissoc all-tile-edges tile))
                    not-matching (->> edges
                                      (map (partial get o-edges))
                                      (filter nil?)
                                      (count))]
                [tile {:edges edges
                       :missing not-matching}])))
       (filter (fn [[tile v]] (= 2 (:missing v))))
       (map (comp
              read-string
              first))
       (reduce *)))


(defn map-all-tiles
  [tile]
  (let [[_ number rows] (re-find #"(?is)Tile (\d+):(.*)" tile)
        rows (filter seq (str/split-lines rows))]
    (vec rows)))


(defn- flip-tile
  [tile]
  (->> tile
       (map reverse)
       (map str/join)
       (vec)))


(defn- rotate-tile
  [tile]
  (->> tile
       (reverse)
       (apply map list)
       (map str/join)
       (vec)))


(defn- permute-tile
  [tile]
  (for [flip (range 2)
        :let [tile' (nth (iterate flip-tile tile) flip)]
        rotation (range 4)]
    (vec (nth (iterate rotate-tile tile') rotation))))


(defn- check-tiles
  [tile match-fn match]
  (->>
    ;; Build rotations
    (permute-tile tile)
    (filter #(= match (match-fn %)))
    (first)))


(defn- matching-fn
  [direction]
  (case direction
    :top
    first

    :bottom
    last

    :left
    #(str/join (map first %))

    :right
    #(str/join (map last %))))


(def opposite-side
  {:top :bottom
   :bottom :top
   :left :right
   :right :left})


(defn find-matching-tile
  [tiles tile match-side]
  (->> tiles
       (map (juxt
              identity
              #(check-tiles
                 %
                 (matching-fn (get opposite-side match-side))
                 ((matching-fn match-side) tile))))
       (filter #(second %))
       (first)))


(defn- monster?
  [[y x] image]
  (and
    (= \# (get-in image [(+ y 0) (+ x 0)]))
    (= \# (get-in image [(+ y 1) (+ x 1)]))
    (= \# (get-in image [(+ y 1) (+ x 4)]))
    (= \# (get-in image [(+ y 0) (+ x 5)]))
    (= \# (get-in image [(+ y 0) (+ x 6)]))
    (= \# (get-in image [(+ y 1) (+ x 7)]))
    (= \# (get-in image [(+ y 1) (+ x 10)]))
    (= \# (get-in image [(+ y 0) (+ x 11)]))
    (= \# (get-in image [(+ y 0) (+ x 12)]))
    (= \# (get-in image [(+ y 1) (+ x 13)]))
    (= \# (get-in image [(+ y 1) (+ x 16)]))
    (= \# (get-in image [(+ y 0) (+ x 17)]))
    (= \# (get-in image [(+ y -1) (+ x 18)]))
    (= \# (get-in image [(+ y 0) (+ x 18)]))
    (= \# (get-in image [(+ y 0) (+ x 19)]))))


(defn- water-roughness
  [image]
  (->>
    (for [y (range (count image))
          x (range (count (first image)))
          :when (= \# (get-in image [y x]))]
      (+ 1 (if (monster? [y x] image) -15 0)))
    (reduce +)))


(def dirmap
  {:top [0 -1]
   :bottom [0 1]
   :left [-1 0]
   :right [1 0]})


(defn place-tile
  [[x y] grid tiles]
  (let [tile (get grid [x y])]
    (reduce
      (fn [[grid rem-tiles] dir]
        (let [[dx dy] (get dirmap dir)
              new-loc [(+ x dx) (+ y dy)]]
          (if (get grid new-loc)
            [grid rem-tiles]
            (let [[org-tile orient-tile] (find-matching-tile rem-tiles tile dir)]
              (if orient-tile
                (place-tile
                  new-loc
                  (assoc
                    grid
                    new-loc
                    orient-tile)
                  (disj rem-tiles org-tile))
                [grid rem-tiles])))))
      [grid tiles]
      [:top :left :right :bottom])))


(defn layout-tiles
  [tiles first-tile]
  (->>
    (place-tile [0 0] {[0 0] first-tile} (disj tiles first-tile))
    (first)
    (group-by #(second (first %)))
    (sort)
    (vals)
    (map #(->> %
               (map (fn [[[x y] tile]] [x tile]))
               (sort-by first)
               (map second)))
    (map #(map (fn [tile]
                 (->> tile
                      (drop 1)
                      (butlast)
                      (mapv (fn [row]
                              (->> row
                                   (drop 1)
                                   (butlast)
                                   str/join))))) %))
    (mapv #(apply (partial mapv (fn [& vars] (str/join vars))) %))
    (flatten)))

;; Part 2
#_(let [tiles (->> "inputs/day20.txt"
                 (slurp)
                 (#(str/split % #"\n\n"))
                 (map map-all-tiles)
                 (into #{}))
      image (layout-tiles tiles (first tiles))]
  (->>
    (permute-tile image)
    (map water-roughness)
    (reduce min)))

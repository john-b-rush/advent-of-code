(ns advent-of-code.day20
  (:require
    [clojure.set :refer [difference intersection]]
    [clojure.string :as str]))


(defn map-tiles
  [tile]
  (let [[_ number rows] (re-find #"(?is)Tile (\d+):(.*)" tile)
        rows (filter seq (str/split-lines rows))
        top (first rows)
        bottom (last rows)
        rotate (map str/join (apply map list rows))
        left (first rotate)
        right (last rotate)]
    {number [top right left bottom]}))


(defn other-edges
  [other-tiles]
  (->> other-tiles
       vals
       (reduce concat)
       (mapcat (juxt identity str/reverse))
       (into #{})))

;; Part 1: just find missing edges
(print (let [all-tiles (->> "inputs/day20.txt"
                     (slurp)
                     (#(str/split % #"\n\n"))
                     (map map-tiles)
                     (into {}))]
  (->> all-tiles)))
       (map (fn [[tile edges]]
              (let [o-edges (other-edges (dissoc all-tiles tile))
                    not-matching (->> edges
                                      (map (partial get o-edges))
                                      (filter nil?)
                                      (count))]
                (when (= 2 not-matching)
                  tile))))
       (remove nil?)
       (map read-string)
       (reduce *)))


;3769 1019 3557 1097
;(2971 3079 1951 1171)
(let [all-tiles (->> "inputs/day20.txt"
                     (slurp)
                     (#(str/split % #"\n\n"))
                     (map map-tiles)
                     (into {}))]
  (loop [rem-tiles (dissoc
                     all-tiles
                     "3079")
         current-tile ["3079"
                       (get all-tiles "3079")]
         placements ["3079"]]
    (let [right-edge (get-in current-tile [1 2])
          right-tile (->> rem-tiles
                          (filter (fn [[_ edges]]
                                    (contains?
                                      (into #{} (mapcat (juxt identity str/reverse) edges))
                                      right-edge)))
                          (first))
          bottom-edge (get-in current-tile [1 3])
          bottom-tile (->> rem-tiles
                          (filter (fn [[_ edges]]
                                    (contains?
                                      (into #{} (mapcat (juxt identity str/reverse) edges))
                                      bottom-edge)))
                          (first))
          top-edge (get-in current-tile [1 0])
          top-tile (->> rem-tiles
                          (filter (fn [[_ edges]]
                                    (contains?
                                      (into #{} (mapcat (juxt identity str/reverse) edges))
                                      top-edge)))
                          (first))
          ]
      (print (first current-tile) bottom-edge":" right-tile "  " bottom-tile top-tile \newline)
      (if (nil? right-tile)
        (if (and (nil? right-tile) (nil? bottom-tile) (nil? top-tile))
          placements
          (let
            ;; Where I left off- Somehow need to do filling algo


        (recur
          (dissoc rem-tiles (first right-tile))
          right-tile
          (concat placements [(first right-tile)]))))))

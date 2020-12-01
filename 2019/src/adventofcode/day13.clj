(ns adventofcode.day13
  (:require
    [adventofcode.intcode :refer [intcode run-intcode]]
    [adventofcode.utils :as utils]
    [clojure.core.async :refer [alts!! timeout go take! put! >! chan >!! <!! close!]]
    [clojure.string :as str]))


(defn get-updates
  [board out]
  (loop [board board]
    (let [[x _] (alts!! [out (timeout 100)])
          [y _] (alts!! [out (timeout 100)])
          [z _] (alts!! [out (timeout 100)])]
      (if (nil? x)
        board
        (recur (merge board {[x y] z}))))))


(defn joystick
  [ball paddle]
  (let [ball_x (first (first  ball))
        paddle_x (first (first paddle))]
    (cond
      (< ball_x paddle_x)
      -1

      (= ball_x paddle_x)
      0

      (> ball_x paddle_x)
      1)))


(defn part2
  []
  (let [in (chan)
        out (intcode
              in
              (->> (utils/day13-input)
                   (drop 1)
                   (into [2])))]
    (loop [game-board (get-updates {} out)]
      (let [blocks (->> game-board
                        (filter (comp #{2} last))
                        (count))
            ball (->> game-board
                      (filter #(and (not (= [-1 0] (first %)))
                                    (= 4 (last %))))
                      ;(filter (comp #{4} last))
                      (first))
            paddle (->> game-board
                        (filter (comp #{3} last))
                        (first))
            score (->> game-board
                       (filter #(= [-1 0] (first %)))
                       (first))]
        (if (= 0 blocks)
          [score ball paddle score blocks]
          (do
            (print "\n->" ball ":" paddle ">")
            (print (joystick ball paddle) " bs:" blocks)
            (go (>! in (joystick ball paddle)))
            (recur (get-updates game-board out))))))))


(comment
(->> (utils/day13-input)
     (run-intcode)
     (into [nil])
     (take-nth 3)
     (filter #(= 2 %))
     (count))

)


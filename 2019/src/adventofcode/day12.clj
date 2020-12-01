(ns adventofcode.day12)


(def d12-sample1
  ;; sample 1
  [{:x -1 :y 0 :z 2}
   {:x 2 :y -10 :z -7}
   {:x 4 :y -8 :z 8}
   {:x 3 :y 5 :z -1}])


(def d12-sample2
  [{:x -8 :y -10 :z 0}
   {:x 5 :y 5 :z 10}
   {:x 2 :y -7 :z 3}
   {:x 9 :y -8 :z -3}])


(def d12-input
  [{:x -16 :y -1 :z -12}
   {:x 0 :y -4 :z -17}
   {:x -11 :y 11 :z 0}
   {:x 2 :y 2 :z -6}])


(defn axis-velocity
  [axis curr-pos planets]
  (reduce + (map #(compare % curr-pos) (map #(axis %) planets))))


(defn velocity
  [planet planets]
  (into {} (for [[k v] planet]
             [k (axis-velocity k v planets)])))


(defn planets-velc
  [planets]
  (map #(velocity % planets) planets))


(defn new-velocities
  [planets old-velocities]
  (let [new-velcs (map #(velocity % planets) planets)]
    (if (nil? old-velocities)
      new-velcs
      (mapv (partial merge-with +) new-velcs old-velocities))))


(defn new-positions
  [positions velocity]
  (if (nil? velocity)
    positions
    (mapv (partial merge-with +) positions velocity)))


(defn abs
  [n]
  (max n (- n)))


(defn energy-value
  [thing]
  (reduce + (mapv #(abs (val %)) thing)))


(defn planet-energy
  [pos vel]
  (print pos vel)
  (* (energy-value pos) (energy-value vel)))


(defn calc-energy
  [positions velocity]
  (reduce +  (mapv  * (map energy-value positions) (map energy-value velocity))))


(defn total-energy
  [steps positions]
  (loop [steps steps
         positions positions
         velocity nil]
    (if (= 0 steps)
      (calc-energy positions velocity)
      (let [new-velcs (new-velocities positions velocity)
            new-pos (new-positions positions new-velcs)]
        (recur (dec steps) new-pos new-velcs)))))


(defn steps-to-repeat
  [orig-positions]
  (loop [steps 0
         positions orig-positions
         velocity nil]
    (let [new-velcs (new-velocities positions velocity)
          new-pos (new-positions positions new-velcs)]
      (if (= orig-positions new-pos)
        steps
        (recur (inc steps) new-pos new-velcs)))))


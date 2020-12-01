(ns adventofcode.day3-test
  (:require [clojure.test :refer :all]
            [adventofcode.day3 :refer :all]))

#_(deftest closest-intersections
  (testing "find closest intersections"
    (let [path1 ["R8" "U5" "L5" "D3"]
          path2 ["U7" "R6" "D4" "L4"]]
      (is (= 6 (closest-intersection path1 path2 ))))
    (let [path1 ["R75" "D30" "R83" "U83" "L12" "D49" "R71" "U7" "L72"]
          path2 ["U62" "R66" "U55" "R34" "D71" "R55" "D58" "R83"]]
      (is (= 159 (closest-intersection path1 path2 ))))
    (let [path1 ["R98" "U47" "R26" "D63" "R33" "U87" "L62" "D20" "R33" "U53" "R51"]
          path2 ["U98" "R91" "D20" "R16" "D67" "R40" "U7" "R15" "U6" "R7"]]
      (is (= 135 (closest-intersection path1 path2 )))))
  (testing "Part 1: answer"
    (is (= (part1) 768))))


#_(deftest least-signal-delays
  (testing "find closest intersections"
    (let [path1 ["R8" "U5" "L5" "D3"]
          path2 ["U7" "R6" "D4" "L4"]]
      (is (= 30 (least-signal-delay path1 path2 ))))
    (let [path1 ["R75" "D30" "R83" "U83" "L12" "D49" "R71" "U7" "L72"]
          path2 ["U62" "R66" "U55" "R34" "D71" "R55" "D58" "R83"]]
      (is (= 610 (least-signal-delay path1 path2 ))))
    (let [path1 ["R98" "U47" "R26" "D63" "R33" "U87" "L62" "D20" "R33" "U53" "R51"]
          path2 ["U98" "R91" "D20" "R16" "D67" "R40" "U7" "R15" "U6" "R7"]]
      (is (= 410 (least-signal-delay path1 path2 )))))
  (testing "Part 2: answer"
    (is (= (part2) 8684))))


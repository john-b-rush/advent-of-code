(ns adventofcode.intcode-test
  (:require
    [adventofcode.intcode :refer :all]
    [clojure.test :refer :all]))


(deftest dispatch-opcodes-test
  (testing "Part 1- opcodes"
    (is (= (dispatch-opcodes [1 0 0 0 99]) [2 0 0 0 99]))
    (is (= (dispatch-opcodes [2 3 0 3 99]) [2 3 0 6 99]))
    (is (= (dispatch-opcodes [1 1 1 4 99 5 6 0 99]) [30 1 1 4 2 5 6 0 99]))))

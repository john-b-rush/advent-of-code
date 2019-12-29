(ns adventofcode.intcode)


(defn read-param
  "Reads the value of a parameter"
  [input index offset]
  (nth input (nth input (+ index offset))))


(defn dispatch-opcodes
  "Process opcodes"
  ([input]
   (dispatch-opcodes input 0))
  ([input index]
   (loop [input input
          index index]
     (let [opcode (nth input index)]
       (if (= opcode 99)
         input
         (recur
           (assoc input
                  (nth input (+ index 3))
                  ((cond (= opcode 1) +
                         (= opcode 2) *)
                   (read-param input index 1)
                   (read-param input index 2)))
           (+ index 4)))))))


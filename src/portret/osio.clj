(ns portret.osio)

(defn execute
  "Executes the command and arguments and returns the
   output as a string to the caller."
  [cmd & args]
  (let [filtered-args (filter #(not (nil? %)) args)
        _ (println (str "command: " cmd ", args: " (reduce #(str %1 " " %2) filtered-args)))
        cmdargs (into-array String (cons cmd filtered-args))
        p (.exec (Runtime/getRuntime) cmdargs)]
    (slurp (.getInputStream p))))

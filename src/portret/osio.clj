(ns portret.osio)

(defn execute
  "Executes the command and arguments and returns the
   output as a string to the caller."
  [cmd & args]
  (println (str "args: " args))
  (let [cmdargs (into-array String (cons cmd  args))
        _ (println (str "cmdargs: " cmdargs))
        p (.exec (Runtime/getRuntime) cmdargs)]
    (slurp (.getInputStream p))))

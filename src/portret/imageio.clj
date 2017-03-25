(ns portret.imageio
  (:require [portret.fs :as fs]
            [portret.osio :as osio])
  (:import (java.io.File)))

(defn- dims-str [{:keys [width height]}]
  (str height "x" width))

(defn- op-dest-file
  [uri op dims]
  (let [res (fs/get-uri-resource uri)
        absolute (.getAbsolutePath res)
        dest (str absolute "-" op "-" (dims-str dims))]
    dest))

(defn resize
  "Resize the given URI to the requested dimensions."
  [uri dims]
;  (println (str "got dims: " dims))
  (let [in-file (fs/get-uri-resource uri)
        dimsstr (dims-str dims)
        dest (op-dest-file uri "resize" dims)
        dest-file (File. dest)]
    (if-not
        (.exists dest-file)
      (let [cv (osio/execute
                "convert" "-resize" dimsstr
                (.getAbsolutePath in-file)
                (.getAbsolutePath dest-file))]
        dest-file)
      dest-file)))

(defn crop
  "Crop the given URI starting at start-coords with
   the crop-dims and output to output-dims size."
  [uri output-dims start-coords crop-dims])

(resize "http://localhost:8000/zmr-pids.png" {:width 200 :height 200})

(dims-str {:width 1 :height 20})

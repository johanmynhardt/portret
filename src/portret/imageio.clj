(ns portret.imageio
  (:require [portret.fs :as fs]
            [portret.osio :as osio]
            [clojure.java.io :as io]))

(defn- dims-str
  [{:keys [width height]}]
  (str width "x" height))

(defn- op-dest-location
  "Calculate the operation destination location for the given uri and dimensions"
  [uri op dims]
  (str (.getAbsolutePath (fs/uri-cache-file uri)) "-" op "-" (dims-str dims)))

(defn- op-dest-file
  [uri op dims]
  (io/file (op-dest-location uri op dims)))

(defn resize
  "Resize the given URI to the requested dimensions."
  [uri dims sizing]
  (let [in-file (fs/get-uri-resource uri)
        dest-file (op-dest-file uri (str "resize-" sizing) dims)]
    (if-not
        (.exists dest-file)
      (let [dimsstr (dims-str dims)
            cv (osio/execute
                "convert" "-resize" (str dimsstr (if (= sizing "cover") "^" ">"))
                "-gravity" "center"
                (if (= sizing "cover") "-extent")
                (if (= sizing "cover") dimsstr)
                (.getAbsolutePath in-file)
                (.getAbsolutePath dest-file))]
        dest-file)
      dest-file)))

(defn crop-str [{:keys [width height]} {:keys [x y]}]
  (str width "x" height "+" x "+" y))

(defn crop
  "Crop the given URI starting at start-coords with
   the crop-dims and output to output-dims size."
  [uri output-dims start-coords crop-dims]
  (println (str "output: " output-dims ", start-coords: " start-coords ", crop-dims: " crop-dims))
  (let [in-file (fs/get-uri-resource uri)
        dimsstr (dims-str output-dims)
        cropstr (crop-str crop-dims start-coords)
        dest-file (op-dest-file uri (str "crop_" cropstr) output-dims)]
    (if-not
        (.exists dest-file)
      (let [
            cv (osio/execute "convert"
                             "-crop" cropstr
                             "-resize" dimsstr
                             (.getAbsolutePath in-file)
                             (.getAbsolutePath dest-file))]
        dest-file)
      dest-file)))

(defn exif
  [uri]
  (fs/uri-exif uri))

;(resize "http://localhost:8000/zmr-pids.png" {:width 200 :height 200})

;(dims-str {:width 1 :height 20})
(comment  (crop "http://localhost:8000/zmr-pids.png"
                {:width 400 :height 400}
                {:x 2000 :y 800}
                {:width 800 :height 800}))


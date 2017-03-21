(ns portret.fs
  (:require [multihash.digest :as digest]))

(defn store-path-for-uri
  "Calculate a relative path using a sha1 sum for the URI."
  [uri]
  (let [s1uri (:hex-digest (digest/sha1 uri))
        pre-path-parts (map #(str (nth % 0) (nth % 1)) (take 5 (partition 2 s1uri)))
        pre-path (reduce #(str %1 "/" %2) pre-path-parts)]
    (str pre-path "/" s1uri)))

(defn uri-available?
  "Determine if the bytes for a URI have been stored before."
  [uri]
  (.exists (java.io.File. (store-path-for-uri uri)))
)

(store-path-for-uri "https://www.google.co.za")
(uri-available? "https://www.google.co.za")

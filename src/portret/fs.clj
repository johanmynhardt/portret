(ns portret.fs
  (:require [clojure.data.json :as json]
            [multihash.digest :as digest]
            [portret.config :as config]
            [portret.osio :as osio]
            [clj-http.client :as client]))

(defn store-path-for-uri
  "Calculate a relative path using a sha1 sum for the URI."
  [uri]
  (let [s1uri (:hex-digest (digest/sha1 uri))
        pre-path-parts (map #(str (nth % 0) (nth % 1)) (take 5 (partition 2 s1uri)))
        pre-path (reduce #(str %1 "/" %2) pre-path-parts)]
    (str pre-path "/" s1uri)))

(defn uri-cache-file [uri]
  (java.io.File. (@config/app-config :fs-cache) (store-path-for-uri uri)))

(defn uri-available?
  "Determine if the bytes for a URI have been stored before."
  [uri]
  (let [file (uri-cache-file uri)]
    (println (str "Exists: " (.getAbsolutePath file)))
    (.exists file)))

(defn ensure-cache-dir-available! [uri]
  (let [cache-dir (.getParentFile (uri-cache-file uri))]
    (if-not (.exists cache-dir) (.mkdirs cache-dir))
    (.exists cache-dir)))

(defn- fetch-uri [uri]
  (client/get uri {:as :byte-array}))

(defn- write-to-disk [uri byte-array]
  (ensure-cache-dir-available! uri)
  (.write (java.io.FileOutputStream. (uri-cache-file uri)) byte-array))

(defn get-uri-resource [uri]
  (if-not (uri-available? uri)
    (write-to-disk uri (:body (fetch-uri uri))))
  (uri-cache-file uri))

(defn uri-exif
  "Pulls EXIF data from the resource at the specified URI."
  [uri]
  (let [f (get-uri-resource uri)
        file-path (.getAbsolutePath f)]
    (nth (json/read-str (osio/execute "exiftool" "-json" file-path) :key-fn keyword) 0)))

(defn uri-mime [uri]
  (:MIMEType (uri-exif uri)))

;(uri-mime "http://localhost:8000/zmr-pids.png")

;(:MIMEType (uri-exif "http://localhost:8000/zmr-pids.png"))

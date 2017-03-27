(ns portret.core
  (:use ring.adapter.jetty
        compojure.core)
  (:require [portret.imageio :as imageio]
            [compojure.handler :as handler])
  (:gen-class))

(defn app-handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (str request)})

(defn- parse-dims [dimstr]
  (let [parsed (re-matches #"(\d+)x(\d+)" dimstr)
        ints (map #(Integer/parseInt %) (filter #(re-matches #"\d+" %) parsed))]
    {:width (nth ints 0) :height (nth ints 1)}))

(defn- parse-coords [coordstr]
  (let [parsed (re-matches #"(\d+):(\d+)" coordstr)
        ints (map #(Integer/parseInt %) (filter #(re-matches #"\d+" %) parsed))]
    {:x (nth ints 0) :y (nth ints 1)}))

(defroutes app-routes
  (GET "/" [] "PONG!")
  (GET "/:name"
       [name]
       (str "hello, " name))
  (GET "/resize/:dims/s/:source"
       [dims source]
       (str "dims: " dims ", source: " source)
       (imageio/resize source (parse-dims dims)))
  (GET "/crop/:output-dims/c/:crop-dims/s/:source"
       [output-dims crop-dims source offset]
       (comment  (println (str "output: " output-dims
                               ", crop: " crop-dims
                               ", source: " source
                               ", offset: " offset)))
       (imageio/crop source
                     (parse-dims output-dims)
                     (if offset (parse-coords offset)
                         {:x 0 :y 0})
                     (parse-dims crop-dims)
                     )))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Starting jetty...")
  (run-jetty (handler/site app-routes) {:port 3000}))

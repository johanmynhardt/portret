(ns portret.core
  (:use ring.adapter.jetty
        compojure.core
        [hiccup.middleware :only (wrap-base-url)])
  (:require [portret.imageio :as imageio]
            [portret.pages :as pages]
            [compojure.handler :as handler]
            [compojure.route :as route])
  (:gen-class))

(defn app-handler
  [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (str request)})

(defn- parse-dims
  [dimstr]
  (let [parsed (re-matches #"(\d+)x(\d+)" dimstr)
        ints (map #(Integer/parseInt %) (filter #(re-matches #"\d+" %) parsed))]
    {:width (nth ints 0) :height (nth ints 1)}))

(defn- parse-coords
  [coordstr]
  (let [parsed (re-matches #"(-?\d+):(-?\d+)" coordstr)
        ints (map #(Integer/parseInt %) (filter #(re-matches #"-?\d+" %) parsed))]
    {:x (nth ints 0) :y (nth ints 1)}))


(defroutes app-routes
  (GET "/" request (pages/home request))
  (GET "/resize/:dims/s/:source"
       [dims source sizing]
       (imageio/resize source (parse-dims dims) (if sizing sizing "contain")))
  (GET "/crop/:dims/c/:crop-dims/s/:source"
       [dims crop-dims source offset sizing]
       (imageio/crop source
                     (parse-dims dims)
                     (if offset (parse-coords offset) {:x 0 :y 0})
                     (parse-dims crop-dims)
                     (if sizing sizing "contain")))
  (GET "/exif/s/:source" [source]
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (clojure.data.json/write-str (imageio/exif source))})
  (GET "/help" request
       (pages/help request))
  (GET "/generate" request
       (pages/generate request))
  (route/files "/assets" {:root "assets"})
  (route/resources "/")
  (route/not-found (fn [req]
                     (println (str "not found: " (:uri req)))
                     (println (str "req: " req))
                     (pages/not-found req))))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (get-in @portret.config/app-config [:server :port])]
    (println (str "Starting jetty or port " port))
    (run-jetty app
               {:port port})))

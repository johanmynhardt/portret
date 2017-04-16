(ns portret.pages
  (:use [hiccup.core]
        [hiccup.page]))

(defn- wrap-context-path
  [request path]
  (if-let [context (:servlet-context-path request)]
    (str context path)
    path))

(defn- page
  [request content]
  (str (html [:html
              [:head
               (hiccup.page/include-css "/assets/styles/default.css")]
              [:body [:a {:href (wrap-context-path request "/")} "/"] content]])))

(defn- section
  [heading content]
  [:div {:class "notice-dialog"}
   [:h1 {:class "heading"} heading]
   [:div {:class "content"} content]])

(defn home
  [request]
  (page request
   (section "Portret 0.1.0"
                  (list [:p "See:"]
                        [:a {:href (wrap-context-path request "/help")} "/help"]))))


(defn help
  [request]
  (page request
   (section "Help" 
                  (list [:p "The following endpoints are available"]
                        [:ul
                         [:li [:code "/resize/:dims/s/:source?sizing=(cover|contain)"]]
                         [:li [:code (str "/crop/:dims/c/crop-dims/s/:source?sizing=(cover|contain)&offset=x:y")]]
                         [:li [:code "/exif/s/:source"]]
                         [:li [:a {:href (wrap-context-path request "/generate")} [:code "/generate"]]]]))))

(defn generate
  [request]
  (println (str "generate: Got request: " (:query-params request)))
  (page request
   (section
    "Generate"
    (list [:p "Use the following form to generate a URL"]
          [:form {:action (wrap-context-path request "/generate")}
           [:label "Source"]
           [:input {:type "text" :name "source"}]
           [:button {:type "submit"} "Submit"]]
          (if-let [source (get (:query-params request) "source")]
            (let [link (str "/resize/" "150x150" "/s/" (java.net.URLEncoder/encode source))]
              [:div
               [:em "Got source, link for the image: " [:a {:href (wrap-context-path request link)} [:code link]]]
               [:br]
               [:img {:src (wrap-context-path request link)}]
               ]))))))


(defn not-found
  [req]
  (page req
   (section "¯\\_(ツ)_/¯ HTTP/404"
                  (str (html [:p "Resource " [:em (:uri req)] " is unavailable."])))))

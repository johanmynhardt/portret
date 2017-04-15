(ns portret.pages
  (:use [hiccup.core]))

(defn- page
  [content]
  (str (html [:html
              [:head
               [:link {:rel "stylesheet" :href "/assets/styles/default.css"}]]
              [:body [:a {:href "/"} "/"] content]])))

(defn- section
  [heading content]
  [:div {:class "notice-dialog"}
   [:h1 {:class "heading"} heading]
   [:div {:class "content"} content]])

(defn home
  []
  (page
   (section "Portret 0.1.0"
                  (list [:p "See:"]
                        [:a {:href "/help"} "/help"]))))


(defn help
  []
  (page
   (section "Help" 
                  (list [:p "The following endpoints are available"]
                        [:ul
                         [:li [:code "/resize/:dims/s/:source?sizing=(cover|contain)"]]
                         [:li [:code (str "/crop/:dims/c/crop-dims/s/:source?sizing=(cover|contain)&offset=x:y")]]
                         [:li [:code "/exif/s/:source"]]
                         [:li [:a {:href "/generate"} [:code "/generate"]]]]))))

(defn generate
  [query-params]
  (println (str "generate: Got request: " query-params))
  (page
   (section
    "Generate"
    (list [:p "Use the following form to generate a URL"]
          [:form {:action "/generate"} 
           [:label "Source"]
           [:input {:type "text" :name "source"}]
           [:button {:type "submit"} "Submit"]]
          (if-let [source (get query-params "source")]
            (let [link (str "/resize/" "150x150" "/s/" (java.net.URLEncoder/encode source))]
              [:div
               [:em "Got source, link for the image: " [:a {:href link} [:code link]]]
               [:br]
               [:img {:src link}]
               ]))))))


(defn not-found
  [req]
  (page 
   (section "Not Found! ¯\\_(ツ)_/¯ "
                  (str (html [:p "Resource " [:em (:uri req)] " is unavailable."])))))

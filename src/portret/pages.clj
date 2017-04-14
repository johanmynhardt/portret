(ns portret.pages
  (:use [hiccup.core]))

(defn- page
  [content]
  (str (html [:html
              [:head
               [:link {:rel "stylesheet" :href "/assets/styles/default.css"}]]
              [:body content]])))

(defn- notice-dialog
  [heading content]
  [:div {:class "notice-dialog"}
   [:h1 {:class "heading"} heading]
   [:div {:class "content"} content]])

(defn home
  []
  (page
   (notice-dialog "Portret 0.1.0"
                  (list [:p "See:"]
                        [:a {:href "/help"} "/help"]))))


(defn help
  []
  (page
   (notice-dialog "Help" 
                  (list [:p "The following endpoints are available"]
                        [:ul
                         [:li [:code "/resize/:dims/s/:source?sizing=(cover|contain)"]]
                         [:li [:code (str "/crop/:dims/c/crop-dims/s/:source
                    ?sizing=(cover|contain)&offset=x:y")]]
                         [:li [:code "/exif/s/:source"]]]))))


(defn not-found
  [req]
  (page 
   (notice-dialog "Not Found! ¯\\_(ツ)_/¯ "
                  (str (html [:p "Resource " [:em (:uri req)] " is unavailable."])))))

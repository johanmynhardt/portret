(ns portret.pages
  (:use [hiccup.core]))

(def home
  (html [:h1 "Portret 0.1.0"]
        [:p "See:"]
        [:a {:href "/help"} "/help"]))


(def help
  (html [:h1 "Help"]
        [:p "The following endpoints are available"]
        [:ul
         [:li "/resize/:dims/s/:source?sizing=(cover|contain)"]
         [:li (str "/crop/:dims/c/crop-dims/s/:source
                    ?sizing=(cover|contain)&offset=x:y")]
         [:li "/exif/s/:source"]]))

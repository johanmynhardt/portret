(ns portret.pages
  (:use [hiccup.core]
        [hiccup.page])
  (:require [hiccup.util :as util]
            [hiccup.element :as element]
            [hiccup.form :as form]))

(defn- wrap-context-path
  [request path]
  (if-let [context (:servlet-context-path request)]
    (str context path)
    path))

(defn- page
  [request content]
  (str (html [:html
              [:head
               (hiccup.page/include-css "/css/default.css")]
              [:body
               (element/link-to "/" "/")
               content]])))

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
                        (element/link-to "/help" "/help")))))


(defn help
  [request]
  (page request
   (section "Help" 
                  (list [:p "The following endpoints are available"]
                        [:ul
                         [:li [:code "/resize/:dims/s/:source?sizing=(cover|contain)"]]
                         [:li [:code (str "/crop/:dims/c/crop-dims/s/:source?sizing=(cover|contain)&offset=x:y")]]
                         [:li [:code "/exif/s/:source"]]
                         [:li (element/link-to "/generate" "/generate")]]))))

(defn generate
  [request]
  (println (str "generate: Got request: " (:query-params request)))
  (page request
   (section
    "Generate"
    (list [:p "Use the following form to generate a URL"]
          (form/form-to [:get "/generate"]
                        (form/label "source" "Source")
                        (form/text-field "source")
                        (form/submit-button "Submit"))

          (if-let [source (get (:query-params request) "source")]
            (let [link (util/url "/resize/" "150x150" "/s/" (util/url-encode source))]
              [:div
               [:em "Got source, link for the image: " (element/link-to link [:code link])]
               [:br]
               (element/image link)
               ]))))))


(defn not-found
  [req]
  (page req
   (section "¯\\_(ツ)_/¯ HTTP/404"
                  (str (html [:p "Resource " [:em (:uri req)] " is unavailable."])))))

(defproject portret "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/johanmynhardt/portret"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.sharneng/gm4java "1.1.1"]
                 [com.google.guava/guava "21.0"]
                 [clj-http "2.3.0"]
                 [mvxcvi/multihash "2.0.1"]
                 ;[]
                 ]
  :main ^:skip-aot portret.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :repositories [["local" "file:lib"]])

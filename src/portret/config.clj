(ns portret.config)

(def app-config (atom {:fs-cache "/tmp/portret/cache"
                       :server {:port 3000}}))


(:fs-cache @app-config)

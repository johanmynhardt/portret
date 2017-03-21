(ns portret.config)

(def app-config (atom {:fs-cache "/tmp/portret/cache"}))

(:fs-cache @app-config)

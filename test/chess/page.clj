(ns chess.page
  (:import org.jsoup.Jsoup))

(defn page [url]
  (.get (Jsoup/connect url)))

(defn links [page]
  (.select page "a"))

(defn url [link]
  (.attr link "href"))


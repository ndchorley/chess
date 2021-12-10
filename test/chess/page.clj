(ns chess.page
  (:import org.jsoup.Jsoup))

(defn page [url]
  (.get (Jsoup/connect url)))

(defn links [page]
  (.select page "a"))

(defn url [link]
  (.attr link "href"))

(defn text [links]
  (map
   (fn [link] (.text link))
   links))

(defn title [page]
  (.text (.selectFirst page "title")))

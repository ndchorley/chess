(ns chess.functional.page
  (:import org.jsoup.Jsoup))

(defn page [response]
  (Jsoup/parse (response :body)))

(defn dates [timeline]
  (map
   (fn [element] (.text element))
   (.select timeline ".date")))

(defn games [timeline]
  (.select timeline ".game"))

(defn white [game]
  (.text (.selectFirst game ".white")))

(defn black [game]
  (.text (.selectFirst game ".black")))

(defn result [game]
  (.text (.selectFirst game ".result")))

(defn link [game] (.selectFirst game "a"))

(defn text [link] (.text link))

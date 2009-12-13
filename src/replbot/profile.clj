(ns replbot.profile (:gen-class :extends com.google.wave.api.ProfileServlet))

(defn -getRobotName           [] "repl bot")
(defn -getRobotProfilePageUrl [] "http://github.com/nick-orton/repl-bot")
(defn -getRobotAvatarUrl      [] "http://repl-bot.appspot.com/avatar.png")

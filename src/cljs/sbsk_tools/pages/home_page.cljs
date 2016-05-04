(ns sbsk-tools.pages.home-page)

(defn home-page []
  [:div.container
   [:div.jumbotron
    [:h1 "Sverresborg Bueskytterverktøy"]
    [:p "Her finnes diverse hjelpsomme verktøy ifm. bueskyting i Norge."]]
   [:div.row
    [:div.col-md-12 [:a {:href "#/events"} "Stevner"]]]])
(ns sbsk-tools.pages.home-page)

(defn home-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:h3 "Stevner i Norge"]
     [:ul [:li [:a {:href "#/events"} "Stevner"]]]
     [:h3 "Eksterne lenker"]
     [:h4 "Kjøpe bueutstyr"]
     [:ul
      [:li [:a {:href "http://www.buesport.no"} "Midt-Norsk Buesport"]]
      [:li [:a {:href "http://www.bueutstyr.no"} "Humlekjær Archery"]]
      [:li [:a {:href "http://eriksensport.no"} "Eriksen Sport"]]
      [:li [:a {:href "http://www.pilogbue.com/"} "Catos Buesport"]]]
     [:h4 "Piler og tuning"]
     [:ul
      [:li "Easton tuning guide"]
      [:li "Easton arrow shaft selector"]
      [:li "Easton spine calculator"]]]]])
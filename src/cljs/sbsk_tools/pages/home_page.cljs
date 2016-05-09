(ns sbsk-tools.pages.home-page)

(defn home-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:h3 "Våre verktøy"]
     [:ul [:li [:a {:href "#/events"} "Stevneoversikt"]]]
     [:h3 "Eksterne lenker"]
     [:h5 "Kjøpe bueutstyr"]
     [:ul
      [:li [:a {:href "http://www.buesport.no"} "Midt-Norsk Buesport"]]
      [:li [:a {:href "http://www.bueutstyr.no"} "Humlekjær Archery"]]
      [:li [:a {:href "http://eriksensport.no"} "Eriksen Sport"]]
      [:li [:a {:href "http://www.pilogbue.com/"} "Catos Buesport"]]]
     [:h5 "Piler og tuning"]
     [:ul
      [:li [:a {:href "http://www.eastonarchery.com/downloads/tuning-guide"} "Easton tuning guide"]]
      ]]]])
# sbsk-tools

Web application to host various archery related tools for use by [Sverresborg Bueskyttere in Trondheim][1].


## Features
### Event list
#### Current and planned immediately

* [x] Fetch events from nor.service.ianseo.net
* [x] Show events in a simple timeline
* [x] Simple filtering of future events only
* [X] Simple filtering of events local to region Midt-Norge
* [x] Cache mechanism so page is loaded from main server if older than 10 minutes
* [ ] Possibility to custom filter events
* [ ] Save custom filter in local storage
* [ ] Better display of events in event list
* [ ] Show scores for finished events


#### Future plans

* Event signups
* Link collection to online resources (arrow selectors, tuning guides etc.)


## Prerequisites

You will need [Leiningen][2] 2.0 or above installed.


## Running

To start a web server for the application, run:

    lein run


[1]: https://sbsk.no
[2]: https://github.com/technomancy/leiningen

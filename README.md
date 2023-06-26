# android-app-web-scraper
A Python server application, which runs a web scraper, with an Android application to access the scrapped data.

## Python server
The Python server application scrapes a cllasses schedule from a website using BeautifulSoup library and Selenium to deal with certification issues. Scraped data are saved to a json file. The HTTP server handles GET requests and posts data requested with a URL query.

## Android application
The Android application, written in Kotlin, displays the schedule requested from the server. It allows to set filters for the schedule and based on these settings sends a request to the server. It also allows to request the server to rerun the scraper.

<img src="https://github.com/mickol111/android-app-web-scraper/assets/22640141/40bd6b61-3593-42de-bdf7-8da3f73e444e" width=50% height=50%><br>
<i>Fig.1. Android app - search filters settings</i>

<img src="https://github.com/mickol111/android-app-web-scraper/assets/22640141/d9bf6ccc-ba77-4982-82f8-bda53ceb07aa" width=50% height=50%><br>
<i>Fig.2. Android app - schedule display</i>

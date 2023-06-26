import json
import urllib.parse
from http.server import HTTPServer, BaseHTTPRequestHandler

import scraper_selenium


class MyRequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "application/json")
        self.end_headers()
        response = self.gen_page()
        self.wfile.write(bytes(response, "utf8"))

    def gen_page(self):
        if self.path == "/favicon.ico": return ""
        parsedURL = urllib.parse.urlparse(self.path)
        print(parsedURL)
        parsedQuery = urllib.parse.parse_qs(parsedURL.query)
        print(parsedQuery)
        del_flag = False
        for key in parsedQuery:
            if key == 'scrape':
                print("scrape")
                # scraper_process.run_scrapper()
                scraper_selenium.run_scraper()
                del_flag = True
        if del_flag:
            del parsedQuery["scrape"]
            del_flag = False


        f = open('items.json', encoding='utf-8')
        data = json.load(f)
        l0 = []
        l = []
        keyNum = 0
        for key in parsedQuery:
            print(key)

            for i in parsedQuery.get(key):
                print(key + ": " + i)
                if keyNum == 0:
                    l0.extend(list(filter(lambda x: x[key] == i, data)))
                else:
                    l.extend(list(filter(lambda x: x[key] == i, l0)))

            keyNum=keyNum+1
        if keyNum == 1:
            l=l0
        response = ",".join(str(item) for item in l)
        response = response.replace("'", "\"")
        response = "["+response+"]"
        print(response)

        f.close()
        return response


serverHttp = HTTPServer(("localhost", 1234), MyRequestHandler)
print("start")
serverHttp.serve_forever()
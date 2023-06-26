from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from urllib.request import urlopen
from bs4 import BeautifulSoup
import json

def run_scraper():
    options = Options()
    options.headless = True
    options.add_argument("--window-size=1920,1200")
    options.add_argument('--ignore-ssl-errors=yes')
    options.add_argument('--ignore-certificate-errors')

    driver = webdriver.Chrome(options=options, executable_path='D:\STU\II\3\po\proj2\chromedriver_win32')
    driver.get("http://akademiatancasmooth.pl/grafik-zajec/")
    print(driver.page_source)


    page_soup = BeautifulSoup(driver.page_source, 'html.parser')
    containers = page_soup.findAll("li",{"class":"mptt-list-event"})
    data = [] # create a list to store the items
    for container in containers:
        item = {}
        item['day'] = container.parent.parent.find('h3',attrs={'class':'mptt-column-title'}).text
        item['lesson'] = container.find('a', attrs={'class': 'mptt-event-title'}).text.replace('\t',
                                                                                                             '').replace(
                        '\n', '')
        item['timeslot-start'] = container.find('time', attrs={'class': 'timeslot-start'})['datetime']
        item['timeslot-end'] = container.find('time', attrs={'class': 'timeslot-end'})['datetime']
        item['location'] = container.find('p', attrs={'class': 'event-description'}).text.replace('\t',
                                                                                                             '').replace(
                        '\n', '')
        item['ref'] = container.find('a', attrs={'class': 'mptt-event-title'})['href']

        data.append(item) # add the item to the list


    with open("items.json", "w", encoding='utf8',) as writeJSON:
        json.dump(data, writeJSON, ensure_ascii=False)
    driver.quit()
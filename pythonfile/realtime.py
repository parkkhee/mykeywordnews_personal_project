from ast import keyword
import pymysql
import requests
from bs4 import BeautifulSoup
from webdriver_manager.chrome import ChromeDriverManager
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
import schedule
import time


def schedule_fuction():

    try:

        chrome_options = webdriver.ChromeOptions()
        # 크롬 창 숨기는 옵션 추가
        chrome_options.add_argument("headless")
        driver = webdriver.Chrome(service=Service(
            ChromeDriverManager().install()), options=chrome_options)

        URL = 'https://signal.bz/news'
        driver.get(url=URL)
        driver.implicitly_wait(time_to_wait=10)

        naver_results = driver.find_elements(
            By.CSS_SELECTOR, '#app > div > main > div > section > div > section > section:nth-child(2) > div > div > div > div > a > span.rank-text')

        naver_list = []

        for naver_result in naver_results:
            print(naver_result.text)
            naver_list.append(naver_result.text)

        db = pymysql.connect(host='localhost', port=3306, user='root',
                             passwd='qkr96#', db='mykeywordnews', charset='utf8')

        cursor = db.cursor()

        update_sql = "UPDATE realtime_keyword SET rtkeyword_no=%s, keyword1=%s, keyword2=%s, keyword3=%s, keyword4=%s, keyword5=%s, keyword6=%s, keyword7=%s, keyword8=%s, keyword9=%s, keyword10=%s;"
        insert_sql = "INSERT INTO realtime_keyword VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"

        cursor.execute(
            insert_sql, (1, naver_list[0], naver_list[1], naver_list[2], naver_list[3], naver_list[4], naver_list[5], naver_list[6], naver_list[7], naver_list[8], naver_list[9]))

        db.commit()
        db.close()

        print(naver_list)
    except:
        pass


# 1초마다 test_fuction을 동작시키자
schedule.every(1).seconds.do(schedule_fuction)

while True:
    schedule.run_pending()
    time.sleep(5)

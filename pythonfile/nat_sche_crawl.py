
import pymysql
from bs4 import BeautifulSoup
import requests
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium import webdriver
import re
from konlpy.tag import Okt
import collections
import time
import schedule
import json
import sys


class ClovaSummary:
    # Clova Speech invoke URL

    url = 'https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize'
    client_id = "hqc8tqgwkq"
    client_secret = "j2i5KecP8QJ8sr4nUA3JXn1V4VFlWMj3eJZ2p0Tb"

    def req(self, content):
        request_body = {
            "document": {
                "content": content
            },
            "option": {
                "language": 'ko',
                "model": "news",
                "summaryCount": 2,
                "tone": 3
            }
        }
        headers = {
            'Accept': 'application/json;UTF-8',
            'Content-Type': 'application/json;UTF-8',
            'X-NCP-APIGW-API-KEY-ID': self.client_id,
            'X-NCP-APIGW-API-KEY': self.client_secret
        }
        return requests.post(headers=headers,
                             url=self.url,
                             data=json.dumps(request_body).encode('UTF-8'))


def schedule_fuction():

    try:

        db = pymysql.connect(host='localhost', port=3306, user='root',
                             passwd='qkr96#', db='mykeywordnews', charset='utf8')

        # print(db)
        cursor = db.cursor()

        # if(kw == 1):
        #     cursor.execute("TRUNCATE keyword;")
        select_sql = "SELECT * from user where user_no=%s;"
        select_news_sql = "SELECT user_key, user_no from nate_keyword where user_no=%s;"
        select_cnt = "SELECT count(*) from mykeywordnews.user;"

        insert_sql = "UPDATE nate_keyword SET nate_news_url1=%s, nate_news_word1_1=%s, nate_news_word1_2=%s, nate_news_url2=%s, nate_news_word2_1=%s, nate_news_word2_2=%s, nate_news_url3=%s, nate_news_word3_1=%s, nate_news_word3_2=%s, nate_news_url4=%s, nate_news_word4_1=%s, nate_news_word4_2=%s, nate_word_cnt1=%s, nate_word_cnt2=%s, nate_word_cnt3=%s, nate_word_cnt4=%s, nate_summary1=%s, nate_summary2=%s, nate_summary3=%s, nate_summary4=%s WHERE user_no = %s and user_key = %s;"

        cursor.execute(select_cnt)
        cnt_sql = cursor.fetchall()

        print(cnt_sql[0][0])

        for user_num in range(1, int(cnt_sql[0][0]+1)):

            time.sleep(4)

            user_num = int(user_num)
            print(user_num)

            cursor.execute(select_sql, user_num)
            res = cursor.fetchall()
            print(res[0][0])

            cursor.execute(select_news_sql, res[0][0])
            res2 = cursor.fetchall()
            print(res2)

            nate_keyword = []
            nate_keyword.append(res2[0][0])
            nate_keyword.append(res2[1][0])

            for search in nate_keyword:
                print(search)
                chrome_options = webdriver.ChromeOptions()
                chrome_options.add_experimental_option(
                    "excludeSwitches", ["enable-logging"])
                # 크롬 창 숨기는 옵션 추가
                # chrome_options.add_argument("headless")
                dr = webdriver.Chrome(service=Service(
                    ChromeDriverManager().install()), options=chrome_options)

                c = 0
                nate_ns_url = []
                while(c < 4):
                    # url 생성
                    url = "https://news.nate.com/search?q=" + search

                    # ConnectionError방지
                    headers = {
                        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/100.0.48496.75"}

                    # html불러오기
                    original_html = requests.get(url, headers=headers)
                    html = BeautifulSoup(original_html.text, "html.parser")

                    # 검색결과
                    articles = html.select(
                        "div.search-result > ul.search-list > li.items >  a.info")

                    # 뉴스기사 URL 가져오기

                    articles = html.select(
                        "div.search-result > ul.search-list > li.items >  a")[c]['href']
                    nate_ns_url.append("https:" + articles)
                    c += 1
                print(nate_ns_url)

                # 뉴스 url을 이용해 뉴스 본문 전처리

                ss = []
                ss_cnt = 0
                nateWordCnt = []
                summaryList = []

                for i in nate_ns_url:
                    contents = []
                    # html불러오기
                    o_html = requests.get(i, headers=headers)
                    soup = BeautifulSoup(o_html.text, "html.parser")
                    artc = ""
                    # artc = soup.select("div.articleContents")
                    artc = soup.select("div.articleContetns")
                    # 검색결과
                    if not artc:
                        artc = soup.select("div#articleContetns")

                    artc = ''.join(str(artc))

                    # 뉴스기사 전처리
                    pattern1 = '<[^>]*>'
                    pattern2 = """[\n\n\n\n\n// flash 오류를 우회하기 위한 함수 추가\nfunction _flash_removeCallback() {}"""
                    pattern3 = '[\t|\n]'

                    artc = re.sub(pattern=pattern1, repl='', string=artc)
                    artc = re.sub(pattern=pattern3, repl='', string=artc)
                    artc = artc.replace(pattern2, '')

                    contents.append(artc)

                    ctt = contents[0]

                    # 크롤링한 기사 요약하기 API
                    WORDS = 1999
                    summary = ""

                    for i in range((len(ctt)//WORDS)+1):
                        # print(i, "번째***********")
                        res = ClovaSummary().req(
                            ctt[WORDS*i:WORDS*(i+1)])
                        rescode = res.status_code
                        if(rescode == 200):
                            # print(str(i) + res.text)
                            summary += json.loads(res.text)["summary"]
                            # summary += res.text["summary"]
                        else:
                            print("first Error : " + res.text)

                    if (len(ctt)//WORDS) > 0:
                        res = ClovaSummary().req(summary)
                        rescode = res.status_code
                        if(rescode == 200):
                            # print(res.text)
                            summary = json.loads(res.text)["summary"]
                        else:
                            print("Error : " + res.text)

                    summaryList.append(summary)
                    print("최종 summary")
                    print(summary)
                    print("-------------------------------")
                    print("-------------------------------")

                    # 형태소 별로 분리해주는 라이브러리
                    nlpy = Okt()

                    nouns = nlpy.nouns(contents[0])  # 기사 본문을 명사로 분리

                    # 한자수의 단어 배제
                    for enum, notone in enumerate(nouns):
                        if len(notone) < 2:
                            nouns.pop(enum)

                    # print(nouns)
                    wcnt = collections.Counter(nouns)

                    cnt = 0
                    num = 0  # 숫자를 db에 저장하기 위한 변수
                    i = 1
                    j = 0

                    while(cnt != 2):
                        if wcnt.most_common(i)[j][0] == search:
                            j += 1
                            i += 1
                            continue
                        else:
                            ss.append(wcnt.most_common(i)[j][0])
                            print('흔한 단어는?: ', ss[ss_cnt], "\n")
                            num = max(num, wcnt[ss[ss_cnt]])
                            cnt += 1
                            ss_cnt += 1
                            i += 1
                            j += 1
                    nateWordCnt.append(num)

                print(ss)
                try:
                    cursor.execute(
                        insert_sql, (nate_ns_url[0], ss[0], ss[1], nate_ns_url[1], ss[2], ss[3], nate_ns_url[2], ss[4], ss[5], nate_ns_url[3], ss[6], ss[7], nateWordCnt[0], nateWordCnt[1], nateWordCnt[2], nateWordCnt[3], summaryList[0], summaryList[1], summaryList[2], summaryList[3], res2[0][1], search))
                except:
                    pass
            try:
                db.commit()
            except:
                pass
        db.close()
    except:
        pass


# 1초마다 test_fuction을 동작시키자
schedule.every(1).seconds.do(schedule_fuction)

while True:
    schedule.run_pending()
    time.sleep(5)

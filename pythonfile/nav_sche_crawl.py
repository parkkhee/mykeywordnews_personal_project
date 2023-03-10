
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
from selenium.webdriver.common.by import By
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

        insert_sql = "UPDATE naver_keyword SET naver_news_url1=%s, naver_news_word1_1=%s, naver_news_word1_2=%s, naver_news_url2=%s, naver_news_word2_1=%s, naver_news_word2_2=%s, naver_news_url3=%s, naver_news_word3_1=%s, naver_news_word3_2=%s, naver_news_url4=%s, naver_news_word4_1=%s, naver_news_word4_2=%s, naver_word_cnt1=%s, naver_word_cnt2=%s, naver_word_cnt3=%s, naver_word_cnt4=%s, naver_summary1=%s, naver_summary2=%s, naver_summary3=%s, naver_summary4=%s WHERE user_no = %s and user_key = %s;"

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
                # ?????? ??? ????????? ?????? ??????
                # chrome_options.add_argument("headless")
                dr = webdriver.Chrome(service=Service(
                    ChromeDriverManager().install()), options=chrome_options)

                # url ??????
                url = "https://search.naver.com/search.naver?where=news&sm=tab_pge&query=" + search

                # articles ????????? ?????????
                #atc_template = ["div.search-result > ul.search-list > li.items >  a.info"]

                # Webdriver?????? ????????? ????????? ??????
                dr.get(url)
                time.sleep(1)  # ???????????? ?????? ??????

                # ????????? ?????? ????????? ?????? ??? ?????? ????????????#
                # ????????? ????????? ?????? ?????? css selector ????????????
                a = dr.find_elements(
                    By.CSS_SELECTOR, 'a.info')

                naver_urls = []
                count = 0  # ????????? ??????(??? ????????? ??????)

                # 4?????? ??????????????? ???????????? ?????? ?????????
                acnt = 0

                # ????????? ????????? css selector list ????????? ???????????? ?????? url??????
                for i in a:
                    # ?????? ????????? ???????????? ?????? conut ??????
                    count += 1
                    i.click()

                # ???????????? ??????
                    dr.switch_to.window(dr.window_handles[count-1])
                    time.sleep(1)  # ???????????? ?????? ??????

                    # ????????? ?????? url??? ????????????

                    url = dr.current_url
                    # print(url)

                    # ????????? ?????? ??????
                    while len(dr.window_handles) != count+1:
                        dr.switch_to.window(
                            dr.window_handles[len(dr.window_handles) - 1])
                        dr.close()

                    if "n.news.naver.com" in url:
                        naver_urls.append(url)
                        acnt += 1
                    elif "entertain.naver.com" in url:
                        naver_urls.append(url)
                        acnt += 1
                    elif "sports.news.naver.com" in url:
                        naver_urls.append(url)
                        acnt += 1
                    else:
                        pass

                    # ???????????? ????????? ????????????(?????? ??????!!!)
                    dr.switch_to.window(dr.window_handles[0])

                    if len(naver_urls) == 4:
                        break

                print(naver_urls)
                # driver ??????  ????????? ?????? ???????????? quit()????????? ????????? ??????. ???????????? background?????? chrome??? ?????? ???????????? ???????????????.
                dr.quit()

                naverWordCnt = []
                summaryList = []

                # if (len(naver_urls) == 3):
                #     for _ in range(1):
                #         summaryList.append('')
                #         naverWordCnt.append('')
                #         naver_urls.append('')
                # elif(len(naver_urls) == 2):
                #     for _ in range(2):
                #         summaryList.append('')
                #         naverWordCnt.append('')
                #         naver_urls.append('')
                # elif(len(naver_urls) == 1):
                #     for _ in range(3):
                #         summaryList.append('')
                #         naverWordCnt.append('')
                #         naver_urls.append('')

                if naver_urls:

                    ###naver ?????? ?????? ??? ?????? ????????????###

                    # ConnectionError??????
                    headers = {
                        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/98.0.4758.102"}

                    contents = []

                    # ss????????? ???????????? ???????????? ?????? ??????
                    ss_cnt = 0
                    ss = []

                    for i in naver_urls:
                        original_html = requests.get(i, headers=headers)
                        html = BeautifulSoup(original_html.text, "html.parser")
                        # ?????????????????????
                        # print(html)

                        # html????????????
                        edit_pattern1 = '<[^>]*>'

                        # ?????? ?????? ????????????
                        if "sports.news.naver.com" in i:
                            naver_news_content = html.select(
                                "div.content_area > div.news_end")
                        elif "entertain.naver.com" in i:
                            naver_news_content = html.select(
                                "div.end_body_wrp > div.article_body")
                        elif "n.news.naver.com" in i:
                            naver_news_content = html.select(
                                "div.newsct_body > div#newsct_article > div#dic_area")
                        # ?????? ???????????? ????????????
                        # list?????????
                        naver_news_content = ''.join(str(naver_news_content))
            # "sports.news.naver.com" in naver_urls:   "n.news.naver.com" in naver_urls:
                        # html????????????2
                        edit_pattern2 = '[\t|\n]'

                        # html???????????? ??? ????????? ?????????
                        naver_news_content = re.sub(
                            pattern=edit_pattern1, repl='', string=naver_news_content)
                        naver_news_content = re.sub(
                            pattern=edit_pattern2, repl='', string=naver_news_content)
                        # ???????????? ????????? ????????? ???????????? ????????? edit_pattern3??? ????????? ?????? ????????????
                        edit_pattern3 = """[\n\n\n\n\n// flash ????????? ???????????? ?????? ?????? ??????\nfunction _flash_removeCallback() {}"""
                        naver_news_content = naver_news_content.replace(
                            edit_pattern3, '')

                        # naver_news_content = naver_news_content.replace(" ", "/")

                        # ???????????? ?????? ???????????? API
                        WORDS = 1999
                        summary = ""

                        for i in range((len(naver_news_content)//WORDS)+1):
                            # print(i, "??????***********")
                            res = ClovaSummary().req(
                                naver_news_content[WORDS*i:WORDS*(i+1)])
                            rescode = res.status_code
                            if(rescode == 200):
                                # print(str(i) + res.text)
                                summary += json.loads(res.text)["summary"]
                                # summary += res.text["summary"]
                            else:
                                print("first Error : " + res.text)

                        if (len(naver_news_content)//WORDS) > 0:
                            res = ClovaSummary().req(summary)
                            rescode = res.status_code
                            if(rescode == 200):
                                # print(res.text)
                                summary = json.loads(res.text)["summary"]
                            else:
                                print("Error : " + res.text)

                        summaryList.append(summary)
                        print("?????? summary")
                        print(summary)
                        print("-------------------------------")
                        print("-------------------------------")

                        # ????????? ?????? ??????????????? ???????????????
                        nlpy = Okt()

                        nouns = nlpy.nouns(naver_news_content)  # ?????? ????????? ????????? ??????

                        # ???????????? ?????? ??????
                        for enum, notone in enumerate(nouns):
                            if len(notone) < 2:
                                nouns.pop(enum)

                        # ls = []
                        # ls = naver_news_content.split("/")
                        # ls = ' '.join(ls).split()

                        # ???????????? ??????????
                        word_count = collections.Counter(nouns)

                        cnt = 0
                        num = 0
                        i = 1
                        j = 0
                        while(cnt != 2):
                            if word_count.most_common(i)[j][0] == search:
                                j += 1
                                i += 1
                                continue
                            else:
                                ss.append(word_count.most_common(i)[j][0])
                                print('?????? ??????????: ', ss[ss_cnt], "\n")
                                num = max(num, word_count[ss[ss_cnt]])
                                cnt += 1
                                ss_cnt += 1
                                i += 1
                                j += 1
                        naverWordCnt.append(num)

                        contents.append(naver_news_content)

                    if (len(naver_urls) == 3):
                        for _ in range(1):
                            summaryList.append('')
                            naverWordCnt.append(0)
                            naver_urls.append('')
                            ss.append('')
                            ss.append('')
                    elif(len(naver_urls) == 2):
                        for _ in range(2):
                            summaryList.append('')
                            naverWordCnt.append(0)
                            naver_urls.append('')
                            ss.append('')
                            ss.append('')
                    elif(len(naver_urls) == 1):
                        for _ in range(3):
                            summaryList.append('')
                            naverWordCnt.append(0)
                            naver_urls.append('')
                            ss.append('')
                            ss.append('')
                try:
                    cursor.execute(
                        insert_sql, (naver_urls[0], ss[0], ss[1], naver_urls[1], ss[2], ss[3], naver_urls[2], ss[4], ss[5], naver_urls[3], ss[6], ss[7], naverWordCnt[0], naverWordCnt[1], naverWordCnt[2], naverWordCnt[3], summaryList[0], summaryList[1], summaryList[2], summaryList[3], res2[0][1], search))
                except:
                    pass
            try:
                db.commit()
            except:
                pass
        db.close()
    except:
        pass


# 1????????? test_fuction??? ???????????????
schedule.every(1).seconds.do(schedule_fuction)

while True:
    schedule.run_pending()
    time.sleep(1)

# -*- coding: utf-8 -*-
from bs4 import BeautifulSoup
import requests
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium import webdriver
import re
from konlpy.tag import Okt
import collections


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
                "model": "general",
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


chrome_options = webdriver.ChromeOptions()
chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])
# 크롬 창 숨기는 옵션 추가
# chrome_options.add_argument("headless")
dr = webdriver.Chrome(service=Service(
    ChromeDriverManager().install()), options=chrome_options)

c = 0
page = 1
zum_ns_url = []
while(c < 4):
    # url 생성
    url = "https://search.zum.com/search.zum?method=news&option=accu&query=" + \
        '코로나' + "&rd=1&cluster=&startdate=&enddate=&datetype=&scp=0&page=" + \
        str(page)

    # ConnectionError방지
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/100.0.48496.75"}

    # html불러오기
    original_html = requests.get(url, headers=headers)
    html = BeautifulSoup(original_html.text, "html.parser")

    # 검색결과
    articles = html.select("div.thumb > div.thumb_photo > a")

    # 뉴스기사 URL 가져오기

    for j in articles:
        if "news.zum.com" in j.attrs['href']:
            print(j.attrs['href'])
            zum_ns_url.append(j.attrs['href'])
            c += 1
            if c == 4:
                break
        # else:
        #     pass
    page += 1
dr.quit()
print(zum_ns_url)

# 뉴스 url을 이용해 뉴스 본문 전처리

ss = []
ss_cnt = 0

zumWordCnt = []
for i in zum_ns_url:
    contents = []
    # html불러오기
    o_html = requests.get(i, headers=headers)
    html = BeautifulSoup(o_html.text, "html.parser")

    # 검색결과
    artc = html.select("div#article_body")

    artc = ''.join(str(artc))

    # 뉴스기사 전처리
    pattern1 = '<[^>]*>'
    pattern2 = """[\n\n\n\n\n// flash 오류를 우회하기 위한 함수 추가\nfunction _flash_removeCallback() {}"""
    pattern3 = '[\t|\n]'

    artc = re.sub(pattern=pattern1, repl='', string=artc)
    artc = re.sub(pattern=pattern3, repl='', string=artc)
    artc = artc.replace(pattern2, '')
    # artc = artc.replace(" ", "/")

    contents.append(artc)
    # print(len(contents))
    WORDS = 1999
    summary = ""
    for ctt in contents:
        for i in range((len(ctt)//WORDS)+1):
            print(i, "번째***********")
            res = ClovaSummary().req(ctt[WORDS*i:WORDS*(i+1)])
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

    print("최종 summary")
    print(summary)
    print("-------------------------------")
    print("-------------------------------")


# ctt = contents[0]
# print(ctt)
print("--------------------------------------------------------------------------------------------------------------------")
# contents = "여기 네이버 api 요약으로 해서 그걸 사용하는 걸로 따져서 보자면은 쓰이는 게 한 두 가지가 있습니다.  언어로서만 따져서 보자면 한국어 일본어 이렇게 지원이 돼요. 그리고 모델은 두 가지가 있는데 하나는 제너럴이고 뉴스입니다 만약에 뉴스에서 이러한 요약하는 것을 한다면 굉장히 유용한 부분들이 있긴 하지만 저는 제너럴적인 부분 이런 것에 신경을 쓰고자 해서 제럴로 저는 선택을 했었었고요 그리고 여기서 한 가지 특별했던 부분들이 있는데 톤이라는 게 있습니다. 톤이라는 게 있냐면은 코드가 4가지가 구성이 되어 있는데 0으로 돼 있는 거는 요체예요.  요체 끝부분이 요로 끝나는 이걸로 돼 있는 거고 그다음에 1번이 구어체입니다.  말하는 2에 이런 식으로 문장을 바꿔서 요약해줘서 결론을 내주는 이런 게 있고 두 번째는 끝 문장이 요약을 해줬을 때 문체를 습니다로 말을 해주는 거고요 마지막 3은 음음체입니다.  음음체 뭐 해 뭐 그래라고 이러한 톤으로 해서 최종적으로 요약해서 보여준다는 거죠. 국의 게이밍 오디오 및 액세서리 브랜드 터틀비치(Turtle Beach)가 공중 및 우주 전투 시뮬레이션 게임용 컨트롤러 조이스틱인 '벨로시티원 플라이트스틱(VelocityOne Flightstick)을 공개한다고 밝혔다. 벨로시티원 플라이트스틱은 게이머에게 항공 및 우주 비행 시뮬레이션, 전투 게임에 몰입감 있고 정확하고 현실적인 조작감을 제공한다. 이 제품은 Xbox 시리즈 X/S, Xbox One 및 Windows PC용으로 설계되어 여러 플랫폼에서 범용성 있게 사용할 수 있다. 주 X&Y 버튼에 있는 비접촉 홀 효과 센서를 포함하여 총 8개의 축이 정밀한 제어를 가능하게 하며, 2개의 레버로 현실적, 미세한 작동을 제공한다. 또한, 벨로시티원 플라이트스틱의 정밀한 나노 트림 휠(Nano Trim Wheel)로 고도를 부드럽게 조정할 수 있다. 27개의 프로그래밍 가능한 버튼은 블루투스 연결을 통해 프로그램 소프트웨어로 편리하게 설정이 가능하며, 다양한 스틱 제어 기능을 제공한다. 벨로시티원 플라이트스틱은 11월 17일 출시 예정이며, 해당 제품에 대한 자세한 정보는 터틀비치 공식 홈페이지와 공식 SNS를 통해 확인 가능하다.  보자면은 쓰이는 게 한 두 가지가 있습니다.  보자면은 쓰이는 게 한 두 가지가 있습니다.  보자면은 쓰이는 게 한 두 가지가 있습니다. 이버 api 요약으로 해서 그걸 사용하는 걸로 따져서 보자면은 쓰이는 게 한 두 가지가 있습니다.  언어로서만 따져서 보자면 한국어 일본어 이렇게 지원이 돼요. 그리고 모델은 두 가지가 있는데 하나는 제너럴이고 뉴스입니다 만약에 뉴스에서 이러한 요약하는 것을 한다면 굉장히 유용한 부분들이 있긴 하지만 저는 제너럴적인 부분 이런 것에 신경을 쓰고자 해서 제럴로 저는 선택을 했었었고요 그리고 여기서 한 가지 특별했던 부분들이 있는데 톤이라는 게 있습니다. 톤이라는 게 있냐면은 코드가 4가지가 구성이 되어 있는데 0으로 돼 있는 거는 요체예요.  요체 끝부분이 요로 끝나는 이걸로 돼 있는 거고 그다음에 1번이 구어체입니다.  말하는 2에 이런 식으로 문장을 바꿔서 요약해줘서 결론을 내주는 이런 게 있고 두 번째는 끝 문장이 요약을 해줬을 때 문체를 습니다로 말을 해주는 거고요 마지막 3은 음음체입니다.  음음체 뭐 해 뭐 그래라고 이러한 톤으로 해서 최종적으로 요약해서 보여준다는 거죠. 국의 게이밍 오디오 및 액세서리 브랜드 터틀비치(Turtle Beach)가 공중 및 우주 전투 시뮬레이션 게임용 컨트롤러 조이스틱인 '벨로시티원 플라이트스틱(VelocityOne Flightstick)을 공개한다고 밝혔다. 벨로시티원 플라이트스틱은 게이머에게 항공 및 우주 비행 시뮬레이션, 전투 게임에 몰입감 있고 정확하고 현실적인 조작감을 제공한다. 이 제품은 Xbox 시리즈 X/S, Xbox One 및 Windows PC용으로 설계되어 여러 플랫폼에서 범용성 있게 사용할 수 있다. 주 X&Y 버튼에 있는 비접촉 홀 효과 센서를 포함하여 총 8개의 축이 정밀한 제어를 가능하게 하며, 2개의 레버로 현실적, 미세한 작동을 제공한다. 또한, 벨로시티원 플라이트스틱의 정밀한 나노 트림 휠(Nano Trim Wheel)로 고도를 부드럽게 조정할 수 있다. 27개의 프로그래밍 가능한 버튼은 블루투스 연결을 통해 프로그램 소프트웨어로 편리하게 설정이 가능하며, 다양한 스틱 제어 기능을 제공한다. 벨로시티원 플라이트스틱은 11월 17일 출시 예정이며, 해당 제품에 대한 자세한 정보는 터틀비치 공식 홈페이지와 공식 SNS를 통해 확인 가능하다."
# WORDS = 1999
# summary = ""
# for ctt in contents:
#     for i in range((len(ctt)//WORDS)+1):
#         print(i, "번째***********")
#         res = ClovaSummary().req(ctt[WORDS*i:WORDS*(i+1)])
#         rescode = res.status_code
#         if(rescode == 200):
#             # print(str(i) + res.text)
#             summary += json.loads(res.text)["summary"]
#             # summary += res.text["summary"]
#         else:
#             print("first Error : " + res.text)

#     if (len(ctt)//WORDS) > 0:
#         res = ClovaSummary().req(summary)
#         rescode = res.status_code
#         if(rescode == 200):
#             # print(res.text)
#             summary = json.loads(res.text)["summary"]
#         else:
#             print("Error : " + res.text)

# print("최종 summary")
# print(summary)
# print("-------------------------------")
# print("-------------------------------")

# 2000자 넘는 문장 2000자씩 나눠서 요약

# # 형태소 별로 분리해주는 라이브러리
# nlpy = Okt()

# nouns = nlpy.nouns(contents[0])  # 기사 본문을 명사로 분리

# # 한자수의 단어 배제
# for enum, notone in enumerate(nouns):
#     if len(notone) < 2:
#         nouns.pop(enum)

# # print(nouns)
# wcnt = collections.Counter(nouns)
# # ls = contents[0].split("/")
# # ls = ' '.join(ls).split()
# # print(ls)

# # word_count = collections.Counter(ls)

# cnt = 0
# num = 0  # 숫자를 db에 저장하기 위한 변수
# i = 1
# j = 0

# while(cnt != 2):
#     if wcnt.most_common(i)[j][0] == '코로나':
#         j += 1
#         i += 1
#         continue
#     else:
#         ss.append(wcnt.most_common(i)[j][0])
#         print('흔한 단어는?: ', ss[ss_cnt], "\n")
#         num = max(num, wcnt[ss[ss_cnt]])
#         cnt += 1
#         ss_cnt += 1
#         i += 1
#         j += 1
# zumWordCnt.append(num)

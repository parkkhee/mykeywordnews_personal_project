#!/usr/bin/python
# -*- coding: utf-8 -*-
import requests
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


if __name__ == '__main__':

    contents = str(sys.argv)  # argv는 리스트로 파이썬실행 파일 제목과 인스턴스 값이 같이 들어감

    # contents = "여기 네이버 api 요약으로 해서 그걸 사용하는 걸로 따져서 보자면은 쓰이는 게 한 두 가지가 있습니다.  언어로서만 따져서 보자면 한국어 일본어 이렇게 지원이 돼요. 그리고 모델은 두 가지가 있는데 하나는 제너럴이고 뉴스입니다 만약에 뉴스에서 이러한 요약하는 것을 한다면 굉장히 유용한 부분들이 있긴 하지만 저는 제너럴적인 부분 이런 것에 신경을 쓰고자 해서 제럴로 저는 선택을 했었었고요 그리고 여기서 한 가지 특별했던 부분들이 있는데 톤이라는 게 있습니다. 톤이라는 게 있냐면은 코드가 4가지가 구성이 되어 있는데 0으로 돼 있는 거는 요체예요.  요체 끝부분이 요로 끝나는 이걸로 돼 있는 거고 그다음에 1번이 구어체입니다.  말하는 2에 이런 식으로 문장을 바꿔서 요약해줘서 결론을 내주는 이런 게 있고 두 번째는 끝 문장이 요약을 해줬을 때 문체를 습니다로 말을 해주는 거고요 마지막 3은 음음체입니다.  음음체 뭐 해 뭐 그래라고 이러한 톤으로 해서 최종적으로 요약해서 보여준다는 거죠. 국의 게이밍 오디오 및 액세서리 브랜드 터틀비치(Turtle Beach)가 공중 및 우주 전투 시뮬레이션 게임용 컨트롤러 조이스틱인 '벨로시티원 플라이트스틱(VelocityOne Flightstick)을 공개한다고 밝혔다. 벨로시티원 플라이트스틱은 게이머에게 항공 및 우주 비행 시뮬레이션, 전투 게임에 몰입감 있고 정확하고 현실적인 조작감을 제공한다. 이 제품은 Xbox 시리즈 X/S, Xbox One 및 Windows PC용으로 설계되어 여러 플랫폼에서 범용성 있게 사용할 수 있다. 주 X&Y 버튼에 있는 비접촉 홀 효과 센서를 포함하여 총 8개의 축이 정밀한 제어를 가능하게 하며, 2개의 레버로 현실적, 미세한 작동을 제공한다. 또한, 벨로시티원 플라이트스틱의 정밀한 나노 트림 휠(Nano Trim Wheel)로 고도를 부드럽게 조정할 수 있다. 27개의 프로그래밍 가능한 버튼은 블루투스 연결을 통해 프로그램 소프트웨어로 편리하게 설정이 가능하며, 다양한 스틱 제어 기능을 제공한다. 벨로시티원 플라이트스틱은 11월 17일 출시 예정이며, 해당 제품에 대한 자세한 정보는 터틀비치 공식 홈페이지와 공식 SNS를 통해 확인 가능하다.  보자면은 쓰이는 게 한 두 가지가 있습니다.  보자면은 쓰이는 게 한 두 가지가 있습니다.  보자면은 쓰이는 게 한 두 가지가 있습니다. 이버 api 요약으로 해서 그걸 사용하는 걸로 따져서 보자면은 쓰이는 게 한 두 가지가 있습니다.  언어로서만 따져서 보자면 한국어 일본어 이렇게 지원이 돼요. 그리고 모델은 두 가지가 있는데 하나는 제너럴이고 뉴스입니다 만약에 뉴스에서 이러한 요약하는 것을 한다면 굉장히 유용한 부분들이 있긴 하지만 저는 제너럴적인 부분 이런 것에 신경을 쓰고자 해서 제럴로 저는 선택을 했었었고요 그리고 여기서 한 가지 특별했던 부분들이 있는데 톤이라는 게 있습니다. 톤이라는 게 있냐면은 코드가 4가지가 구성이 되어 있는데 0으로 돼 있는 거는 요체예요.  요체 끝부분이 요로 끝나는 이걸로 돼 있는 거고 그다음에 1번이 구어체입니다.  말하는 2에 이런 식으로 문장을 바꿔서 요약해줘서 결론을 내주는 이런 게 있고 두 번째는 끝 문장이 요약을 해줬을 때 문체를 습니다로 말을 해주는 거고요 마지막 3은 음음체입니다.  음음체 뭐 해 뭐 그래라고 이러한 톤으로 해서 최종적으로 요약해서 보여준다는 거죠. 국의 게이밍 오디오 및 액세서리 브랜드 터틀비치(Turtle Beach)가 공중 및 우주 전투 시뮬레이션 게임용 컨트롤러 조이스틱인 '벨로시티원 플라이트스틱(VelocityOne Flightstick)을 공개한다고 밝혔다. 벨로시티원 플라이트스틱은 게이머에게 항공 및 우주 비행 시뮬레이션, 전투 게임에 몰입감 있고 정확하고 현실적인 조작감을 제공한다. 이 제품은 Xbox 시리즈 X/S, Xbox One 및 Windows PC용으로 설계되어 여러 플랫폼에서 범용성 있게 사용할 수 있다. 주 X&Y 버튼에 있는 비접촉 홀 효과 센서를 포함하여 총 8개의 축이 정밀한 제어를 가능하게 하며, 2개의 레버로 현실적, 미세한 작동을 제공한다. 또한, 벨로시티원 플라이트스틱의 정밀한 나노 트림 휠(Nano Trim Wheel)로 고도를 부드럽게 조정할 수 있다. 27개의 프로그래밍 가능한 버튼은 블루투스 연결을 통해 프로그램 소프트웨어로 편리하게 설정이 가능하며, 다양한 스틱 제어 기능을 제공한다. 벨로시티원 플라이트스틱은 11월 17일 출시 예정이며, 해당 제품에 대한 자세한 정보는 터틀비치 공식 홈페이지와 공식 SNS를 통해 확인 가능하다."
    WORDS = 1999
    summary = ""

    for i in range((len(contents)//WORDS)+1):
        print(i, "번째***********")
        res = ClovaSummary().req(contents[WORDS*i:WORDS*(i+1)])
        rescode = res.status_code
        if(rescode == 200):
            print(str(i) + res.text)
            summary += json.loads(res.text)["summary"]
            # summary += res.text["summary"]
        else:
            print("first Error : " + res.text)

    if (len(contents)//WORDS) > 0:
        res = ClovaSummary().req(summary)
        rescode = res.status_code
        if(rescode == 200):
            print(res.text)
            summary = json.loads(res.text)["summary"]
        else:
            print("Error : " + res.text)

    print("최종 summary")
    print(summary)

    # 2000자 넘는 문장 2000자씩 나눠서 요약

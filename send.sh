#!/usr/bin/env bash

APP_ID="GussdTestBot"
APP_SECRET="d1fd78109450494c8fe4f21d15b5d125"

read -r -d '' DATA << EOF
{
  "type":"Message",
  "text":"foo bar",
  "channelData":{
    "attachment":{
      "type":"template",
      "payload":{
        "template_type":"generic",
        "elements": [
          {
            "title":"DPC Yellow Pages:\n
            Please reply with keyword or choose from category",
            "buttons":[
              {
                "type":"postback", "title":"Link 1", "payload":"Link 1"
              },
              {
                "type":"postback", "title":"Link 2", "payload":"Link 2"
              },
              {
                "type":"postback", "title":"Link 3", "payload":"Link 3"
              }
            ]
          },
          {
            "title":".",
            "buttons":[
              {
                "type":"postback", "title":"Link 4", "payload":"Link 4"
              },
              {
                "type":"postback", "title":"Link 5", "payload":"Link 5"
              },
              {
                "type":"postback", "title":"Link 6", "payload":"Link 6"
              }
            ]
          }
        ]
      }
    }
  },
  "from": {
    "channelId": "facebook",
    "address": "1145788065463011",
  },
  "to": {
    "channelId": "facebook",
    "address": "1131328913555485",
  }
}
EOF

curl \
  -v \
  -XPOST "https://api.botframework.com/bot/v1.0/messages" \
  --user ${APP_ID}:${APP_SECRET} \
  -H "Ocp-Apim-Subscription-Key: ${APP_SECRET}" \
  -H "Content-Type: application/json; charset=UTF-8" \
  -H "Accept: application/json; charset=UTF-8" \
  -d "$DATA"


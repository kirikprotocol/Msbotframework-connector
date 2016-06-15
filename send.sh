#!/usr/bin/env bash

APP_ID="GussdTestBot"
APP_SECRET="d1fd78109450494c8fe4f21d15b5d125"

read -r -d '' DATA << EOF
{
  "text": "Hello",
  "attachments": [
    {
      "actions": [
        {
          "title": "Okay",
          "message": "Okay"
        }
      ]
    }
  ],
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


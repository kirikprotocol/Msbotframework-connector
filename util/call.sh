#!/usr/bin/env bash

APP_ID="GussdTestBot"
APP_SECRET="d1fd78109450494c8fe4f21d15b5d125"

data='{"conversationId":"AniEP42wb6r3CxP2mT8kKEyA8oB2eF9AY8y3D1dFjtCgFHf","text":"curl test","from":{"name":"GussdTestBot","channelId":"webchat","address":"GussdTestBot","id":"GussdTestBot","isBot":true},"to":{"name":"G31Gf72XwxQ","channelId":"webchat","address":"PtsnGP9BaWg.G31Gf72XwxQ","id":"7Pmnn3HyI1F","isBot":false}}'

curl \
  -v \
  -XPOST "https://api.botframework.com/bot/v1.0/messages" \
  --user ${APP_ID}:${APP_SECRET} \
  -H "Ocp-Apim-Subscription-Key: ${APP_SECRET}" \
  -H "Content-Type: application/json; charset=UTF-8" \
  -H "Accept: application/json; charset=UTF-8" \
  -d "$data"

#  '{"type":"Message"}'
#  "https://api.botframework.com/bot/v1.0/messages" \


#  "https://api.botframework.com/bot/v1.0/bots/${APP_ID}/users/1" \
GussdTestBot
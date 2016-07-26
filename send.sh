#!/usr/bin/env bash

read -r -d '' DATA << EOF
{
  "type": "message",
  "timestamp": "2016-07-26T12:29:10.000096Z",
  "serviceUrl": "https://facebook.botframework.com",
  "channelId": "facebook",
  "from": {
    "id": "878405072264349"
  },
  "conversation": {
    "id": "1071173859631252-878405072264349"
  },
  "recipient": {
    "id": "1071173859631252"
  },
  "attachments": [
    {
      "contentType": "image",
      "contentUrl": "https://devel.globalussd.mobi/files/2049294797"
    }
  ],
  "replyToId": "LSBsgT5zyQo",
  "protocol": "FACEBOOK"
}
EOF

#{
#  "type": "Message",
#  "timestamp": "2016-07-25T16:27:52.176Z",
#  "from": {
#    "id": "28:d26fad71-a3fc-488d-afca-689481e7278c",
#    "name": "AgentMistyFerret"
#  },
#  "conversation": {
#    "id": "29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8"
#  },
#  "recipient": {
#    "id": "29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8",
#    "name": "Andy Belsky"
#  },
#  "text": "test"
#}

#CID="29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8"
CID="1071173859631252-878405072264349"

curl \
  -vvv \
  -XPOST "https://facebook.botframework.com/v3/conversations/$CID/activities" \
  -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSIsImtpZCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwiaWF0IjoxNDY5NTM0Mzc5LCJuYmYiOjE0Njk1MzQzNzksImV4cCI6MTQ2OTUzODI3OSwiYXBwaWQiOiJkMjZmYWQ3MS1hM2ZjLTQ4OGQtYWZjYS02ODk0ODFlNzI3OGMiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwidGlkIjoiNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3IiwidmVyIjoiMS4wIn0.KetKul2GAXnG5WWxAdvKzs7u-klKnmPElSfzUPuW7TfDI7bs8K_ES3DyIK5lobXMk1sCHb4xmJ5UhYZdF9iY-i_T5iYDqhXB-2I8xWRktE66u7UfFCBNjhA7NehQzIP9UnzcEDUglF2qk5UdHbi7VdvpXEj16UUyicvHSGIIYI9Zs-1PnxNudzW9XGmAJYpNcToJPZ0HGvKHIGi1cIoMmLsGCoOl1HC8s1IMReq8TVvrt8wnHD4Te_0WU_5sNhef6Gt1q_0SZhZ_k9e056qTNmTYUsCM7eVJA5vZ-hvgk58oJTnaGrr0QZXB4axscU0ZBY3J8IbI3XCIsqcHUJKdRg" \
  -H "Content-Type: application/json; charset=UTF-8" \
  -H "Accept: application/json; charset=UTF-8" \
  -d "$DATA"


#  -H "Transfer-Encoding: chunked" \

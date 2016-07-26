#!/usr/bin/env bash

read -r -d '' DATA << EOF
{
  "type": "message",
  "timestamp": "2016-07-26T06:08:55.112Z",
  "from": {
    "id": "28:d26fad71-a3fc-488d-afca-689481e7278c"
  },
  "conversation": {
    "id": "29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8"
  },
  "recipient": {
    "id": "29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8"
  },
  "channelId": "skype",
  "text": "Hello7",
  "serviceUrl": "https://skype.botframework.com"
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

CID="29:1MNVnHAfuPoOSUKMQH1-bAi_QLbs-59rgYf817TQUHQ8"

curl \
  -vvv \
  -XPOST "https://skype.botframework.com/v3/conversations/$CID/activities" \
  -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSIsImtpZCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwiaWF0IjoxNDY5NTEyNzczLCJuYmYiOjE0Njk1MTI3NzMsImV4cCI6MTQ2OTUxNjY3MywiYXBwaWQiOiJkMjZmYWQ3MS1hM2ZjLTQ4OGQtYWZjYS02ODk0ODFlNzI3OGMiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwidGlkIjoiNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3IiwidmVyIjoiMS4wIn0.Hvx1DyQDL1Uj0N0F18QDC2iSKyym43-gYfBt9usgyD6fxSiIaxAO5bxPxqbaH__2E1QkI5DybjqsXzL33VaGR3RXJN5MCzf2zSWXobkz_1j8ielV4FlMG3h20USeFS8B0KEx5UutxjEZ6VTAqKDLgjASncRC2HoM9_xlGp9LiLO4zQXVeyw4jrVz1Cr5YgFD4nfJ1gKDtFQsnSAkD_O9O8pwW1vtAYHQPI4nVsuHPUJhIltHx9vWzcJAydit_UKFBwIvjh5ArbBs9K7NVSu4HzKgZInjJ_tB_0t_SeGOQYLfkt00bUHK5N3M5uqarHrS8IMfRSeoewaPSpt_Repqnw" \
  -H "Content-Type: application/json; charset=UTF-8" \
  -H "Accept: application/json; charset=UTF-8" \
  -d "$DATA"


#  -H "Transfer-Encoding: chunked" \

#!/usr/bin/env bash

read -r -d '' DATA << EOF
{
  "type": "Message",

  "text": "test"
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
  -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSIsImtpZCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwiaWF0IjoxNDY5NDYzNDEzLCJuYmYiOjE0Njk0NjM0MTMsImV4cCI6MTQ2OTQ2NzMxMywiYXBwaWQiOiJkMjZmYWQ3MS1hM2ZjLTQ4OGQtYWZjYS02ODk0ODFlNzI3OGMiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC83MmY5ODhiZi04NmYxLTQxYWYtOTFhYi0yZDdjZDAxMWRiNDcvIiwidGlkIjoiNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3IiwidmVyIjoiMS4wIn0.AHabp7W0cVSQIdi7zWo7x1CaOVbvT2ktgtCtDL-PYmjBB48rsaJltZC0GYVUQzZ73nWaQ-a5MG-9moZJ-AuHsu62BGOzV72-yEjfn3frFOT8tZHCBO5CCTnVPIAEP-GDMRrXj02cQ-zgar75pygHmjqM1VYUeP0eRG-3L542QzgavepfyNYoWVptIRuDb5Nnlm1UEl51GnnV8-wp8lAo671kJH5u5KJdyGnPer1gDsn2WJ-eUVVTCpfHoE5lUsHDwBJ7MS1WCQMImACVAtQNrAoIBMeTsgFa3qKl9nxYC1ije9RzYQtNbKat8Xg2ii7BCmTEfdCD6yFt7Xz7We30_w" \
  -H "Content-Type: application/json; charset=UTF-8" \
  -H "Accept: application/json; charset=UTF-8" \
  -d "$DATA"


#  -H "Transfer-Encoding: chunked" \

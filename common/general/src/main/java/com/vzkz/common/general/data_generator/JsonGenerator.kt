package com.vzkz.common.general.data_generator

fun dummySingleMatchJson(): String =
    """
    {
      "exportedAt": 123,
      "items": [
        {
          "matchId": "dae17528-e4c3-4002-8a3b-f905a792bcf4",
          "dateTimeIso": "2025-12-16T15:10:33.099+01:00",
          "zoneId": "+01:00",
          "elapsedSeconds": 8,
          "avgHeartRate": 73,
          "maxHeartRate": 95,
          "sets": [
            {
              "setId": "4c0301f2-2e2c-415e-8bae-04279319e16b",
              "gameList": [
                {
                  "gameId": "308e3558-2730-4817-a35c-1b405f8082f4",
                  "player1Points": 0,
                  "player2Points": 5
                },
                {
                  "gameId": "40fe16bb-eef8-43db-9cc4-48d3cbc0c508",
                  "player1Points": 0,
                  "player2Points": 5
                },
                {
                  "gameId": "4e9b6dc6-9d61-47fb-8083-27c2458a24c2",
                  "player1Points": 0,
                  "player2Points": 5
                },
                {
                  "gameId": "387bf2bf-6ce5-4de1-aeb5-e417b312793a",
                  "player1Points": 0,
                  "player2Points": 5
                },
                {
                  "gameId": "9dfced9c-db9b-467a-8124-effd5740221e",
                  "player1Points": 0,
                  "player2Points": 5
                },
                {
                  "gameId": "ab26d19b-0cdf-4b8a-8448-ad263e275df3",
                  "player1Points": 0,
                  "player2Points": 5
                }
              ]
            }
          ]
        }
      ]
    }
    """.trimIndent()

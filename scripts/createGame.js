// Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
const AWS = require('aws-sdk')
const documentClient = new AWS.DynamoDB.DocumentClient()

const params = {
  TableName: 'turn-based-game',
  Item: {
    gameId: '5b5ee7d8',
    user1: 'myfirstuser',
    user2: 'theseconduser',
    A1: ' ',
    A2: ' ',
    A3: ' ',
    B1: ' ',
    B2: ' ',
    B3: ' ',
    C1: ' ',
    C2: ' ',
    C3: ' ',
    lastMoveBy: 'myfirstuser'
  }
}

documentClient.put(params).promise()
  .then(() => console.log('Game added successfully!'))
  .catch((error) => console.log('Error adding game', error))

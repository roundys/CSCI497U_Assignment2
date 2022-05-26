// Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
const AWS = require("aws-sdk");
const ses = new AWS.SES({region: "us-west-2"});

const sendMessage = async ({ email, message }) => {
  var myMessage = message
  var receiverEmail = email

  console.log(JSON.stringify(receiverEmail))
  console.log(JSON.stringify(myMessage))

  var params = {
    Destination: { /* required */
      ToAddresses: [receiverEmail],
    },
    Message: { /* required */
      Body: { /* required */
        Text: {Data : myMessage},
       },

       Subject: {Data: "Game Update!"},
      },
    Source: "myemail@example.com", /* required */
  };


  return ses.sendEmail(params).promise();
};

module.exports = sendMessage;

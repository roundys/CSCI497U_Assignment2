// Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
const AWS = require("aws-sdk");
const ses = new AWS.SES();

const sendMessage = async ({ email, message }) => {

  const params = {
    Destination: { /* required */
      ToAddresses: [
        email
      ]
    },
    Message: { /* required */
      Body: { /* required */
        Html: {
         Charset: "UTF-8",
         Data: "HTML_FORMAT_BODY"
        },
        Text: message,
       Subject: {
        Charset: 'UTF-8',
        Data: 'Tic-Tac-Toe'
       }
      },
    Source: 'roundys@wwu.edu', /* required */
  };


  return ses.sendEmail(params).promise();
};

module.exports = sendMessage;

// Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
const Joi = require("joi");

const extractError = error => {
  return error.details[0].message;
};

// Request body validation for the POST /users endpoint
const validateCreateUser = body => {
  const schema = Joi.object().keys({
    email: Joi.string().email({ minDomainAtoms: 2 }).required(),
    password: Joi.string().min(8).max(20).required(),
    username: Joi.string().min(4).max(20).required()
  });

  const result = Joi.validate(body, schema);
  if (result.error) {
    return {
      valid: false,
      message: extractError(result.error)
    };
  }
  return {
    valid: true
  };
};

// Request body validation for the POST /games endpoint
const validateCreateGame = body => {
  const schema = Joi.object().keys({
    opponent: Joi.string().min(4).max(20).required()
  });

  const result = Joi.validate(body, schema);
  if (result.error) {
    return {
      valid: false,
      message: extractError(result.error)
    };
  }
  return {
    valid: true
  };
};

// Request body validation for the POST /games/:gameIdendpoint
const validatePerformMove = body => {
  const schema = Joi.object().keys({
    changedSpace: Joi.string().valid('A1', 'A2', 'A3', 'B1', 'B2', 'B3', 'C1', 'C2', 'C3').required(),
    changedSpaceValue: Joi.string().min(1).max(1).required()
  });

  const result = Joi.validate(body, schema);
  if (result.error) {
    return {
      valid: false,
      message: extractError(result.error)
    };
  }
  return {
    valid: true
  };
};

module.exports = {
  validateCreateUser,
  validateCreateGame,
  validatePerformMove
};

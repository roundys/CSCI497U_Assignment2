// Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

const sendMessage = require('./sendMessage')

const handlePostMoveNotification = async ({ game, mover, opponent }) => {
  // Handle when game is finished
  if ((game.A1 == game.A2  && game.A2 == game.A3 && game.A2 != ' ') || (game.B1 == game.B2 && game.B2 == game.B3 && game.B2 != ' ') || (game.C1 == game.C2 && game.C2 == game.C3 && game.C2 != ' ') || (game.A1 == game.B1 && game.B1 && game.C1 && game.B1 != ' ') || (game.A2 == game.B2 && game.B2 == game.C2 && game.B2 != ' ') || (game.A3 == game.B3 && game.B3 == game.C3 && game.B3 != ' ') || (game.A1 == game.B2 && game.B2 == game.C3 && game.B2 != ' ') || (game.A3 == game.B2 && game.B2 == game.C1 && game.B2 != ' ')) {
    const winnerMessage = `You beat ${opponent.username} in a game of Tic-Tac-Toe!`
    const loserMessage = `Ahh, you lost to ${mover.username} in Tic-Tac-Toe.`
    await Promise.all([
      sendMessage({ email: `${mover.email}`, message: winnerMessage }),
      sendMessage({ email: `${opponent.email}`, message: loserMessage })
    ])

    return
  }
  if(game.A1 != ' ' && game.A2 != ' ' && game.A3 != ' ' && game.B1 != ' ' && game.B2 != ' ' && game.B3 != ' ' && game.C1 != ' ' && game.C2 != ' ' && game.C3 != ' '){
    const tiedMessage = `The game is tied. No one won!`
    await Promise.all([
      sendMessage({ email: `${opponent.email}`, message: tiedMessage}),
      sendMessage({email: `${mover.email}`, message: tiedMessage})
  }

  const message = `${mover.username} has moved. It's your turn next in Game ID ${game.gameId}!`
  await sendMessage({ email: `${opponent.email}`, message })
};

module.exports = handlePostMoveNotification;

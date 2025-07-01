package com.games

import com.games.Connect4Logic.BoardState
import com.games.Connect4Logic.GameState.None

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

fun playConnect4() {
    val gameBoard = Connect4Logic.GameBoard()
    println(displayGameGrid(gameBoard))

    val connect4Logic = Connect4Logic()

    while (connect4Logic.getGameOutput(gameBoard) == None) {
        println("Make a Move ${connect4Logic.currentPlayer}")
        val input = readln().trim().toInt() // handle empty input

        connect4Logic.makeAMove(gameBoard, input)
        println(displayGameGrid(gameBoard))

        if (connect4Logic.getGameOutput(gameBoard) != None) {
            println("Game over: ${connect4Logic.getGameOutput(gameBoard)}")
            break
        }
    }
}

fun displayGameGrid(gameBoard: Connect4Logic.GameBoard): String {
    var board = ""
    var cellValue: String

    for (i in 0 until gameBoard.cells.size) {
        for (j in 0 until gameBoard.cells[i].size) {
            val position = if (i * 8 + j < 10) "0${i * 8 + j}" else i * 8 + j
            cellValue = if (gameBoard.cells[i][j] == BoardState.Player1Piece) {
                " 🟢 "
            } else if (gameBoard.cells[i][j] == BoardState.Player2Piece) {
                " 🔴 "
            } else {
                " $position "
            }

            board += "$cellValue|"

            if ((j % 8 + 1) == 8) {
                board += "\n\n"
            }
        }
    }
    return board
}

fun main() {
    playConnect4()
}
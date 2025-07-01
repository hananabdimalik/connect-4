package com.games

import com.games.Connect4Logic.*

interface Connect4Interface {
    fun updateBoard(position: Coordinates, board: GameBoard)
    fun alternatePlayers()
    fun mapPlayerInputToCoordinate(position: Int): Coordinates
    fun listOfCellsBasedOnPosition(position: Coordinates, board: GameBoard): List<Coordinates>
    fun findListOfAvailableCells(cells: List<Coordinates>, gameBoard: GameBoard): List<Coordinates>
    fun findListOfAvailableCellsWithinBounds(cells: List<Coordinates>): List<Coordinates>
    fun lowestAvailableCellInBoard(cells: List<Coordinates>): Coordinates
    fun getGameOutput(board: GameBoard): GameState
}

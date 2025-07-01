package com.games

import com.games.Connect4Logic.*

interface Connect4Interface {
    fun updateBoard(position: PlayerCoordinates, board: GameBoard)
    fun alternatePlayers()
    fun mapPositionToCoordinate(position: Int): PlayerCoordinates
    fun listOfCellsBasedOnPosition(position: PlayerCoordinates, board: GameBoard): List<PlayerCoordinates>
    fun findListOfAvailableCells(cells: List<PlayerCoordinates>, gameBoard: GameBoard): List<PlayerCoordinates>
    fun findListOfAvailableCellsWithinBounds(cells: List<PlayerCoordinates>): List<PlayerCoordinates>
    fun lowestAvailableCellInBoard(cells: List<PlayerCoordinates>): PlayerCoordinates
    fun getGameOutput(board: GameBoard): GameState
}

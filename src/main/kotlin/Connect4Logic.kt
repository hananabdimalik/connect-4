package com.games

class Connect4Logic : Connect4Interface {

    enum class BoardState {
        Empty,
        Player1Piece,
        Player2Piece,
    }

    enum class Players {
        Player1,
        Player2,
    }

    data class GameBoard(
        val cells: MutableList<MutableList<BoardState>> = MutableList(8) {
            MutableList(8) { BoardState.Empty }
        }
    )

    data class PlayerCoordinates(val x: Int, val y: Int)

    enum class GameState {
        Player1Wins,
        Player2Wins,
        Draw,
        None
    }

    var currentPlayer = Players.Player1

    fun makeAMove(board: GameBoard, selectedCell: Int) {
        val mapSelectedCellToCoordinate = mapPositionToCoordinate(selectedCell)
        val findListOfCellsBasedOnSelection = listOfCellsBasedOnPosition(mapSelectedCellToCoordinate, board)
        val findAvailableCells = findListOfAvailableCells(findListOfCellsBasedOnSelection, board)
        val findAvailableCellsWithBound = findListOfAvailableCellsWithinBounds(findAvailableCells)
        val findTheSmallestAvailableCell =
            if (findAvailableCellsWithBound.isNotEmpty()) lowestAvailableCellInBoard(findAvailableCellsWithBound) else return // change the name of this function

        if (board.cells[findTheSmallestAvailableCell.x][findTheSmallestAvailableCell.y] == BoardState.Empty) {
            updateBoard(findTheSmallestAvailableCell, board)
            alternatePlayers()
        }

        if (getGameOutput(board) != GameState.None) {
            return
        }
    }

    override fun updateBoard(position: PlayerCoordinates, board: GameBoard) {
        if (currentPlayer == Players.Player1) {
            board.cells[position.x][position.y] = BoardState.Player1Piece
        }

        if (currentPlayer == Players.Player2) {
            board.cells[position.x][position.y] = BoardState.Player2Piece
        }
    }

    override fun alternatePlayers() {
        if (currentPlayer == Players.Player1) {
            currentPlayer = Players.Player2
        } else if (currentPlayer == Players.Player2) {
            currentPlayer = Players.Player1
        }
    }

    override fun mapPositionToCoordinate(position: Int) = PlayerCoordinates(position / 8, position % 8)

    // given a number -> give me list of numbers in a col
    override fun listOfCellsBasedOnPosition(position: PlayerCoordinates, board: GameBoard): List<PlayerCoordinates> {
        val cells = mutableListOf<PlayerCoordinates>()
        for (i in 0 until board.cells.size) {
            for (j in 0 until board.cells[i].size) {
                if (j == position.y) {
                    cells.add(PlayerCoordinates(i, j))
                }
            }
        }
        return cells
    }

    override fun findListOfAvailableCells(cells: List<PlayerCoordinates>, gameBoard: GameBoard) =
        cells.filter { cell -> gameBoard.cells[cell.x][cell.y] == BoardState.Empty }

    override fun findListOfAvailableCellsWithinBounds(cells: List<PlayerCoordinates>) = cells.filter { cell ->
        cell.x in 0 until 8 && cell.y in 0 until 8
    }

    override fun lowestAvailableCellInBoard(cells: List<PlayerCoordinates>): PlayerCoordinates {
        var largestXValue = 0
        cells.forEach { cell ->
            if (cell.x > largestXValue) {
                largestXValue = cell.x
            }
        }
        return PlayerCoordinates(largestXValue, cells[0].y)
    }

    // how to find game output
    private fun hasConsecutiveNumbers(input: List<Int>, diff: Int): Boolean {
        var count = 0
        input.forEachIndexed { index, i ->
            if (index + 1 <= input.lastIndex) {
                if (input[index + 1] - input[index] == diff) {
                    count++
                }
            }
        }
        return count == 3
    }

    override fun getGameOutput(board: GameBoard): GameState {
        val player1Selection = mutableListOf<Int>()
        val player2Selection = mutableListOf<Int>()

        board.cells.flatMap { it.toList() }.forEachIndexed { index, state ->
            if (state == BoardState.Player1Piece) {
                player1Selection.add(index)
            } else if (state == BoardState.Player2Piece) {
                player2Selection.add(index)
            }
        }

        if (hasConnected(player1Selection)) {
            return GameState.Player1Wins
        } else if (hasConnected(player2Selection)) {
            return GameState.Player2Wins
        } else if (player1Selection.size + player2Selection.size == board.cells.flatMap { it.toList() }.size) {
            return GameState.Draw
        }
        return GameState.None
    }

    fun hasConnectedHorizontally(selection: List<Int>): Boolean { // if the diff is 1
        selection.sorted().windowed(4, 1).forEach {
            if (hasConsecutiveNumbers(it, 1)) {
                return true
            }
        }
        return false
    }

    fun hasConnectedVertically(selection: List<Int>): Boolean { // if the diff is 8
        return hasConnectedBySetValue(selection, 8)
    }

    fun hasConnectedDiagonally(selection: List<Int>): Boolean {
        return hasConnectedBySetValue(selection, 7) || hasConnectedBySetValue(selection, 9)
    }

    fun hasConnectedBySetValue(selection: List<Int>, value: Int): Boolean {
        selection.forEachIndexed { index, _ ->
            if (selection.contains(selection[index])
                && selection.contains(selection[index] + value)
                && selection.contains(selection[index] + value * 2)
                && selection.contains(selection[index] + value * 3)
            ) {
                return true
            }
        }
        return false
    }

    fun hasConnected(selection: List<Int>) =
        hasConnectedDiagonally(selection) || hasConnectedVertically(selection) || hasConnectedHorizontally(selection)
}

// connected 4

//State
// board -> size (8 * 8) and state
// players
// game state

// Rules
// First player that has 4 consecutive pieces (horizontal, vertical and diagonal) wins
// how player takes a position -> first available index in a col, within bound

// formula for consecutive numbers in horizontal and vertical
// formula for diagonal numbers

// this is for a 8 * 8 symmetrical grid
// how to play
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

    data class Coordinates(val x: Int, val y: Int)

    enum class GameState {
        Player1Wins,
        Player2Wins,
        Draw,
        None
    }

    var currentPlayer = Players.Player1

    fun makeAMove(board: GameBoard, selectedCell: Int) {
        val mapSelectedCellToCoordinate = mapPlayerInputToCoordinate(selectedCell)
        val findListOfCellsBasedOnSelection = listOfCellsBasedOnPosition(mapSelectedCellToCoordinate, board)
        val findAvailableCells = findListOfAvailableCells(findListOfCellsBasedOnSelection, board)
        val findAvailableCellsWithBound = findListOfAvailableCellsWithinBounds(findAvailableCells)
        val findTheLowestAvailableCellInBoard =
            if (findAvailableCellsWithBound.isNotEmpty()) lowestAvailableCellInBoard(findAvailableCellsWithBound) else return

        if (board.cells[findTheLowestAvailableCellInBoard.x][findTheLowestAvailableCellInBoard.y] == BoardState.Empty) {
            updateBoard(findTheLowestAvailableCellInBoard, board)
            alternatePlayers()
        }

        if (getGameOutput(board) != GameState.None) {
            return
        }
    }

    override fun updateBoard(position: Coordinates, board: GameBoard) {
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

    override fun mapPlayerInputToCoordinate(position: Int) = Coordinates(position / 8, position % 8)

    // given a player move -> give list of coordinates in a col
    override fun listOfCellsBasedOnPosition(position: Coordinates, board: GameBoard): List<Coordinates> {
        val cells = mutableListOf<Coordinates>()
        for (x in 0 until board.cells.size) {
            for (y in 0 until board.cells[x].size) {
                if (y == position.y) {
                    cells.add(Coordinates(x = x, y = y))
                }
            }
        }
        return cells
    }

    override fun findListOfAvailableCells(cells: List<Coordinates>, gameBoard: GameBoard) =
        cells.filter { cell -> gameBoard.cells[cell.x][cell.y] == BoardState.Empty }

    override fun findListOfAvailableCellsWithinBounds(cells: List<Coordinates>) = cells.filter { cell ->
        cell.x in 0 until 8 && cell.y in 0 until 8
    }

    override fun lowestAvailableCellInBoard(cells: List<Coordinates>): Coordinates {
        var xValue = 0
        cells.forEach { cell ->
            if (cell.x > xValue) {
                xValue = cell.x
            }
        }
        return Coordinates(xValue, cells[0].y)
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

    private fun hasConnectedHorizontally(selection: List<Int>) =
        hasConnectedBySetValue(selection, 1) // if the diff is 1

    private fun hasConnectedVertically(selection: List<Int>) = hasConnectedBySetValue(selection, 8) // if the diff is 8

    private fun hasConnectedDiagonally(selection: List<Int>) =
        hasConnectedBySetValue(selection, 7) || hasConnectedBySetValue(selection, 9) // if the diff is 7 || 9

    private fun hasConnectedBySetValue(selection: List<Int>, value: Int): Boolean {
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

// Rules - how to play
// First player that has 4 consecutive pieces (horizontal, vertical and diagonal) wins
// how player takes a position -> first available index in a col, within bound

// formula for consecutive numbers in horizontal and vertical
// formula for diagonal numbers

// this is for a 8 * 8 symmetrical grid

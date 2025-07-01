import com.games.Connect4Logic
import com.games.Connect4Logic.BoardState.Player1Piece
import com.games.Connect4Logic.GameState.Player1Wins
import com.games.Connect4Logic.GameState.Player2Wins
import com.games.Connect4Logic.PlayerCoordinates
import com.games.Connect4Logic.Players.Player2
import kotlin.test.Test
import kotlin.test.assertEquals

class Connect4LogicTest {
    val connect4Logic = Connect4Logic()

    @Test
    fun `updateBoard, given currentPlayer is Player1, when player 1 makes a move to cell 10, return updated board with cell 10 set to player 1`() {
        val board = Connect4Logic.GameBoard()
        val positionAsCoordinates = connect4Logic.mapPositionToCoordinate(10)
        connect4Logic.updateBoard(positionAsCoordinates, board)

        assertEquals(
            Player1Piece,
            board.cells[positionAsCoordinates.x][positionAsCoordinates.y]
        )
    }

    @Test
    fun `alternatePlayers, given current player is Player 1, when alternatePlayer is called, currentPlayer is updated to player2`() {
        connect4Logic.alternatePlayers()
        assertEquals(Player2, connect4Logic.currentPlayer)
    }

    @Test
    fun `mapPositionToCoordinate, given cell position 20, return PlayerCoordinate`() {
        assertEquals(PlayerCoordinates(2, 4), connect4Logic.mapPositionToCoordinate(20))
    }

    @Test
    fun `listOfCellsBasedOnPosition, given player next move position, return list of cells based on position`() {
        val board = Connect4Logic.GameBoard()
        val actual = connect4Logic.listOfCellsBasedOnPosition(PlayerCoordinates(2, 4), board)
        assertEquals(
            listOf(
                PlayerCoordinates(x = 0, y = 4),
                PlayerCoordinates(x = 1, y = 4),
                PlayerCoordinates(x = 2, y = 4),
                PlayerCoordinates(x = 3, y = 4),
                PlayerCoordinates(x = 4, y = 4),
                PlayerCoordinates(x = 5, y = 4),
                PlayerCoordinates(x = 6, y = 4),
                PlayerCoordinates(x = 7, y = 4)
            ), actual
        )
    }

    @Test
    fun `findListOfAvailableCells, find list of cells that are available`() {
        val cells = listOf(
            PlayerCoordinates(x = 0, y = 4),
            PlayerCoordinates(x = 1, y = 4),
            PlayerCoordinates(x = 2, y = 4),
            PlayerCoordinates(x = 3, y = 4),
            PlayerCoordinates(x = 4, y = 4),
            PlayerCoordinates(x = 5, y = 4),
            PlayerCoordinates(x = 6, y = 4),
            PlayerCoordinates(x = 7, y = 4)
        )
        val board = Connect4Logic.GameBoard()
        connect4Logic.updateBoard(PlayerCoordinates(x = 1, y = 4), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 2, y = 4), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 5, y = 4), board)

        val actual = connect4Logic.findListOfAvailableCells(cells, board)
        val expected = listOf(
            PlayerCoordinates(x = 0, y = 4),
            PlayerCoordinates(x = 3, y = 4),
            PlayerCoordinates(x = 4, y = 4),
            PlayerCoordinates(x = 6, y = 4),
            PlayerCoordinates(x = 7, y = 4)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `findListOfAvailableCellsWithinBounds, find list of cells that are available`() {
        val cells = listOf(
            PlayerCoordinates(x = 0, y = 4),
            PlayerCoordinates(x = 1, y = 4),
            PlayerCoordinates(x = 2, y = 4),
            PlayerCoordinates(x = 3, y = 4),
            PlayerCoordinates(x = 4, y = 4),
            PlayerCoordinates(x = 5, y = 4),
            PlayerCoordinates(x = 6, y = 4),
            PlayerCoordinates(x = 7, y = 4),
            PlayerCoordinates(x = 7, y = 8),
            PlayerCoordinates(x = 8, y = 4),
        )

        val actual = connect4Logic.findListOfAvailableCellsWithinBounds(cells)

        assertEquals(
            listOf(
                PlayerCoordinates(x = 0, y = 4),
                PlayerCoordinates(x = 1, y = 4),
                PlayerCoordinates(x = 2, y = 4),
                PlayerCoordinates(x = 3, y = 4),
                PlayerCoordinates(x = 4, y = 4),
                PlayerCoordinates(x = 5, y = 4),
                PlayerCoordinates(x = 6, y = 4),
                PlayerCoordinates(x = 7, y = 4),
            ), actual
        )
    }

    @Test
    fun `lowestAvailableCellInBoard, find lowest available cell on the board`() {
        val cells = listOf(
            PlayerCoordinates(x = 0, y = 4),
            PlayerCoordinates(x = 1, y = 4),
            PlayerCoordinates(x = 2, y = 4),
            PlayerCoordinates(x = 3, y = 4),
            PlayerCoordinates(x = 4, y = 4),
            PlayerCoordinates(x = 5, y = 4),
            PlayerCoordinates(x = 6, y = 4),
            PlayerCoordinates(x = 7, y = 4),
        )

        val actual = connect4Logic.lowestAvailableCellInBoard(cells)
        assertEquals(PlayerCoordinates(x = 7, y = 4), actual)
    }

    @Test
    fun `getGameOutput, given player 1 has 4 connected pieces, return Player1Wins`() {
        val board = Connect4Logic.GameBoard()
        connect4Logic.updateBoard(PlayerCoordinates(x = 7, y = 4), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 6, y = 4), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 5, y = 4), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 4, y = 4), board)
        val actual = connect4Logic.getGameOutput(board)

        assertEquals(Player1Wins, actual)
    }

    @Test
    fun `getGameOutput, given player 2 has 4 connected pieces, return Player2Wins`() {
        val board = Connect4Logic.GameBoard()
        connect4Logic.alternatePlayers() // current player is updated to Player2
        connect4Logic.updateBoard(PlayerCoordinates(x = 7, y = 5), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 6, y = 5), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 5, y = 5), board)
        connect4Logic.updateBoard(PlayerCoordinates(x = 4, y = 5), board)

        val actual = connect4Logic.getGameOutput(board)

        assertEquals(Player2Wins, actual)
    }

    @Test
    fun `given 4 numbers are diagonally connect, return true`() {
        assertEquals(true, connect4Logic.hasConnectedDiagonally(listOf(37, 44, 49, 51, 53, 58)))
    }

    @Test
    fun `given 5 numbers are diagonally connect, return true`() {
        assertEquals(true, connect4Logic.hasConnectedDiagonally(listOf(35, 36, 43, 50, 51, 57)))
    }

    @Test
    fun `given 5 numbers that  are diagonally connect, return true`() {
        assertEquals(true, connect4Logic.hasConnectedDiagonally(listOf(36, 43, 50, 52, 57)))
    }

    @Test
    fun `hasConnectedDiagonally, given 4 numbers connected, return true`() {
        assertEquals(true, connect4Logic.hasConnectedDiagonally(listOf(57, 59, 50, 52, 54, 45, 38)))
    }

    @Test
    fun `hasConnectedDiagonally, given a list of input that not connected, return false`() {
        assertEquals(false, connect4Logic.hasConnectedDiagonally(listOf(42, 43, 49, 50, 56, 60)))
    }


    @Test
    fun `given list of numbers that arent diagonally connect, return false`() {
        assertEquals(false, connect4Logic.hasConnectedDiagonally(listOf(41, 42, 50, 51, 60)))
    }

    @Test
    fun `given lists of numbers that arent diagonally connect, return false`() {
        assertEquals(false, connect4Logic.hasConnectedDiagonally(listOf(41, 49, 50, 59)))
    }

    @Test
    fun `hasConnectedHorizontally, given a list of input that is connected horizontally, return true`() {
        assertEquals(true, connect4Logic.hasConnectedHorizontally(listOf(50, 57, 58, 59, 60)))
    }

    @Test
    fun `hasConnectedDiagonally, given a list of input that is not connected diagonally, return false`() {
        assertEquals(false, connect4Logic.hasConnectedDiagonally(listOf(33, 42, 43, 49, 51, 52, 61)))
    }

    @Test
    fun `hasConnectedVertically, given a list of input that is connected vertically, return true`() {
        assertEquals(false, connect4Logic.hasConnectedDiagonally(listOf(31, 39, 47, 55, 61)))
    }
}
package connectfour

const val END_GAME = "end"
const val BOARD_DIMENSION = "Set the board dimensions (Rows x Columns)\n" +
        "Press Enter for default (6 x 7)"
const val MULTIPLE_GAME = "Do you want to play single or multiple games?\n" +
        "For a single game, input 1 or press Enter\n" +
        "Input a number of games:"

class Game(){
    private var row:Int = 0
    private var col:Int = 0
    private var turns = 0
    private var turnsPlayed = mutableListOf<MutableList<String>>()
    private var fPlayer:String
    private var sPlayer:String
    private var fPlayerPoints = 0
    private var sPlayerPoints = 0
    private var numberOfGames:Int = 0
    private var countGame = 1

    init{
        var auxInput:String //verify the quality of input text
        println("Connect Four")
        println("First player's name:")
        fPlayer = readln()
        println("Second player's name:")
        sPlayer = readln()
        //dimensions
        while(true) {
            println(BOARD_DIMENSION)
            val dimension = readln().replace(" ", "").lowercase()
            if (dimension.isEmpty()) {
                row = 6
                col = 7
                break
            }
            if (!dimension.contains("x") ) {
                println("Invalid input")
                continue
            }
            val (strRow, strCol) = dimension.split("x")
            if(strRow.isEmpty() || strCol.isEmpty() || strRow.trim().toIntOrNull() == null || strCol.trim().toIntOrNull() == null){
                println("Invalid input")
                continue
            }
            row = strRow.trim().toIntOrNull() ?: 6
            col = strCol.trim().toIntOrNull() ?: 7
            if (row > 9 || row < 5) {
                println("Board rows should be from 5 to 9")
                continue
            }
            if (col > 9 || col < 5) {
                println("Board columns should be from 5 to 9")
                continue
            }
            break
        }
        //how many games
        while(true){
            println(MULTIPLE_GAME)
            auxInput = readln()
            if(auxInput.isEmpty()){
                numberOfGames = 1
                break
            }
            numberOfGames = auxInput.toIntOrNull() ?: -1
            if(numberOfGames > 0){
                break
            }
            println("Invalid input")
        }
        println("$fPlayer VS $sPlayer")
        println("$row X $col board")
        if(numberOfGames > 1) {
            println("Total $numberOfGames games")
        }
    }
    fun printBoard(){
        turns = 0
        turnsPlayed.clear()
        for(i in 1..col) {
            turnsPlayed.add(mutableListOf())
        }
        if(numberOfGames > 1) {
            println("Game #$countGame")
        }else{
            println("Single game")
        }
        for(c in 1..col){
            print(" $c")
        }
        println()
        for(r in 1..row) {
            for (c in 1..col) {
                print("║ ")
            }
            println("║")
        }
        print("╚═")
        for(c in 2..col) {
            print("╩═")
        }
        println("╝")
    }

    fun play():Boolean{
        var playingNow =
            if((countGame+1) % 2 == 0) {
                 fPlayer
            }else{
                sPlayer
            }
        while(turns < row*col) {
            println("$playingNow's turn:")
            val played = readln()
            if (played == END_GAME) {
                return false
            }
            if (played.toIntOrNull() == null) {
                println("Incorrect column number")
                continue
            }else if (played.toInt() < 1 || played.toInt() > col){
                println("The column number is out of range (1 - $col)")
                continue
            }else if(turnsPlayed[played.toInt()-1].size >= row){
                println("Column ${played.toInt()} is full")
                continue
            }
            if (playingNow == fPlayer) {
                turnsPlayed[played.toInt()-1].add("o")
                playingNow = sPlayer
            } else {
                playingNow = fPlayer
                turnsPlayed[played.toInt()-1].add("*")
            }
            //desenha tabuleiro

            for(c in 1..col){
                print(" $c")
            }
            println()
            for(r in 1..row) {
                for (c in 1..col) {
                    print("║")
                    if(turnsPlayed[c-1].size >= row-r+1){
                        print(turnsPlayed[c-1][row-r])
                    }else{
                        print(" ")
                    }
                }
                println("║")
            }
            print("╚═")
            for(c in 2..col) {
                print("╩═")
            }
            println("╝")
            //check winning condition can use recursion
            var auxi:Int
            var auxj:Int
            var element: String
            var counting:Int
            for(i in turnsPlayed.indices) {
                for(j in turnsPlayed[i].indices) {
                    element = turnsPlayed[i][j]
                    auxi = i
                    counting = 1
                    //down
                    while (turnsPlayed.size > auxi + 1 && turnsPlayed[auxi + 1].size > j && turnsPlayed[auxi + 1][j] == element) {
                        counting++
                        auxi++
                    }
                    if (counting >= 4) {
                        countGame++
                        if (element == "o") {
                            println("Player $fPlayer won")
                            fPlayerPoints += 2
                        } else {
                            println("Player $sPlayer won")
                            sPlayerPoints += 2
                        }
                        if(numberOfGames > 1) {
                            println("Score\n" +
                                    "$fPlayer: $fPlayerPoints $sPlayer: $sPlayerPoints")
                        }
                        //println("Game Over!")
                        return countGame-1 != numberOfGames
                    }
                    counting = 1
                    auxj = j
                    //right
                    while (turnsPlayed.size > i && turnsPlayed[i].size > auxj + 1 && turnsPlayed[i][auxj+1] == element) {
                        counting++
                        auxj++
                    }
                    if (counting >= 4) {
                        countGame++
                        if (element == "o") {
                            println("Player $fPlayer won")
                            fPlayerPoints += 2
                        } else {
                            println("Player $sPlayer won")
                            sPlayerPoints += 2
                        }
                        if(numberOfGames > 1) {
                            println("Score\n" +
                                    "$fPlayer: $fPlayerPoints $sPlayer: $sPlayerPoints")
                        }
                        //println("Game Over!")
                        return countGame-1 != numberOfGames
                    }
                    //diagonal: down/right
                    auxi = i
                    auxj = j
                    counting = 1
                    while (turnsPlayed.size > auxi + 1 && turnsPlayed[auxi+1].size > auxj + 1 && turnsPlayed[auxi+1][auxj+1] == element) {
                        counting++
                        auxj++
                        auxi++
                    }
                    if (counting >= 4) {
                        countGame++
                        if (element == "o") {
                            println("Player $fPlayer won")
                            fPlayerPoints += 2
                        } else {
                            println("Player $sPlayer won")
                            sPlayerPoints += 2
                        }
                        if(numberOfGames > 1) {
                            println("Score\n" +
                                    "$fPlayer: $fPlayerPoints $sPlayer: $sPlayerPoints")
                        }
                        //println("Game Over!")
                        return countGame-1 != numberOfGames
                    }
                    //diagonal: down/left
                    auxi = i
                    auxj = j
                    counting = 1
                    while (turnsPlayed.size > auxi + 1 && turnsPlayed[auxi+1].size > auxj - 1 && auxj - 1 >= 0 && turnsPlayed[auxi+1][auxj-1] == element) {
                        counting++
                        auxj--
                        auxi++
                    }
                    if (counting >= 4) {
                        countGame++
                        if (element == "o") {
                            println("Player $fPlayer won")
                            fPlayerPoints += 2
                        } else {
                            println("Player $sPlayer won")
                            sPlayerPoints += 2
                        }
                        if(numberOfGames > 1) {
                            println("Score\n" +
                                    "$fPlayer: $fPlayerPoints $sPlayer: $sPlayerPoints")
                        }
                        //println("Game Over!")
                        return countGame-1 != numberOfGames
                    }
                }
            }
            turns++
        }
        countGame++
        println("It is a draw")
        fPlayerPoints++
        sPlayerPoints++
        println("Score\n" +
                "$fPlayer: $fPlayerPoints $sPlayer: $sPlayerPoints")
        return countGame-1 != numberOfGames
    }
}

fun main() {
    val game = Game()
    do{
        game.printBoard()
    }while(game.play())
    println("Game Over!")
}
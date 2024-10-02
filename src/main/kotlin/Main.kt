package org.example

import java.io.File
import java.io.IOException

fun main() {
    // Get login and number of rounds
    var login = getLogin()
    val numberOfRounds = getNumberOfRounds()

    // Read number of points from the file
    var points = readPoints("points.txt", login)?.second ?: 0
    println("Welcome $login! Your score is $points.")

    // Start game
    for (currentRound in 1..numberOfRounds) {
        println("\nRound $currentRound")
        println("Your points: $points")

        // Play game and update points
        val roundResults = game()
        points += roundResults

        println("After this round you've got: $roundResults")
    }

    // Update the file with the new points for the user
    updatePoints("points.txt", login, points)

    // Display user's results
    println("\nScoreboard:")
    displayScoreboard("points.txt")
}

// Function to get user's login
fun getLogin(): String {
    println("Enter your login: ")
    return readLine()?:"Player"
}

// Function to get number of rounds
fun getNumberOfRounds(): Int {
    println("Enter number of rounds: ")
    return readLine()?.toIntOrNull() ?:3;
}

// Function to read number od points from the file according to login
fun readPoints(fileName: String, login: String): Pair<String, Int>? {
    try {
        val file = File(fileName)
        if (!file.exists()) {
            file.createNewFile()
            return null
        }
        val lines = file.readLines()

        for (line in lines) {
            if (line.isNotEmpty()) {
                val data = line.trim().split(",")
                if (data.size >= 2) {
                    val name = data[0]
                    val score = data[1].toIntOrNull()?:0

                    if (name == login) {
                        return Pair(login, score)
                    }
                }
            }
        }
        return null
    } catch (e: IOException) {
        println("error: ${e.message}")
        return null
    }
}

// Function that updates points
fun updatePoints(fileName: String, login: String, newPoints: Int) {
    try {
        val file = File(fileName)
        val lines = file.readLines().toMutableList()

        var found = false
        for (i in lines.indices) {
            val data = lines[i].split(",")
            if (data.size >= 2 && data[0] == login) {
                lines[i] = "$login,$newPoints"
                found = true
                break
            }
        }

        if (!found) {
            lines.add("$login,$newPoints")
        }

        file.writeText(lines.joinToString("\n"))

    } catch (e: IOException) {
        println ("error: ${e.message}")
    }
}

// Function that runs game
fun game(): Int {
    // Get user's move
    println("Enter 1 for ROCK, 2 for PAPER and 3 for SCISSORS")
    val userMove = readLine()?.toIntOrNull() ?:3

    // Get computer's move
    val computerMove = getComputerMove()

    // Print both moves
    println("Your move: ${moveToString(userMove)}")
    println("Computer's move: ${moveToString(computerMove)}")

    // Determine results
    return determineResults(userMove, computerMove)
}

// Get computer's move
fun getComputerMove(): Int {
    val move = (1..3).random()
    return move
}

// Function to convert move integer to string for display
fun moveToString(move: Int): String {
    return when (move) {
        1 -> "ROCK"
        2 -> "PAPER"
        3 -> "SCISSORS"
        else -> "INVALID"
    }
}

// Function to determine result of the round
fun determineResults(userMove: Int, computerMove: Int): Int {
    if (userMove == computerMove) {
        println("It's draw!")
        return 0 // draw
    }
    else if ((userMove == 1 && computerMove == 3) || (userMove == 2 && computerMove == 1) || (userMove == 3 && computerMove == 2)) {
        println("You won!")
        return 1 // user won
    }
    else {
        println("You lose!")
        return -1 // computer won
    }
}

// Function to display the scoreboard
fun displayScoreboard(fileName: String) {
    try {
        val file = File(fileName)
        if (!file.exists()) {
            return
        }

        val lines = file.readLines()
        if (lines.isEmpty()) {
            println("No scoreboard.")
        }
        else {
            for (line in lines) {
                val data = line.split(",")
                if (data.size >= 2) {
                    val name = data[0]
                    val score = data[1].toIntOrNull() ?: 0
                    println("$name, $score")
                }
            }
        }
    } catch (e: IOException) {
        println("error: ${e.message}")
    }
}
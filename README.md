# Tic-Tac-Toe

This game is made entirely using Java OOP. There exists a class created entirely to constuct the GUI consisting of the gameplay menus as well as the game board panel.

The game can be played either in double player mode or in single player mode againts the computer.

Rules of the game:
1. The game is played on a grid that's 3 squares by 3 squares.
2. You are X, the oponent is O (or vice-versa). Players take turns putting their marks in empty squares.
3. The first player to get 3 of their marks in a row (up, down, across, or diagonally) is the winner.
4. When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie.

(Source: [The exploratorium - Brain Explorer : Tic Tac Toe](https://www.exploratorium.edu/brain_explorer/tictactoe.html))

There are 4 levels to this game with progressing difficulty (for single player mode only).
- Easy
- Medium
- Hard
- Impossible
As the difficulty increases, the computer implements [minimax algorithm](https://en.wikipedia.org/wiki/Minimax) with [alpha-beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning) with a higher probability than randomly placing a tile in an empty spot making it much harder to choose an optimal spot to place the tile to win.

Code editor - Visual Studio Code by Microsoft
Graphics - Tiles created on Canva

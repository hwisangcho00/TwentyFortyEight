=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: hwisang
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Array:
  The board (matrix) is of type int[][]. The int value indicates the tile value that is located in that row and column
  of the matrix. The 2d array is initiated using a final ROW and COL value which are both 4. So we always have
  a 4 x 4 game board.


  2. JUnit Testing
  The JUnit testing ensures the methods work as intended. the moveDIRECTION() methods especially required
  thorough testing since there were multiple edge cases (such as spacing before merging, aligning to the direction
  after merging, ect). Also, it helped with the debugging process, since it was possible to set the board as intended.
  This would have been very difficult taking into consideration the randomness of new block being added to the block
  after every move.

  3. Collections
  To implement the undo functionality into the game, I needed a way to save the current state of the board after
  every move. Each state had a board and the score, so I decided to use Map.Entry<int[][], Integer> type to
  match the board with its score. I then put this data into List<Map.Entry<int[][], Integer>>.
  I used List since I had to keep the order of the current state. This became the @history variable that was used in
  both undo function and save/load function.


  4. File I/O
  This was to implement the save/load functionality of the game. Right before the window closing, the game state
  saves all of the data stored in the @history into a single .txt file. This .txt file is then read when the player
  decides to load the game. Since the @history is a List<Map.Entry<int[][], Integer>>, I have saved the int[][] matrix,
  followed by the Integer score.
  Here is a sample of a valid saveFile.txt:
  0 0 0 0
  0 0 0 0
  0 2 2 0
  0 0 0 0
  0
  0 2 2 0
  0 0 0 0
  0 0 2 0
  0 0 0 0
  0
  0 0 0 4
  0 0 0 0
  0 2 0 2
  0 0 0 0
  2


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

1) Matrix

This class stores the current state of the 4 x 4 matrix, score, all the history (changes in the matrix),
and the functionality the GameStateManger needs to keep track and update the game status.

Some major methods include:
isGameOver() - check if the matrix is unplayable or the player has reached the winning condition
addNewBlock() - add new block to a random empty tile
undo() - recovers the game state immediately before the most recent change
moveDIRECTION() - move the tiles to the DIRECTION. The tiles merge together when they are the same,
and all the tiles are aligned in the DIRECTION afterwards.

2) GameStateManager

This class has a private Matrix variable state and uses the Matrix functionality to update the game state
accordingly to the player input. It extends JPanel so it can take in user input and draw its state.
One special functionality of GameStateManager is that it has the ability to read and write file to
save and load game state.

3) RunTwentyFortyEight

This class is a Runnable class that holds the View aspect of the MVC model.
When first initiated it shows the MainScreen and the MenuBar which has the Instruction,
NewGame, Load button.

When the game starts by pressing either the NewGame or Load, the initial screen is set invisible,
and the user can see the GameStateManager and play the game.

4) MainScreen, InstructionScreen

The two classes extends the JPanel. Unlike Button, JPanel needs to override the paintComponent() method,
so I have decided to separate this part from the RunTwentyFortyEight class. It makes sure the image is present
then paint itself.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

One challenge was regarding aliases. When first testing the score functionality, I realized that the score was
incrementing dramatically more than it needed to be. Soon, I realized it was because of the isGameLost() that used
the moveDIRECTION() methods. Even though the changes weren't shown on the screen, the scores kept summing up when
I only wanted to see the result of the moveDIRECTION(). So I created a copy() function that shallow copied any
int[][] to ensure that @matrix was directly used in the isGameLost().


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I believe that the design is well separated. It may not follow the perfect MVC model, but every functionality is
independent from the GUI state. This was why I was able to use JUnit testing. Private state was encapsulated and
aliases were taken into consideration with the copy() method. One thing I would have refactored would be creating
a separate class (maybe Saver and Loader) for saving and loading functionality instead of putting it in the
GameStateManager.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
I created all of the pixel image.
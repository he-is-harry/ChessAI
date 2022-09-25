# ChessAI

Author: Harry He

## Project Description


https://user-images.githubusercontent.com/69978107/192163855-6ea7f9d5-ab31-4092-bd8b-f6e9a563dced.mp4

A video demonstration of how the game is played.

The application is a game where the user is able to play chess against another player, or a computer. All of the technologies used in the game are apart of the Java Standard Library, and there are no external libraries that were used in the creation of the game. In the game some of the features of the Java Standard Library used were graphics, threading, and text file editing.

## Computational Techniques

- Alpha-Beta pruning
- Quiescence search
- Zobrist hashing
- Transposition tables

## Installation and Running

1. Create or open the folder in which you wish to download and run the game.
	
2. Open the command line at that folder and download the game using the command.

       git clone https://github.com/he-is-harry/ChessAI.git

3. Move all java files into the src folder above where they currently are, so instead of src/main/java/src, move it into the parent src folder.

4. At the src folder run the following command to compile all of the Java files.

       javac $(find . -name "\*.java")

5. Move up to the parent folder, which should be named ChessAI by default by running the following command.

       cd ..

6. Run the game by running the command.

       java src/Game

## Using the Project

The application will accept mouse input. By clicking on a piece, possible moves will be highlighted. Then, selecting one of those moves will move the piece to that position. If no highlighted squares appear, then the piece cannot move.

## License

MIT License

Copyright (c) 2022 Harry He

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


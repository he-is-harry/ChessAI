/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;

/**
 *
 * @author harryhe
 */
public class Zobrist {
    
    Board board;
    
    private final int seed = 2361912;
    private final String randomNumbersFileName = "GeneratedRandomNumbers.txt";
    private String randomNumbersPath;

    public long[][][][] piecesArray = new long[13][2][8][8];
    private final int pieceTypeLength = 13;
    // The 2 are black and white
    // 1 will be white and 0 will be black which matches
    // the mod that we usually use
    public long[] castlingRights = new long[16];
    
    public long[] enPassantNums = new long[9];
    public long sideToMove;
    
    private Random prng = new Random(seed);
    
    private String truePath = "";
    
    public Zobrist(Board board){
        try{
            truePath = new File(".").getCanonicalPath();
        } catch (IOException e){
            System.out.println("Canonical Path has failed");
        }
        this.board = board;
        randomNumbersPath = getRandomNumbersPath();
        Queue<Long> randomNumbers = ReadRandomNumbers();
        
        for(int squareRIndex = 0; squareRIndex < 8; squareRIndex++){
            for(int squareCIndex = 0; squareCIndex < 8; squareCIndex++){
                for(int pieceIndex = 0; pieceIndex < pieceTypeLength; pieceIndex++){
                    piecesArray[pieceIndex][0][squareRIndex][squareCIndex]
                            = randomNumbers.poll();
                    piecesArray[pieceIndex][1][squareRIndex][squareCIndex]
                            = randomNumbers.poll();
                }
            }
        }
        
        for(int i = 0; i < 16; i++){
            castlingRights[i] = randomNumbers.poll();
        }
        
        for(int i = 0; i < enPassantNums.length; i++){
            enPassantNums[i] = randomNumbers.poll();
        }
        
        sideToMove = randomNumbers.poll();
    }
    
    long CalculateZobristKey(){
        long zobristKey = 0;
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                zobristKey ^= piecesArray[board.chessBoard[row][col]]
                [board.chessBoard[row][col] % 2][row][col];
            }
        }
        int epIndex = board.enPassantFile;
        if(epIndex != -1){
            zobristKey ^= enPassantNums[epIndex];
        }
        
        if(!board.whiteTurn){
            zobristKey ^= sideToMove;
        }
        
        int bK = board.bKCast ? 1 : 0;
        int bQ = board.bQCast ? 1 : 0;
        int wK = board.wKCast ? 1 : 0;
        int wQ = board.wQCast ? 1 : 0;
        
        zobristKey ^= castlingRights[8 * bK + 4 * bQ + 2 * wK + wQ];
        // Sudo binary
        
        return zobristKey;
    }
    
    void WriteRandomNumbers(){
        prng = new Random(seed);
        
        String randomNumberString = "";
        int numRandomNumbers = 64 * pieceTypeLength
                * 2 + castlingRights.length + 9 + 1;
        
        for(int i = 0; i < numRandomNumbers; i++){
            randomNumberString += RandomUnsigned64BitNumber();
            if(i != numRandomNumbers - 1){
                randomNumberString += ',';
            }
        }
        try {
            FileWriter writer = new FileWriter(randomNumbersPath);
            writer.write(randomNumberString);
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    Queue<Long> ReadRandomNumbers(){
        File file = new File(randomNumbersPath);
        if(!file.exists()){
            System.out.println("Created Random Number File");
            WriteRandomNumbers();
            file = new File(randomNumbersPath);
        }
        
        Queue<Long> randomNumbers = new LinkedList<Long>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(randomNumbersPath));
            String numbersString = reader.readLine();
            reader.close();
            
            String [] numberStrings = numbersString.split(",");
            for(int i = 0; i < numberStrings.length; i++){
                long number = Long.parseLong(numberStrings[i]);
                randomNumbers.add(number);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return randomNumbers;
    }
    
    String getRandomNumbersPath(){
        return truePath + "/res/" + randomNumbersFileName;
    }
    
    long RandomUnsigned64BitNumber(){
        byte[] buffer = new byte[8];
        prng.nextBytes(buffer);
        return ToUInt64(buffer, 0);
    }
    
    long ToUInt64(byte[] buffer, int index){
        return ((long) (0xff & buffer[index]) << 56
				| (long) (0xff & buffer[index + 1]) << 48
				| (long) (0xff & buffer[index + 2]) << 40
				| (long) (0xff & buffer[index + 3]) << 32
				| (long) (0xff & buffer[index + 4]) << 24
				| (long) (0xff & buffer[index + 5]) << 16
				| (long) (0xff & buffer[index + 6]) << 8 
                | (long) (0xff & buffer[index + 7]) << 0);
    }
}

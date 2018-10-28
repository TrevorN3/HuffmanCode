//Trevor Nichols
//HuffmanCode
//Section: AF

import java.io.PrintStream;
import java.util.*;

//Creates an instance of a HuffmanCode that can compress data and uncompress 
//data back into the original text. Uses ASCII to interchange between integer 
//values and character values.
public class HuffmanCode {
    private HuffmanNode huffmanRoot;
    
    //Creates a new instance of HuffmanCode given a integer array containing 
    //the ASCII values and their frequencies 
    public HuffmanCode(int[] frequencies){
        PriorityQueue<HuffmanNode> priorityChars = new PriorityQueue<HuffmanNode>();
        
        for(int i = 0; i < frequencies.length; i++){
            if(frequencies[i] > 0){
                priorityChars.add(new HuffmanNode(i, frequencies[i]));
            }
        }
        
        while(priorityChars.size() > 1){
            HuffmanNode tempLeft = priorityChars.remove();
            HuffmanNode tempRight = priorityChars.remove();
            HuffmanNode combinedNodes = new HuffmanNode('\0', 
                                  tempLeft.frequency + tempRight.frequency,
                                  tempLeft, tempRight);
            priorityChars.add(combinedNodes);
        }
        
        this.huffmanRoot = priorityChars.remove();
    }
    
    //Creates a new instance of HuffmanCode given a Scanner attached to data 
    //in standard format.
    public HuffmanCode(Scanner input){
        while(input.hasNext()){
            int charCode = Integer.parseInt(input.nextLine());
            String pathCode = input.nextLine();
        
            this.huffmanRoot = this.huffmanCodeHelper(this.huffmanRoot,
                                                    charCode, pathCode);
        }
    }
    
    //Helps construct the HuffmanCode given a HuffmanNode, 
    //an integer corresponding to a ASCII value, and a String.
    private HuffmanNode huffmanCodeHelper(HuffmanNode current, int leafData,
                                   String currentPath){
        if(current == null && currentPath.isEmpty()){
            return new HuffmanNode(leafData, -1);
        
        }else if(current == null){
            current = new HuffmanNode('\0', -1);
        }    
        
        char nextPath = currentPath.charAt(0);
            
        if(nextPath == '0'){
            current.leftZero = this.huffmanCodeHelper(current.leftZero, 
                                          leafData, currentPath.substring(1));
        
        }else{
            current.rightOne = this.huffmanCodeHelper(current.rightOne,
                                          leafData, currentPath.substring(1));
        }
        
        return current;
    }

    //Saves the current state of the current instance of HuffmanCode,
    //in standard format, given a PrintStream attached to a valid file.
    //throws IllegalArgumentException if the PrintStream is null.
    public void save(PrintStream output){
        if(output == null){
            throw new IllegalArgumentException("The PrintStream is null");
        }
        
        this.save(this.huffmanRoot, output, "");
    }
    
    //Helps save the current state of the current instance of HuffmanCode,
    //in standard format, given a HuffmanNode, PrintStream, and a String.
    private void save(HuffmanNode current, PrintStream output, String pathAcc){
        if(current != null){
           String tempPath = pathAcc;
           
            if (current.leftZero == null && current.rightOne == null){
                output.println(current.charData);
                output.println(pathAcc);
            }else{
                tempPath += "0";
                this.save(current.leftZero, output, tempPath);
                tempPath = pathAcc;
                tempPath += "1";
                this.save(current.rightOne, output, tempPath);
            }
        }
    }
    
    //Translates compressed data back into its original contents given a
    //BitInputStream and a PrintStream attached to a valid file. Uses the 
    //current instance of HuffmanCode.
    public void translate(BitInputStream input, PrintStream output){
        while(input.hasNextBit()){
            this.translate(this.huffmanRoot, input, output);
        }
    }
    
    //Assists in translating compressed data back into its original contents
    //given a HuffmanNode, BitInputStream, and a PrintStream.
    private void translate(HuffmanNode current, BitInputStream input,
                                                    PrintStream output){
        if(current != null){
            if (current.leftZero == null && current.rightOne == null){
                output.write(current.charData);
            
            }else if(input.nextBit() == 0){
                this.translate(current.leftZero, input, output);
            
            }else{
                this.translate(current.rightOne, input, output);
            }
        }
    }
    
    //A HuffmanNode represents an ASCII value. Implements comparable.
    private static class HuffmanNode implements Comparable<HuffmanNode>{
        public final int  charData;
        public final int frequency;
        public HuffmanNode leftZero;
        public HuffmanNode rightOne;
        
        //Constructs a new HuffmanNode given two integer values a ASCII value
        //and its frequency.
        public HuffmanNode(int inputChar, int currentFrequency){
            this(inputChar, currentFrequency, null, null);
        }
       
        //Constructs a new HuffmanNode given two integers, a ASCII value and
        //its frequency, and two HuffmanNodes.
        public HuffmanNode(int inputChar, int currentFrequency, 
                                    HuffmanNode left, HuffmanNode right){
            this.charData = inputChar;
            this.frequency = currentFrequency;
            this.leftZero = left;
            this.rightOne = right;
        }
        
        //Compares two HuffmanNodes based on their frequencies
        //given a second HuffmanNode. 
        //Returns an int based on the relationship of the two frequencies. 
        //returns a negative number for a less than relationship
        //returns a zero for an equal relationship
        //returns a positive number for a greater than relationship.
        @Override
        public int compareTo(HuffmanNode other){
            return Integer.compare(this.frequency, other.frequency);
        }
    }
}

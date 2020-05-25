package Model.Definitions;

import java.io.*;
import java.io.*;
import java.nio.charset.*;
import java.util.Scanner;


public class PatchUpdate {



    public static String readFile() throws IOException {
        try {
            Scanner scanner = new Scanner(new File("src/Controllers/TextFiles/PatchUpdates.txt"));
            String text = scanner.useDelimiter("\\A").next();
            scanner.close();
            return text;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return "";


    }


    }

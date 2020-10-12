package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        /*System.out.println("Enter filename:");

        String filename = sc.nextLine();
        Path path = Paths.get(filename);
        File file = path.toFile();*/

        File file = Paths.get("test.txt").toFile();

        try {
            Automate automate = new Automate(file);
            //automate.matrix();
            System.out.println("Enter the length of word:");
            int length;
            while (sc.hasNextLine()){
                if ((length = sc.nextInt()) > 0){
                    automate.setAllWords(length);
                    automate.printResult();
                } else
                    return;

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
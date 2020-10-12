package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Automate {
    private int wordLength;
    private final int alphabetLen;
    private final int stateQty;
    private final int startState;
    private final int finalStateQty;
    private final List<Integer> finalStates = new LinkedList<>();
    private ArrayList<Character>[][] transitionFunction;
    private int[][] neighbours;
    private ArrayList<ArrayList<Integer>> allWordsInteger = new ArrayList<>();
    private HashSet<String> allWords = new HashSet<>();
    private HashSet<Character> allLetters = new HashSet<>();
    private ArrayList<String> allProbWord = new ArrayList<>();

    public void matrix (){
        for(int i = 0; i < stateQty; i++){
            for (int j = 0; j < stateQty; j++){
                System.out.print(transitionFunction[i][j] + " ");
            }
            System.out.println();
        }
    }


    Automate(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        alphabetLen = Integer.parseInt(scanner.nextLine());
        stateQty = Integer.parseInt(scanner.nextLine());
        startState = Integer.parseInt(scanner.nextLine());
        String finalStateLine = scanner.nextLine();
        var finalStateString = finalStateLine.split(" ");
        finalStateQty = Integer.parseInt(finalStateString[0]);
        for (int i = 1; i <= finalStateQty; i++) {
            finalStates.add(Integer.parseInt(finalStateString[i]));
        }
        initTransitionFunction();
        initNeighbours();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            var statesAndLetters = line.split(" ");
            int state1 = Integer.parseInt(statesAndLetters[0]);
            int state2 = Integer.parseInt(statesAndLetters[2]);
            neighbours[state1][state2] += 1;
            var letter = statesAndLetters[1].charAt(0);
            transitionFunction[state1][state2].add(letter);
            allLetters.add(letter);
        }
    }

    private void initTransitionFunction() {
        transitionFunction = new ArrayList[stateQty][stateQty];
        for (int i = 0; i < stateQty; i++) {
            for (int j = 0; j < stateQty; j++) {
                transitionFunction[i][j] = new ArrayList<>();
            }

        }
    }

    private void initNeighbours() {
        neighbours = new int[stateQty][stateQty];
        for (int i = 0; i < stateQty; i++) {
            for (int j = 0; j < stateQty; j++) {
                neighbours[i][j] = 0;
            }
        }
    }


    private void findPathInMachine(Integer startState, ArrayList<Integer> currentWord) {

        if (finalStates.contains(startState)) {
            ArrayList<Integer> word = new ArrayList<>(currentWord);
            allWordsInteger.add(word);
        } else {
            for (int i = 0; i < stateQty; i++) {
                if (neighbours[startState][i] > 0) {
                    neighbours[startState][i] -= 1;
                    currentWord.add(i);
                    findPathInMachine(i, currentWord);
                    neighbours[startState][i] += 1;
                    currentWord.remove(currentWord.lastIndexOf(i));
                }

            }
        }

    }

    private ArrayList<ArrayList<Character>> convertPathToCharsArray(ArrayList<Integer> wordInt) {
        Integer currentState = wordInt.get(0);
        ArrayList<ArrayList<Character>> charsArray = new ArrayList<>();
        for (int i = 1; i < wordInt.size(); i++) {
            Integer nextState = wordInt.get(i);
            ArrayList<Character> chars = new ArrayList<>(transitionFunction[currentState][nextState]);
            charsArray.add(chars);
            currentState = nextState;
        }
        return charsArray;
    }


    private ArrayList<Integer> convertPathToIntegerArray(ArrayList<Integer> wordInt) {
        ArrayList<Integer> path = new ArrayList<>();
        int currentState = wordInt.get(0);
        for (int i = 1; i < wordInt.size(); i++) {
            int nextState = wordInt.get(i);
            path.add(currentState * stateQty + nextState);
            currentState = nextState;
        }
        return path;
    }

    private void makeWordsFromCharsArray(ArrayList<ArrayList<Character>> charsArray, int start,
                                         ArrayList<Character> current, ArrayList<Integer> path) {
        if (start == charsArray.size()) {
            if (checkWord(path, current)) {
                ArrayList<Character> word = new ArrayList<>(current);
                StringBuilder builder = new StringBuilder();
                for (Character ch : word) {
                    builder.append(ch);
                }
                allWords.add(builder.toString());
            }

        } else {
            for (int j = 0; j < charsArray.get(start).size(); j++) {
                current.add(charsArray.get(start).get(j));
                makeWordsFromCharsArray(charsArray, start + 1, current, path);
                current.remove(current.size() - 1);
            }
        }
    }


    public void setAllWords(int wordLen) {
        this.wordLength = wordLen;
        allProbWord.clear();
        ArrayList<Integer> word = new ArrayList<>();
        word.add(startState);
        findPathInMachine(startState, word);
        for (ArrayList<Integer> integers : allWordsInteger) {
            ArrayList<Character> current = new ArrayList<>();
            makeWordsFromCharsArray(convertPathToCharsArray(integers), 0, current,
                    convertPathToIntegerArray(integers));
        }
        StringBuilder builder = new StringBuilder();
        allProbWords(builder);
    }

    public void printAllWords() {
        for (String word : allWords) {
            if (word.length() == wordLength) {
                System.out.print(word);
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    private boolean checkWord(ArrayList<Integer> integerWord, ArrayList<Character> word) {
        HashMap<Integer, ArrayList<Character>> dep = new HashMap<>();
        for (int i = 0; i < integerWord.size(); i++) {
            if (dep.containsKey(integerWord.get(i))) {
                if (dep.get(integerWord.get((i))).contains(word.get(i))) {
                    return false;
                } else {
                    dep.get(integerWord.get(i)).add(word.get(i));
                }
            } else {
                ArrayList<Character> letters = new ArrayList<>();
                letters.add(word.get(i));
                dep.put(integerWord.get(i), letters);
            }
        }
        return true;
    }

    private void allProbWords(StringBuilder builder) {
        if (builder.length() == wordLength) {
            allProbWord.add(builder.toString());
            builder = new StringBuilder();
        } else {
            for (Character ch : allLetters) {
                builder.append(ch);
                allProbWords(builder);
                builder.deleteCharAt(builder.length() - 1);
            }
        }
    }

    public void printResult()
    {
        System.out.print("All possible words: ");
        printAllProbWords();
        System.out.print("All automate words: ");
        printAllWords();
        for (String word: allProbWord) {
            if (!allWords.contains(word))
            {
                System.out.println(false);
                return;
            }
        }
        System.out.println(true);
    }

    public void printAllProbWords() {

        for (String word : allProbWord) {
            System.out.print(word);
            System.out.print(" ");
        }
        System.out.println();
    }
}

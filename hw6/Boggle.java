import java.util.*;
import java.io.File;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static Trie trie = new Trie();
    private static List<String> words = new ArrayList<>();
    private static List<Integer>[] adj;
    private static char[][] board;
    private static char[] board1D;
    private static Node p;
    private static Stack<Node> stack;
    private static List<Integer> passed;
    private static List<String> ans = new ArrayList<>();

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        In in = new In(dictPath);
//        trie = new Trie();
        while (in.hasNextLine()) {
            trie.add(in.readLine());
        }

        In in1 = new In(boardFilePath);
        String[] s= in1.readAllLines();
        int r = s.length;
        int c = s[0].length();
        board = new char[r][c];
        board1D = new char[r * c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                board[i][j] = s[i].charAt(j);
                board1D[c * i + j] = s[i].charAt(j);
            }
        }

        adj = adjacentCharList();

        stack = new Stack<>();
        for (int i = 0; i < r * c; i++) {
            passed = new ArrayList<>();
            char startChar = board1D[i];
            p = trie.getRoot();
            stack.push(p);
            if (p.next.get(startChar) == null) {
                continue;
            }
            passed.add(i);
            p = p.next.get(startChar);
            DFS(i, board1D);
        }

        return sortList(ans, k);
    }

    private static List<Integer>[] adjacentCharList() {
        int row = board.length;
        int col = board[0].length;
        List<Integer>[] adj = new List[row * col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int v = xyTo1D(i, j, col);
                adj[v] = new ArrayList<>();
                if (checkIfVIsValid(i - 1, j - 1, row, col)) {
                    adj[v].add(xyTo1D(i - 1, j - 1, col));
                }
                if (checkIfVIsValid(i - 1, j, row, col)) {
                    adj[v].add(xyTo1D(i - 1, j, col));
                }
                if (checkIfVIsValid(i - 1, j + 1, row, col)) {
                    adj[v].add(xyTo1D(i - 1, j + 1, col));
                }
                if (checkIfVIsValid(i, j - 1, row, col)) {
                    adj[v].add(xyTo1D(i, j - 1, col));
                }
                if (checkIfVIsValid(i, j + 1, row, col)) {
                    adj[v].add(xyTo1D(i, j + 1, col));
                }
                if (checkIfVIsValid(i + 1, j - 1, row, col)) {
                    adj[v].add(xyTo1D(i + 1, j - 1, col));
                }
                if (checkIfVIsValid(i + 1, j, row, col)) {
                    adj[v].add(xyTo1D(i + 1, j, col));
                }
                if (checkIfVIsValid(i + 1, j + 1, row, col)) {
                    adj[v].add(xyTo1D(i + 1, j + 1, col));
                }
            }
        }
        return adj;
    }

    private static boolean checkIfVIsValid(int i, int j, int row, int col) {
        return (i >= 0 && i < row && j >= 0 && j < col);
    }

    private static int xyTo1D(int x, int y, int size) {
        return x * size + y;
    }

    // DFS
    private static void DFS(int v, char[] board1D) {
        if (p.isWord()) {
            String a = pathToString(passed, board1D);
            if (!ans.contains(a)) {
                ans.add(a);
            }
        }
        List<Integer> adjVertices = adj[v];
        stack.push(p);
        for (int w : adjVertices) {
            if (passed.contains(w)) {
                continue;
            }
            if (p.next.size() == 0) {
                continue;
            }
            char nextChar = board1D[w];
            if (!p.next.containsKey(nextChar)) {
                continue;
            }
            p = p.next.get(nextChar);
            passed.add(w);
            DFS(w, board1D);
        }
        passed.remove(passed.size() - 1);
        stack.pop();
        p = stack.peek();
    }

    private static String pathToString(List<Integer> passed, char[] board1D) {
        Iterator<Integer> i = passed.iterator();
        StringBuilder sb = new StringBuilder();
        while (i.hasNext()) {
            sb.append(board1D[i.next()]);
        }
        return sb.toString();
    }

    private static List<String> sortList(List<String> myList, int k) {
        Comparator<String> x = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() > o2.length()) {
                    return -1;
                }
                if (o2.length() > o1.length()) {
                    return 1;
                }
                if (o2.length() == o1.length()) {
                    return o1.compareTo(o2);
                }
                return 0;
            }

        };

        List<String> sortedList = new ArrayList<>();
        Collections.sort(myList,  x);
        for (int i = 0; i < k; i++) {
            if (i > myList.size() - 1) {
                break;
            }
            sortedList.add(myList.get(i));
        }
        return sortedList;
    }

    public static void main(String[] args) {
        File tmpDir = new File(args[1]);
        if(Integer.valueOf(args[0]) <= 0 || !tmpDir.exists()){
            throw new IllegalArgumentException();
        }

        List<String> solveList = solve(Integer.valueOf(args[0]) ,args[1]);
        Iterator<String> i = solveList.iterator();
        while(i.hasNext()){
            System.out.println(i.next());
        }
    }
}


package gomoku;


import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/*Gomoku class pour aider les étudiants à ne pas tout coder.
//@author JBC
Pour simplifier le plateau est une liste unidimensionnelle, à voir pour le passer en bytecode direct
sans doute pas clair pour les étudiants.
On ne gére pas le cas du match nul, faut check si board plein et on arrete (ou alors on compte le nombre de coup en tout et 
on arrete à SIZE*SIZE
*/



public class Gomoku {

    private static final int SIZE = 15; // Taille du plateau (15x15)
    private static final char EMPTY = '.';
    private static final char PLAYER1 = 'X'; // Représentation du joueur 1
    private static final char PLAYER2 = 'O'; // Représentation du joueur 2
    private List<Character> board; // Plateau représenté comme une liste
    private char currentPlayer;
    private Random random;
    private static final int DEPTH = 2;
    private static final String BASE64_CHARS ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    
    private static int nombrePions =0;
    
    private boolean saving = false;

    private HashMap<String, Integer>[] pionCaches;


    private final int[] firstMoves = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 300, 300, 300, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 300, 500, 300, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 300, 300, 300, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public Gomoku(boolean saving) {
        this.saving = saving;
        board = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            board.add(EMPTY);
        }
        currentPlayer = PLAYER1; // Le joueur 1 commence
        random = new Random();

        pionCaches = new HashMap[SIZE * SIZE + 1];
        for (int i = 0; i <= SIZE * SIZE; i++) {
            pionCaches[i] = new HashMap<>();
            loadScoresFromFile(i); 
        }
    }

    private void loadScoresFromFile(int pionCount) {
        String filename = "dictionnaires/dictionnaire" + pionCount + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    pionCaches[pionCount].put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            // System.err.println("Could not read file: " + filename);
        }
    }

private void saveScoresToFile(int pionCount) {
    String filename = "dictionnaires/dictionnaire" + pionCount + ".txt";
    
    // lire les fichiers pour eviter les doubles
    Set<String> existingIDs = new HashSet<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length > 0) {
                existingIDs.add(parts[0].trim()); // Add existing ID to the set
            }
        }
    } catch (IOException e) {
        
    }

    // ecrire les scores dans le fichier
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {  // 'true' enables append mode
        for (String key : pionCaches[pionCount].keySet()) {
            if (!existingIDs.contains(key)) { // verifier les doubles
                writer.write(key + ":" + pionCaches[pionCount].get(key));
                writer.newLine();
            }
        }
    } catch (IOException e) {
        System.err.println("Could not write to file: " + filename);
    }
}


    public void storeIDScore(int score) {
        String boardState = boardID();
        pionCaches[nombrePions].put(boardState, score);
        saveScoresToFile(nombrePions);  
    }

    // Affiche le plateau de jeu
    public void printBoard() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            if (i < 10) {
                System.out.print(i + " ");
            } else {
                System.out.print(i);
            }
        }
        System.out.println();
        for (int row = 0; row < SIZE; row++) {
            // System.out.print(row + " ");
            String formattedNumber = String.format("%02d", row);
            System.out.print(formattedNumber);
            for (int col = 0; col < SIZE; col++) {
                System.out.print(board.get(row * SIZE + col) + " ");
            }
            System.out.println();
        }

//        System.out.println("Score: " + calculateScore());
    }

    public String boardID(){

        String id = "";
        
        for (int row = 0; row < SIZE; row++){
        
            for (int col = 0; col < SIZE; col++){
            
            int index =row * SIZE + col;
            
            if (board.get(index) == EMPTY)
                id += "0";
            if (board.get(index) == PLAYER1)
                    id += "1";
            if (board.get(index) == PLAYER2)
                    id += "2";
            
            }
        
        }


        BigInteger decimalID = new BigInteger(id, 3);
        BigInteger number = decimalID;
        StringBuilder result = new StringBuilder();
        BigInteger base = BigInteger.valueOf(64);

        while(number.compareTo(BigInteger.ZERO)> 0){
            int remainder = number.mod(base).intValue();
            result.insert(0,BASE64_CHARS.charAt(remainder));
            number = number.divide(base);

        }
        return result.toString();
        
    }

    // Permet au joueur actuel de faire un mouvement
    public void makeMove(int row, int col) {
        int index = row * SIZE + col;
        if (board.get(index) == EMPTY) {
            board.set(index, currentPlayer);
            if (checkWin(row, col)) {
                printBoard();
                System.out.println("Le joueur " + currentPlayer + " a gagné!");
                System.exit(0);
            }
            // Changer de joueur
            currentPlayer = (currentPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
        } else {
            System.out.println("Case est déjà occupée.");
        }
    }

    // calcul score du tableau
    private int calculateScore() {
        int score = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                char player = board.get(row * SIZE + col);
                if (player == PLAYER1) {
                    score -= calculDirection(row, col, player);
                } else if (player == PLAYER2) {
                    score += calculDirection(row, col, player);
                    int index = row * SIZE + col;
                    score += firstMoves[index];
                }

            }
        }
        return score;
    }
    // calcul score d'une direction

    private int calculDirection(int row, int col, char player) {
        int score = 0;
        score += scoreDirection(row, col, 1, 0, player);
        score += scoreDirection(row, col, 0, 1, player);
        score += scoreDirection(row, col, 1, 1, player);
        score += scoreDirection(row, col, 1, -1, player);

        return score;
    }

    private int scoreDirection(int row, int col, int dRow, int dCol, char player) {

        int compteur = 0;
        int vide = 0;
        int r = row;
        int c = col;

        //parcourir une direction donnée jusqu'il n'a plus de pions d'un joueur donné
        while (r < SIZE && r >= 0 && c < SIZE && c >= 0 && board.get(r * SIZE + c) == player) {
            compteur++;
            r += dRow;
            c += dCol;
        }

        if (r < SIZE && r >= 0 && c < SIZE && c >= 0 && board.get(r * SIZE + c) == EMPTY) {
            vide++;
        }

        // Répéter dans la direction opposée
        r = row - dRow;
        c = col - dCol;

        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == player) {
            compteur++;
            r -= dRow;
            c -= dCol;
        }

        if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == EMPTY) {
            vide++;
        }

        if (compteur == 5) {
            return 99999;
        } else if (compteur == 4 && vide == 2) {
            return 8000;
        } else if (compteur == 4 && vide == 1) {
            return 5000;
        } else if (compteur == 3 && vide == 2) {
            return 3000;
        } else if (compteur == 3 && vide == 1) {
            return 1000;
        } else if (compteur == 2 && vide == 2) {
            return 100;
        } else if (compteur == 2 && vide == 1) {
            return 50;
        }
        return 0;

    }

    public int findIDScore() {
        String boardState = boardID();
        if (pionCaches[nombrePions].containsKey(boardState)) {
            return pionCaches[nombrePions].get(boardState);
        }
        int score = calculateScore();
        if(saving == true){
        storeIDScore(score);  //sauvegarder score apres calcul
        }
        return score;
    }

    public int[] minimax(int depth, char player, int alpha, int beta) throws IOException {
        // System.out.println(depth);
        List<Integer> emptyPositions = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == EMPTY) {
                emptyPositions.add(i);
            }
        }

        int maxScore = (player == PLAYER2) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//        int score;
        int maxRow = -1;
        int maxCol = -1;

        if (depth == 0 || emptyPositions.isEmpty()) {
            maxScore = findIDScore();

        } else {
            for (int index : emptyPositions) {
                int row = index / SIZE;
                int col = index % SIZE;
                board.set(index, player);
                nombrePions++;
                
                // max joueur 2: O
                int score = minimax(depth - 1, (player == PLAYER1) ? PLAYER2 : PLAYER1, alpha, beta)[0];
                board.set(index, EMPTY);
                nombrePions--;

                if (player == PLAYER2 && score > maxScore) {
                    
                        maxScore = score;
                        maxRow = row;
                        maxCol = col;
                        
                    alpha = Math.max(alpha, maxScore);
                    //min (joueur 1: X)
                } else if (player == PLAYER1 && score < maxScore) {
                    
                        maxScore = score;
                        maxRow = row;
                        maxCol = col;
                        
                    beta = Math.min(beta, maxScore);
                }

                

                if (beta <= alpha) {
                    break;
                }
            }
        }
        return new int[]{maxScore, maxRow, maxCol};

    }

    public void makeMinimaxMove() throws IOException {
        int[] bestMoves = minimax(DEPTH, currentPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);
        makeMove(bestMoves[1], bestMoves[2]);
    }

    // Permet au joueur 2 de jouer un coup aléatoire
    public void makeRandomMove() {
        List<Integer> emptyPositions = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i) == EMPTY) {
                emptyPositions.add(i);
            }
        }
        if (!emptyPositions.isEmpty()) {
            int randomIndex = emptyPositions.get(random.nextInt(emptyPositions.size()));
            int row = randomIndex / SIZE;
            int col = randomIndex % SIZE;
            makeMove(row, col);
        }
    }

    // Vérifie si le mouvement courant entraîne une victoire
    private boolean checkWin(int row, int col) {
        return (checkDirection(row, col, 1, 0) // Vérifie l'horizontale
                || checkDirection(row, col, 0, 1) // Vérifie la verticale
                || checkDirection(row, col, 1, 1) // Vérifie la diagonale descendante
                || checkDirection(row, col, 1, -1)); // Vérifie la diagonale montante
    }

    // Vérifie une direction pour les conditions de victoire TODO : le tester aux limites
    private boolean checkDirection(int row, int col, int dRow, int dCol) {
        int count = 1; // Compte le pion actuel
        count += countStones(row, col, dRow, dCol); // Compte les pions dans une direction
        count += countStones(row, col, -dRow, -dCol); // Compte les pions dans l'autre direction
        return count >= 5; // Victoire si 5 pions alignés
    }

    // Compte les pions dans une direction donnée, TODO : le tester aux limites
    private int countStones(int row, int col, int dRow, int dCol) {
        int count = 0;
        char player = currentPlayer;
        int r = row + dRow;
        int c = col + dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board.get(r * SIZE + c) == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }

    // Démarre le jeu avec l'interaction utilisateur
    public void playGame() throws IOException {
        Scanner scanner = new Scanner(System.in);
        
               
        while (true) {
            
            printBoard();
            System.out.println(boardID());
            System.out.println("C'est le tour du joueur " + currentPlayer);
            if(nombrePions==0){
                makeMove(7, 8);
                
            }else if (currentPlayer == PLAYER1) {
                makeMinimaxMove();
                // System.out.print("Entrez la ligne: ");
                // int row = scanner.nextInt();
                // System.out.print("Entrez la colonne: ");
                // int col = scanner.nextInt();
                // if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                //     makeMove(row, col);
                //     } else {
                //     System.out.println("Coordonnées invalides. Essayez de nouveau.");
                // }
            } else {
                makeMinimaxMove(); // Le joueur 2 joue un coup aléatoire
            }
            
            nombrePions++;
            
            
        }}
                 

	public static void main(String[] args) throws IOException {
            // false pour ne pas sauvegarder les coups calcules, true pour le faire
		Gomoku game = new Gomoku(true);
		game.playGame();
	}
}

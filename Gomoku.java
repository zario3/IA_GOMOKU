package Gomoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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

	public Gomoku() {
		board = new ArrayList<>();
		for (int i = 0; i < SIZE * SIZE; i++) {
			board.add(EMPTY);
		}
		currentPlayer = PLAYER1; // Le joueur 1 commence
		random = new Random();
	}

	// Affiche le plateau de jeu
	public void printBoard() {
		System.out.print("  ");
		for (int i = 0; i < SIZE; i++) {
			if (i < 10)
				System.out.print(i + " ");
			else
				System.out.print(i);
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
	public void playGame() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			printBoard();
			System.out.println("C'est le tour du joueur " + currentPlayer);
			if (currentPlayer == PLAYER1) {
				System.out.print("Entrez la ligne: ");
				int row = scanner.nextInt();
				System.out.print("Entrez la colonne: ");
				int col = scanner.nextInt();
				if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
					makeMove(row, col);
				} else {
					System.out.println("Coordonnées invalides. Essayez de nouveau.");
				}
			} else {
				makeRandomMove(); // Le joueur 2 joue un coup aléatoire
			}
		}
	}

	public static void main(String[] args) {
		Gomoku game = new Gomoku();
		game.playGame();
	}
}

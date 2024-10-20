/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

import java.io.IOException;
import java.util.Random;

/**
 *
 * @author alain
 */
public class WeightOptimizer {

    private static final int GAMES_TO_PLAY = 5;
    private static final int MAX_ITERATIONS = 50;
    private static final int MAX_WEIGHT[] = {100, 2000, 6000, 10000, 15000, 30000};
    private static final int MIN_WEIGHT[] = {10, 100, 2000, 6000, 10000, 15000};
    private static Random random;
    private static int player1Wins = 0;
    private static int player2Wins = 0;
    private static Gomoku gameInstance;

    private static int[] randomizeWeights() {
        int[] weights = new int[6];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextInt(MAX_WEIGHT[i] - MIN_WEIGHT[i] + 1) + MIN_WEIGHT[i];
        }
//        System.out.println(arrayToString(weights));
        return weights;
    }

    // Lancer le jeu
    private static char playGame(int[] poidsPlayer1, int[] poidsPlayer2) throws IOException {

        gameInstance = new Gomoku(false, false, poidsPlayer1, poidsPlayer2);
        gameInstance.print = false;

        return gameInstance.playGame();

    }

    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i : array) {
            sb.append(i).append(" ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) throws IOException {

        int bestEmptySquares = 225;
        random = new Random();

        int[] bestWeights = {};
        int[] bestWeightsPlayer1 = {50, 100, 1000, 3000, 5000, 8000};
        int[] bestWeightsPlayer2 = {10, 100, 1000, 5000, 10000, 20000};

        int[] randomWeightsPlayer1 = bestWeightsPlayer1;
        int[] randomWeightsPlayer2 = randomizeWeights();

        // weightControl permet de controller si on change poids1 ou poids2. Si = true -> changer poids2 sinon poids1
        boolean weightControl = true;

        //faire MAX_ITERATIONS pour touver les meilleurs poids
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            System.out.println("Iteration: " + i);
            //chaque iteration, consiste de jouer GAMES_TO_PLAY jeux. 
            for (int j = 0; j < GAMES_TO_PLAY; j++) {

                char winner = playGame(randomWeightsPlayer1, randomWeightsPlayer2);
                // Si X gagne, gagner les poids de X et changer celles de O
                if (winner == 'X') {
                    player1Wins++;
                    if (weightControl) {
                        randomWeightsPlayer2 = randomizeWeights();
                    }
                    // Si O gagne, gagner les poids de X et changer celles de X
                } else if (winner == 'O') {
                    player2Wins++;
                    if (!weightControl) {
                        randomWeightsPlayer1 = randomizeWeights();
                    }

                }
            }
            System.out.println("Player X: " + player1Wins + "/" + GAMES_TO_PLAY);
            System.out.println("Player O: " + player2Wins + "/" + GAMES_TO_PLAY);

            // Si poids1 gagnent sur ceux de 2, les sauvegarder s'ils sont les meilleurs poids et changer poids2
            if (player2Wins - player1Wins <= -3) {
                if (gameInstance.emptySquares() < bestEmptySquares) {
                    bestWeightsPlayer1 = randomWeightsPlayer1;
                    bestEmptySquares = gameInstance.emptySquares();

                }
                randomWeightsPlayer2 = randomizeWeights();
                System.out.println("Poids 2 changés");
                weightControl = true;
            }
            // Si poids2 gagnent sur ceux de 1, les sauvegarder s'ils sont les meilleurs poids et changer poids1

            if (player1Wins - player2Wins <= -3) {
                if (gameInstance.emptySquares() < bestEmptySquares) {
                    bestWeightsPlayer2 = randomWeightsPlayer2;
                    bestEmptySquares = gameInstance.emptySquares();

                }

                randomWeightsPlayer1 = randomizeWeights();
                System.out.println("Poids 1 changés");

                weightControl = false;

            }
            player1Wins = 0;
            player2Wins = 0;
            System.out.println();
        }
        //faire affronter les meilleurs poids parmi les 2 jouers et trouver le meilleur.
        char winner = playGame(bestWeightsPlayer1, bestWeightsPlayer2);

        if (winner == 'X') {
            bestWeights = bestWeightsPlayer1;
        } else if (winner == 'O') {
            bestWeights = bestWeightsPlayer2;

        }

        System.out.println("Meilleurs Poids: " + arrayToString(bestWeights));

    }

}

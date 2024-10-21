# GOMOKU: Rendu 2

## Partie I: Enregistrement des scores

La première partie de ce deuxième rendu consiste à enregistrer toutes les évaluations de plateaux au cours des jeux de Gomoku afin d'éviter de recalculer le score des mêmes plateaux de jeu lors des prochaines parties. L'objectif est donc de réduir le temps de clacul en évitant d'utiliser la fonction d'évaluation à chaque appel de minimax.

L'enregistrement des scores on le fait dans des fichiers, où chaque fichier contient les évaluations des plateaux à n pions. Cette séparation permet de faire la recherche dans ces fichiers plus rapidement.

Pour identifier chaque plateau de jeu, on calcule un ID unique. Dans les fichiers, les données sont donc stockées par couples (ID, score). Le calcul de cet ID consiste à obtenir une représentation numérique en base 3 du plateau en observant l'état de chaque case (vide, joueur 1, joueur 2) puis en transormant cette valeur en base 64 pour réduir sa taille.

La fonctionalité d'enregistrement on la trouve dans le fichier Gomoku.java.

Le constructeur Gomoku (sauvegarde, lecture, poids1, poids2) permet la personnalisation de cette fonctionnalité d'enregistrement:
 - pour sauvegarder dans le fichier sauvegarde = true
 - pour lire uniquement du fichier sans les calculs de sauvegarde, lecture = true

Au lancement de Gomoku, les paires (ID/score) sont lus des fichiers et sauvegardés dans des dictionnaires pour l'utilisation durant le jeu.


## Partie II: Optimisation des poids

L'optimisation des poids se trouve dans le fichier WeightOptimizer.java.

L'objectif de cette partie est de trouver les meilleurs coefficients/poids pour notre fonction d'évaluation. Pour cela on va faire combattre des IA avec des poids différents et les faire varier selon ses victoires ou défaites.

Des poids minimum et maximumm sont définis. On fixe l'un deux pour pouvoir commencer.
Plus le nombre d'itérations MAX_ITERATIONS est grand, on aura un resulat plus précis vu car on essaye plus de combinaisons différentes.
Durant chaque iteration, X et O s'affrontent. Lors d'une itération donnée, un des poids de X et de O sont changés.
À la fin de chaque itération, le joueur avec le plus de jeux gagnés garde ses poids, et si le nombre de cases vides est inférieur au nombre de cases vides précédents, ses poids sont sauvegardés étant les meilleurs jusqu'à présent, et les poids du deuxième joueur sont changés.

A la fin des itérations, les meilleurs poids du joueur 1 et ceux du joueur 2 s'affrontent, et le stdout affichera les meilleurs poids (ceux du gagnant).
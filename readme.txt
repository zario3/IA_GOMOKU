Mode d'utilisation:

2 fichiers:
- Gomoku.Java : Jeu Gomoku + fonctionnalité lecture/sauvegarde de poids dans les fichiers txt
- WeightOptimizer.Java : Programme pour trouver les meilleurs poids

Constructeur Gomoku (sauvegarde, lecture, poids1, poids2):
- pour sauvegarder dans le fichier sauvegarde = true
- pour lire uniquement du fichier sans les calculs de sauvegarde, lecture = true

Au lancement de Gomoku, les paires ID/score sont lus des fichiers et sauvegardés dans des dictionnaires pour l'utilisation durant le jeu.

WeightOptimizer:
Des poids min et max sont définis. On fixe l'un deux pour pouvoir commencer.
Plus que le nombre MAX_ITERATIONS est plus grand, on aura un resulat plus précis vu un nombre d'essais plus grand.
Durant chaque iteration, X et O s'affrontent. Durant une itération donnée, un des poids de X et de O sont changés.
A la fin de chaque itération, le jouer avec plus de jeux gagnés garde ses poids, et si le nombre de cases vides est < le nombre de cases vides précédent, ses poids sont sauvegardés étant les meilleurs jusqu'à présent, et les poids du deuxième joueur sont changés.

A la fin des itérations, les meilleurs poids du joueur 1 et ceux du joueur 2 s'affrontent, et le stdout affichera les meilleurs poids (ceux du gagnant).
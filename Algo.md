# Defunition de la fonction d'evaluation

## Situations perdantes

- 3 pions de l'adversaire de suite qui ne sont bloques d'aucun cote.

  ![](images/perdante1.png)

- 4 pions de l'adversaire qui sont bloques de 1 cote ou moins + trous.

  ![](images/perdante2.png)

## Evaluation d'un tableau

_On regarde l'ensemble de jetons ainsi que les chemins qu'ils forment_

chemin bloqué = 0  
2 ensembles + 1 coté bloqué = 50 points  
2 ensembles libres = 100 points  
3 ensembles + 1 coté bloqué = 1000 points  
3 ensembles libres = 3000 points  
4 ensembles + 1 coté bloqué = 5000 points  
4 ensembles libres = 8000 points
5 ensemnle = 99999 (infini)

## Algo fonction d'évaliation

**calculateScore** :

Cette fonction calcul le score du plateau à un état donné.
Pour cela toutes les cases sont parcourues et analysées selon si elles sont occupées et de ce qu'il y a autour.
Lorsqu'une case est occupée on appelle CallculDirection.

**calculDirection** :

Cette fonction analyse les 4 directions possibles et appelle scoreDirection pour obtenir un score.

**scoreDirection**

Cette fonction regarde les cases autour d'un jeton dans une direction donnée et évalue la situation selon les critères définis en dessu.

**minimax**

Evalue tous les futurs coups possibles à une distance (profondeur) donnée et choisi le meilleur coup possible supposant que le joueur adverse choisisse son meilleur coup selon nos critères.

**makeMinimaxMove**

Appel de la fonction makeMove avec le véredicte de la fonction miniMax

**firstMoves**

Celui-ci est un tableau de force, défini pour que au début de la partie on donne un préférence aux cases au centre du plateau de jeu.
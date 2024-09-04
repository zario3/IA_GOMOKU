# Defunition de la fonction d'evaluation

## Situations perdantes

- 3 pions de l'adversaire de suite qui ne sont bloques d'aucun cote.

  ![](images/perdante1.png)

- 4 pions de l'adversaire qui sont bloques de 1 cote ou moins + trous.

  ![](images/perdante2.png)

## Evaluation d'un tableau

*On regarde l'ensemble de jetons ainsi que les chemins qu'ils forment*

chemin bloqué = 0  
1 pion seul = 1 point  
2 ensembles + 1 coté bloqué = 2 points  
2 ensembles libres = 3 points  
3 ensembles + 1 coté bloqué = 4 points  
3 ensembles libres = 5 points  
4 ensembles + 1 coté bloqué = 6 points  
4 ensembles libres = inf

ArrayList tableau du board

Algo:

move(){

verifier score actuel  
si on est d=0  
make move

move(nouvelles données, d-1)

return x, y  
}

## Algo fonction d'évaliation

**Entrées** : 
- Tableau

**Sorties** :
- Score du tableau pour le joueur donné

**idées**

création d'une sous fonctions qui évalue une case:
celle ci prend en entrée le joueur en question, le tableau, et la position de la case à évaluer

On parcours le tableau entier:
si la case est vide. -> skip
si la case est occuppée on appelle la fonction d'évaluation de case

**calculateScore**
```
Cette fonction calcule le score d'un plateau donné
```

**calculDirection**
```
Cette fonction calcul le score d'une case donnée en analysant toutes les possibles directons. Si c'est nous qui jouons le score est ajouté, si c'est l'adversaire il est soustrait au score total de la position.
``` 

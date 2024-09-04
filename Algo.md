# Defunition de la fonction d'evaluation

## Situations perdantes

- 3 pions de l'adversaire de suite qui ne sont bloques d'aucun cote.

  ![Exemple](IA_GOMOKU/images/perdante1.png)

- 4 pions de l'adversaire qui sont bloques de 1 cote ou moins + trous.

  ![Exemple](IA_GOMOKU/images/perdante2.png)

move()

verifier score actuel
si on est d=0
make move

move(nouvelles données, d-1)

return x, y

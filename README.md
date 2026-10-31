# Destino y Tablero

Este módulo modela un tablero rectangular con elementos, una nave y un objetivo. La clase `Tablero` mantiene el estado de la grilla. `Destino` coordina un turno de intentos de movimiento aleatorios sobre sus elementos. La interacción entre ambas clases se implementa de forma coordinada: `Tablero` mantiene el estado y `Destino` solicita y aplica los movimientos de cada turno.

## Modelo del tablero

Las dimensiones se expresan en filas (`x`) y columnas (`y`), entre 5 y 100 inclusive. Las coordenadas de las casillas comienzan en cero: `x` es el índice de fila y `y` el índice de columna. Cada casilla puede alojar como máximo un elemento. La nave y el objetivo se almacenan por separado de la ocupación de elementos.

Al construir el tablero, sus elementos se distribuyen en casillas distintas. La cantidad máxima admitida es `x * y / 4 * 3`. El objetivo y la posición de la nave comienzan sin establecerse.

## API pública de `Tablero`

### Construcción

```java
Tablero(int x, int y, ArrayList<ElementoDelUniverso> elementos) throws DimensionesDeTableroInconsistentesExceptionException, CantidadDeElementosSuperaCasillaException
```

Crea el tablero y distribuye una copia de la lista recibida. Lanza `DimensionesDeTableroInconsistentesException` si alguna dimensión está fuera del rango permitido y `CantidadDeElementosSuperaCasillaException` si la cantidad supera la capacidad máxima. La excepción conserva ese límite en `getCantidadMaxima()`, además de incluirlo en el mensaje.

### Consultas y operaciones

| Método | Comportamiento |
| --- | --- |
| `int getX()` | Devuelve la cantidad de filas. |
| `int getY()` | Devuelve la cantidad de columnas. |
| `ElementoDelUniverso moverNave(Posicion posicion)` | Sitúa la nave en la casilla indicada y devuelve el elemento que la ocupa, o `null` si está vacía. Notifica a los observadores. La posición debe ser válida, el método no comprueba los límites. |
| `Posicion obtenerObjetivo()` | Elige y guarda una posición objetivo en una casilla sin elementos. Puede coincidir con el objetivo anterior. |
| `ElementoDelUniverso[][] verTablero()` | Devuelve una matriz nueva de dimensiones `x` por `y`. La primera dimensión representa filas y la segunda columnas, se accede como `matriz[fila][columna]`. Las casillas vacías contienen `null`. La matriz no incluye la nave ni el objetivo. |

`Posicion` representa coordenadas inmutables y es el tipo usado por las operaciones de movimiento y objetivo. Sus coordenadas coinciden con los índices de la matriz devuelta por `verTablero()`.

## API pública de `Destino`

### Construcción

```java
Destino(Tablero tablero)
```

Crea un coordinador asociado al tablero recibido. El tablero debe ser no nulo.

### Operación de turno

```java
void turnoDestino()
```

Selecciona al azar, sin repetir, la mitad entera de los elementos del tablero e
intenta mover cada uno a una de sus ocho casillas vecinas. El intento se descarta
si la posición queda fuera del tablero, está ocupada o coincide con el objetivo.
Los movimientos se procesan secuencialmente. Al finalizar, notifica a los
observadores del tablero, aunque ningún elemento haya podido moverse.

Los ejemplos y firmas usan `ElementoDelUniverso`, el tipo de elemento del dominio.

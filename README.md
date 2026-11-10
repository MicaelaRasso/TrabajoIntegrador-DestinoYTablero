# Destino y Tablero

Este módulo modela un tablero rectangular con elementos, una nave y un objetivo. La clase `Tablero` mantiene el estado de la grilla. `Destino` coordina un turno de intentos de movimiento aleatorios sobre sus elementos. La interacción entre ambas clases se implementa de forma coordinada: `Tablero` mantiene el estado y `Destino` solicita y aplica los movimientos de cada turno.

## Modelo del tablero

Las dimensiones se expresan en filas (`x`) y columnas (`y`), entre 5 y 100 inclusive. Las coordenadas de las casillas comienzan en cero: `x` es el índice de fila y `y` el índice de columna. Cada casilla puede alojar como máximo un elemento. La nave y el objetivo se almacenan por separado de la ocupación de elementos.

Al construir el tablero, sus elementos se distribuyen en casillas distintas. La cantidad máxima admitida es `x * y / 4 * 3`. La nave comienza en el centro del tablero, en la posición `(x / 2, y / 2)`. El objetivo comienza sin establecerse.

## API pública de `Tablero`

### Construcción

```java
Tablero(int x, int y, ArrayList<ElementoDelUniverso> elementos) throws DimensionesDeTableroInconsistentesException, CantidadDeElementosSuperaCasillaException
```

Crea el tablero y distribuye una copia de la lista recibida. Lanza `DimensionesDeTableroInconsistentesException` si alguna dimensión está fuera del rango permitido y `CantidadDeElementosSuperaCasillaException` si la cantidad supera la capacidad máxima. La excepción conserva ese límite en `getCantidadMaxima()`, además de incluirlo en el mensaje.

### Consultas y operaciones

| Método | Comportamiento |
| --- | --- |
| `int getX()` | Devuelve la cantidad de filas. |
| `int getY()` | Devuelve la cantidad de columnas. |
| `void agregarObservador(Observador observador)` | Registra un observador. Requiere que no sea `null` e ignora registros duplicados. |
| `void quitarObservador(Observador observador)` | Quita un observador. No hace nada si no estaba registrado. |
| `void notificarObservadores(String mensaje)` | Llama a `actualizar(mensaje)` en cada observador registrado. *(Versión 2, en la versión 1 era `notificarObservadores()`.)* |
| `Posicion getPosicionNave()` | Devuelve la posición actual de la nave. Al construir el tablero, comienza en `(x / 2, y / 2)`. |
| `void moverNave(Posicion posicion)` | Sitúa la nave en la casilla indicada y notifica a los observadores. La posición debe ser válida. El método no comprueba los límites. |
| `ElementoDelUniverso inspeccionarPosicion()` | Devuelve el elemento ubicado en la posición actual de la nave, o `null` si la casilla está vacía. |
| `ElementoDelUniverso[][] observarAlrededores()` | Devuelve una matriz nueva de 3 por 3 con los elementos del vecindario de la nave. La nave queda en `[1][1]`. Las filas y columnas avanzan en el mismo sentido que las coordenadas del tablero. Las casillas vacías y las posiciones fuera del tablero contienen `null`. |
| `Posicion obtenerObjetivo()` | Elige y guarda una posición objetivo en una casilla sin elementos. |
| `ElementoDelUniverso[][] verTablero()` | Devuelve una matriz nueva de dimensiones `x` por `y`. La primera dimensión representa filas y la segunda columnas, se accede como `matriz[fila][columna]`. Las casillas vacías contienen `null`. La matriz no incluye la nave ni el objetivo. |

`Posicion` representa coordenadas inmutables y es el tipo usado por las operaciones de movimiento y objetivo. Sus coordenadas coinciden con los índices de la matriz devuelta por `verTablero()`.

`Observable` es una clase abstracta que mantiene el registro de observadores y ofrece los métodos `agregarObservador`, `quitarObservador` y `notificarObservadores`. `Tablero` extiende esta clase. `Observador` define el método `actualizar(String mensaje)` (en la versión 1, `actualizar()`). El tablero notifica a sus observadores al mover la nave y al finalizar cada turno de `Destino`.

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

## Versión 2

Por solicitud de algunos grupos, se actualizaron ciertos comportamientos del módulo. Los demás grupos pueden mantener su funcionamiento con el sistema anterior (la versión que ya tienen descargada) o integrar esta nueva versión.

### Notificaciones con mensaje

Las notificaciones a los observadores ahora incluyen un mensaje que describe lo ocurrido. Los métodos sin parámetros de la versión 1 se reemplazan por:

| Versión 1 | Versión 2 |
| --- | --- |
| `Observable.notificarObservadores()` | `Observable.notificarObservadores(String mensaje)` |
| `Observador.actualizar()` | `Observador.actualizar(String mensaje)` |

Quien integre la versión 2 debe implementar `actualizar(String mensaje)` en sus observadores en lugar de `actualizar()`.

El tablero envía estos mensajes:

| Evento | Mensaje |
| --- | --- |
| `moverNave(posicion)` | `La nave se movió a la posición (x, y).` |
| `obtenerObjetivo()` | `Se estableció el objetivo en la posición (x, y).` |
| Fin de `turnoDestino()` | `Finalizó el turno de Destino: se movieron M de N elementos seleccionados.` |

### Acceso público al objetivo

`getObjetivo()` de `Tablero` pasa de `protected` a `public`:

| Método | Comportamiento |
| --- | --- |
| `Posicion getObjetivo()` | Devuelve el objetivo actual sin volver a elegirlo, o `null` si todavía no se eligió uno. No modifica el tablero. |

A diferencia de `obtenerObjetivo()`, este método no elige un objetivo nuevo ni notifica a los observadores.

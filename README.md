# Programación multimedia y dispositivos móviles - Actividad de Aprendizaje 2 (AA2)
Videojuego de género de plataformas desarrollado con la librería LibGDX para la asignatura de Programación multimedia y dispositivos móviles del 2º curso de DAM.


## Requisitos para la realización de la actividad
Se pide una serie de requisitos a cumplir para desarrollar la actividad. Esta consiste en el desarrollo de un videojuego 2D o 3D utilizando la librería libGDX. 

Se proponen una serie de Requisitos obligatorios que deben ser implementados y otra serie 
de funcionalidades de entre las que se puede elegir hasta 5.

La temática del juego es de libre elección y se podrán usar tanto elementros gráficos como de audio que no hayan sido creados por uno mismo.

Hay que tener en cuenta que se considera indispensable que el juego se programe siguiendo 
el paradigma de Programación Orientado a Objetos.

### Requisitos obligatorios
✅ 1. El videojuego debe contar con un personaje principal que el jugador podrá manejar y 
debe tener un inicio, final y un objetivo claro. Preparar, al menos, dos niveles 
claramente diferenciados.

✅ 2. El videojuego mostrará información en pantalla al usuario (puntuación, energía, 
nivel actual) y ésta se actualizará cuando corresponda.

✅ 3. Utilizar menús para iniciar/configurar/terminar la partida y mostrar las instrucciones 
del juego. Hay que tener en cuenta que se deberá poder jugar otra partida sin 
necesidad de salir del juego. Añadir, al menos, dos opciones configurables.

✅ 4. El juego dispondrá de características de sonido y animaciones en todos los 
caracteres con movimiento.

✅ 5. El juego dispondrá de NPCs (Non-Playable Characters) que deberán interactuar con 
el personaje del jugador (al menos deberá haber 3 NPCs diferentes).

### Requisitos opcionales
✅ 1. Subir el videojuego como proyecto a un repositorio de GitHub y gestionar el 
proyecto con dicha herramienta (gestionar algunas issues, crear alguna release, 
editar la wiki con las instrucciones del juego).

✅ 2. Al finalizar la partida se almacenará la puntuación del jugador (con su nombre) y se 
mostrará el tipo 10 puntuaciones.

✅ 3. Es posible mostrar/ocultar un menú durante el juego con opciones: 
activar/desactivar sonido, volver al menú principal, salir del juego y continuar con 
la partida.

✅ 4. Añadir dos niveles más al juego.

✅ 5. Crear un generador de niveles (utilizando TiledMap o cualquier otro fichero de 
entrada) de forma que sea posible incorporar niveles nuevos para el juego de forma 
sencilla sin tocar el código.

⬜ 6. Añadir algún tipo de IA a algún NPC del juego.

⬜ 7. Añadir opciones de multijugador (al menos 2 jugadores) en local o en red.

⬜ 8. Añadir 3 nuevos tipos de NPCs, también diferentes.

⬜ 9. Guardar y cargar partidas para continuar donde se dejó el juego.

⬜ 10. Modificar el comportamiento o características del jugador durante el juego (más 
energía, más vidas, mejor armamento, más velocidad, ...) de forma permanente o 
temporal (powerups). Añadir al menos 3 powerups diferentes.

⬜ 11. El usuario podrá escoger entre diferentes personajes del juego (al menos tres) con 
características diferentes.

⬜ 12. Trasladar el juego a más de una plataforma (PC, móvil, web, ...).

⬜ 13. Utiliza un motor de físicas: Box2D.

## Requisitos para la creación de nuevos niveles
Para la creación de nuevos niveles se ha utilizado Tiled, un editor de mapas 2D. Por lo que para la creación de nuevos niveles es necesario tener instalado Tiled.

Facilitando la creación de nuevos niveles, se ha creado un mapa de ejemplo en `core/assets/sampleLevel.tmx`. Este mapa contiene todas las capas necesarias para la creación de nuevos niveles así como todos objetos y personajes necesarios para la ejecución del juego. Todos los elementos tienen los atributos necesarios para su funcionamiento añadido para indicar de que forma se deben emplear.

Este mapa de ejemplo tiene asignadas las rutas relativas de los ficheros `.tsx`, de los que se extraen los elementos gráficos, apuntando a la carpeta de `core/assets/levels/` por lo que si se quiere comenzar un la creación de un nuevo nivel, se recomienda copiar este mapa a la carpeta de `core/assets/levels/` y renombrarlo. Tras esto, al abrirlo con Tiled, aparecerá un mensaje de error indicando que no se encuentran los ficheros `.tsx` pero basta con darle a localizar y seleccionar cada uno de los 3 que entonces compartirán carpeta en `core/assets/levels/`. Tras esto, se podrá comenzar a crear un nuevo nivel.

## Requisitos para la ejecución de un nivel
En el fichero `.tmx` de un nivel, se deben encontrar las siguientes capas y objetos mínimo para su correcto funcionamiento como se puede observar en el `sampleLevel.tmx`:
- foreground: Capa de patrones que es dibujada por encima de los objetos y el fondo. En esta capa se dibujan los elementos que se utilizan para ocultar trampas, enemigos, coleccionables, etc.
- exit: Capa de objetos que contiene un único objeto que indica la salida del nivel. Este objeto puede ser cualquiera de los 2 sprites de puerta que se encuentran en el `tilemap_packed.tsx` ya que los 2 tienen el atributo type=exit.
- items: Capa de objetos que contiene los objetos coleccionables del nivel. Estos objetos pueden ser monedas o llave (que preferiblemente debe ser única por nivel). Estos objetos deben tener el atributo type=coin o type=key respectivamente.
- enemies: Capa de objetos que contiene los enemigos del nivel. Estos objetos deben tener el atributo enemy=fly, enemy=tank o enemy=fish.
  - fly: Enemigo volador que se mueve inicialmente hacia la derecha y recorre la distancia especificada en el atributo distance. Al llegar al final de su recorrido, cambia de dirección y vuelve a recorrer la distancia especificada hasta su punto de inicio. El atributo distance es un número entero que indica el número de tiles (18x18) o casillas que recorre el enemigo.
  - tank: Enemigo terrestre que se mueve inicialmente hacia la derecha y recorre la distancia especificada en el atributo distance. Al llegar al final de su recorrido, cambia de dirección y vuelve a recorrer la distancia especificada hasta su punto de inicio. El atributo distance es un número entero que indica el número de tiles (18x18) o casillas que recorre el enemigo.
  - fish: Enemigo acuático que se mueve verticalmente. De manera opcional, se puede especificar el atributo jumpSpeed que indica la velocidad de salto del enemigo. Si no se especifica, el enemigo saltará a una velocidad por defecto. El atributo jumpSpeed es de tipo float.
- player: Capa de objetos que contiene el objeto jugador. Este objeto debe tener el atributo player=1 siendo este atributo de tipo string.
- ground: Capa de patrones que contiene el suelo y obstáculos del nivel. Esta capa es la que se utiliza para detectar las colisiones del jugador con el suelo y los enemigos. En esta capa se deben encontrar los patrones que se utilizan para crear el suelo del nivel. Estos patrones deben tener el atributo de noombre ground y falor string cualquiera.
- decoration: Capa de patrones que contiene los elementos decorativos del nivel. Esta capa es la que se utiliza para dibujar los elementos decorativos del nivel.
- death: Capa de patrones que contiene los elementos que matan al jugador. Los patrones deben tener el atributo de nombre death y valor string cualquiera. Esta capa es la que se utiliza para detectar las colisiones del jugador con los elementos que matan al jugador y no son enemigos animados.
- background: Capa de patrones que contiene el fondo del nivel. Esta capa es la que se utiliza para dibujar el fondo del nivel.

Cuando se ha diseñado el nuevo nivel, se debe añadir el nombre del fichero `.tmx` en el fichero `core/assets/levels/levels.txt` para que el juego lo reconozca y lo cargue. Este fichero contiene una lista de los niveles disponibles en el juego. Cada línea del fichero debe contener el nombre del fichero `.tmx` del nivel. El orden de los niveles en el fichero es el orden en el que se cargarán en el juego. Por lo que si se quiere cambiar el orden de los niveles, basta con cambiar el orden de las líneas del fichero.

## Requisitos para la ejecución del juego
Se debe tener instalado Java 17 o superior y Gradle 7.0 o superior. Para la ejecución del juego, basta con ejecutar el siguiente comando en la raíz del proyecto:
```bash
./gradlew lwjgl3:run
```
Esto ejecutará el juego en la plataforma de escritorio.

## Exportación del juego
Para exportar el juego a la plataforma de escritorio, basta con ejecutar el siguiente comando en la raíz del proyecto:
```bash
./gradlew lwjgl3:jar
```
Esto generará un fichero `.jar` en la carpeta `lwjgl3/build/libs/` que se puede ejecutar con el siguiente comando:
```bash
java -jar lwjgl3/build/libs/aa2pmdm-1.0.0.jar
```
Aunque también se puede ejecutar directamente desde la carpeta `lwjgl3/build/libs/` haciendo doble clic en el fichero `.jar`.

## Instrucciones de juego
El juego se controla con las teclas de dirección y la barra espaciadora. Las teclas de dirección se utilizan para mover al jugador y la barra espaciadora se utiliza para saltar. El jugador puede recoger monedas y llaves para aumentar su puntuación y abrir la puerta final del nivel para avanzar al siguiente. El objetivo del juego es llegar a la puerta de salida de cada nivel muriendo menos de 3 veces y recogiendo el mayor número de monedas posible en el menor tiempo posible. Si se el jugador se queda sin vidas en el nivel, el juego deberá ser comenzado desde el principio (PRIMER NIVEL 😈). Al tratarse de niveles independientes y de la dificultad que plantea el juego, las puntuaciones serán reiniciadas para cada nivel y si se completan todos ellos, se permitirá al jugador registrar su puntuación total y verificar si se encuentra entre los 10 mejores.

## Créditos
- **Desarrollador**: Javier Sanz Moliné
- **Assets**: 
  - [Kenney](https://kenney.nl/assets/pixel-platformer)
  - [OpenGameArt](https://opengameart.org/)
  - Javibu13

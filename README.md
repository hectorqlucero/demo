# demo
Una libreria que genera una pagina web full stack

## Requisitos antes de Instalar
1. Java sdk instalado
2. MySQL instalado y configurado con una contraseña
3. Leiningen instalado. https://leiningen.org
4. vscode instalado con la extension calva:clojure

## Como usar la libreria
1. Clonar este repositorio en su maquina
2. Crear una base de datos en su cliente favorito de mysql
3. Renombrar resources/private/config_example a config.clj
4. COnfigurar project.clj. Cambiar donde diga 'Change me' con tu configuración
5. Configurar todas las 'xxxxx' con la informacion correcta
6. Click en el icono de la barra de estado abajo 'REPL' para iniciar un repl
7. Abrir una terminal nueva y correr: 
..* `lein with-profile dev run`
8. Abrir otra terminal nueva y correr:
..* `lein migrate`
..* `lein database`
   ..* Creara un usuario user@gmail.com password user
   ..* Creara un usuario admin@gmail.com password admin
   ..* Creara un usuario sistema@gmail.com password sistema
9. Correr la pagina:
  ..* `http:localhost:3000` en tu browser favorito
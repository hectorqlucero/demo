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
3. Configurar **project.clj**
4. Renombrar **resources/private/config_example** a **config.clj**
5. Configurar **project.clj**. Cambiar donde diga **Change me** con tu configuración
6. Configurar todas las **xxxxx** con la informacion correcta
7. Click en el icono de la barra de estado abajo **REPL** para iniciar un repl. Usar la opción **Start your project with a REPL and connect (a.k.a. jack-in)**
8. Abrir una terminal nueva y correr: 
   * `lein with-profile dev run`
9. Abrir otra terminal nueva y correr:
   * `lein migrate`
      * Creara la tabla **users**
   * `lein database`
      * Creara un usuario user@gmail.com password **user** en la tabla **users**
      * Creara un usuario admin@gmail.com password **admin** en la tabla **users**
      * Creara un usuario sistema@gmail.com password **sistema** en la tabla **users**
10. Correr la pagina:
    * `http:localhost:3000` en tu browser favorito
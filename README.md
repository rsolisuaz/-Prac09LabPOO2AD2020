# PRÁCTICA 9. IMPLEMENTACIÓN DE SERVICIOS REST para institucion y municipio.

## PREREQUISITOS PARA ESTA PRACTICA
1. Tener instalado el Glassfish 5.1.0, que se puede bajar de [Página de Glassfish](https://projects.eclipse.org/projects/ee4j.glassfish/downloads) (la versión Full Profile)
2. Tener instalada la versión 8 de Java (Glassfish solo funciona para esa versión), que se puede bajar de [Página de Java 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
3. Configurar IntellijIDEA para usar Glassfish
4. Para realizar las pruebas desde Github será necesario tener ngrok, el cual se puede bajar de [Página de ngrok](https://dashboard.ngrok.com/get-started/setup), además de haberlo configurado de acuerdo a lo que se verá en la sesión del 26 de octubre.

## COPIA DEL REPOSITORIO REMOTO EN SU COMPUTADORA LOCAL

Como primer paso, será necesario crear una copia local del repositorio remoto creado en Github al aceptar la tarea. Para ello, es necesario hacer los siguientes pasos:
1. Entrar a la página cuyo URL les fue proporcionado al aceptar la tarea, en tal página dé click en el botón Code y copie el URL que aparece en el cuadro de texto de nombre **Clone with HTTPS** (comienza con *https://*)
2. Abre IntelliJ IDEA e indica que harás un clon local de tu repositorio:
   - Si no tienes ningún repositorio abierto selecciona la opción **Get From Version Control** de la Ventana de Bienvenida, o si tienes un proyecto abierto, puedes entrar al menú **VCS** y seleccionar la opción **Get From Version Control**
   - En el cuadro de diálogo que aparece:
     - Selecciona Git
     - Pega el URL que copiaste en el paso 1  en el cuadro de texto **URL**
     - Selecciona en Directory la carpeta donde lo colocarás, es importante que crees una nueva carpeta o se colocará (da click en el icono de carpeta , navega a donde lo quieres colocar y da click en el icono de New Folder para crear una nueva carpeta)
     - Da click en **Clone**
     - Si te pide usuario y clave de Github proporciona esos datos
     - Después de unos segundos tendrás listo tu un clon de tu repositorio listo para trabajar en Intellij IDEA


## MODIFICACIÓN DEL CÓDIGO PROPORCIONADO

Una vez que tengas el repositorio local, el trabajo principal consiste en crear las clases para proporcionar los servicios REST correspondientes a las tablas **institucion** y **municipio** de la base de datos **controlconcursos**, tales servicios serán representados por las clases InstitucionREST y MunicipioREST, ambas a colocarse en el paquete **mx.edu.uaz.ingsoftware.poo2.servicios**. Estas clases heredarán de la clase **RESTAbstracto<T>** la cual ya implementa la funcionalidad básica de la interface **DaoWeb<T>** (que es una interface muy similar a la interface **Dao<T>** usada en el Programa 1, pero con cambios para adaptarse a la forma en que regresa los resultados). En el método **agregaRecursosREST** de la clase **AplicacionConfig** deberá ponerse código para agregar las clases que representan a los servicios REST al Set que recibe como argumento.

La ruta para el servicio de institucion deberá ser ***urlbase*/institucion** y para el servicio municipio deberá ser ***urlbase*/municipio/{ide}** donde {ide} representa que se dará un id de entidad de la cual se desean los municipios.

Las operaciones para agregar, actualizar o eliminar alguna institución deberán primero validar si es posible realizar la operación solicitada, de acuerdo a las reglas indicadas en el Programa 1, si no es posible, regresará "false".  

Antes de hacer los cambios en el código haga las siguientes modificaciones:

1. En el archivo index.html, incluido dentro de la subcarpeta **webapp** en la sección **main** del proyecto, deberá sustituir las NNNNNNNNNNNNN por su nombre completo y las XXXXXXXX por su matrícula, lo cual también deberá realizar en el archivo **datosmysql.properties** ubicado dentro de la subcarpeta **resources** en la sección **main**
2. En el archivo build.gradle modifique el valor de version para que se use su matrícula

## CALIFICACIÓN

Habrá un archivo para realizar las pruebas dentro de la sección **test**, que es la clase **testServiciosREST**, la cual deberá irse actualizando conforme lo indique el profesor, el incluido solo hace un par de pruebas. La calificación será mostrada al final de la prueba. Ese archivo será ejecutado de manera automática en el Github al hacer push, aunque debido a la naturaleza del proyecto, el servidor Glassfish deberá estar corriendo en su computadora local y Github se comunicará con su servidor Glassfish y MySQL  por lo cual será necesario que tenga corriendo ngrok de acuerdo a lo indicado en la sesión del 26 de octubre y deberá haber colocado los datos correspondientes para que desde Github se pueda hacer conexión a su computadora cambiando los datos del archivo **datosmysql.properties**


Para ejecutar las pruebas de tu programa en tu computadora local selecciona **testServiciosREST** (asegurándote que los datos del archivo **datosmysql.properties** indiquen que la conexión será local) dentro de src/test/java, dale click con el botón derecho y selecciona **Run** (la opción tendrá un triángulo verde ), te mostrará el resultado de las pruebas y la calificación obtenida
OS EN LO RELACIONADO A LA TABLA INSTITUCION** 

## NOTAS IMPORTANTES
1. Las clases que representan a los servicios REST deben tener un constructor vacío que manda llamar al constructor de la clase padre pasándole como referencia la clase de entidad con la que se trabajará.
2. Deben tener un atributo privado de tipo EntityManager que tendrá la anotación siguiente: **@PersistenceContext(unitName = "default")**
3. El método getEntityManager (que debe implementar) regresará simplemente el objeto EntityManager que tiene como atributo
4. Solo implementará los métodos que requiera hacer para la tabla para la cual está haciendo los servicios REST con las anotaciones adecuadas (GET,POST,PUT,DELETE, si consume información de que tipo, si genera información de que tipo y las rutas asociadas)

5. Cada vez que completes un método hazle deploy al servicio y ejecuta la prueba para verificar que las pruebas del método completado son exitosas

6. Una vez vez que completes un método y verifiques que pasa las pruebas haz un Commit : 
   - Para hacer commit, selecciona primero el proyecto, después entra al menú VCS y selecciona la opción Commit...
   - Te aparecerá una lista de archivos con los cambios detectados, verifica que incluye todos los archivos que deberían estar
   - Teclea un mensaje que describa los cambios realizados de manera clara y concisa (debe ser un mensaje que permita a otras personas darse cuenta de lo realizado)
   - Dé click en el ícono del engrane (Show Commit Options), escriba su nombre en el cuadro Author, deseleccione Perform Code Analysis y Check TODO (Esto es necesario solo en el primer commit que hagan)
   - Dé click en Commit

7. Después de haber hecho todos los commits que completan tu programa, súbelo al repositorio remoto haciendo lo siguiente:
   - Entre al menú VCS y seleccione la opción Git->Push...
   - Dé click en el botón Push en el cuadro de diálogo que aparece

8. Cada vez que subas tu proyecto al repositorio privada con un push, se aplicarán automáticamente las pruebas sobre tu código para verificar si funciona correctamente. Recuerda que en la página de tu repositorio en la sección **Pull Requests**, se encuentra una subsección de nombre **Feedback**, donde podrás encontrar los resultados de las pruebas en la pestaña denominada **Check** (expandiendo la parte que dice **Run education/autograding@v1**), y cualquier comentario general que el profesor tenga sobre tu código en la pestaña **Conversation**. 

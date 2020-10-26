package mx.edu.uaz.ingsoftware.poo2.servicios;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Esta clase se encarga de dar de alta los servicios que contiene el
 * proyecto indicando con @ApplicationPath la ruta a seguir para llegar
 * a los servicios
 */
@ApplicationPath("servicios")
public class AplicacionConfig extends Application {

    /**
     * Este metodo devuelve  las clases de servicios REST que se
     * proporcionaran por este proyecto
     * @return Set de objetos Class que representan los servicios disponibles
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> recursos = new java.util.HashSet<>();
        agregaRecursosREST(recursos);
        return recursos;
    }

    /**
     * Este metodo agrega al argumento recibido las clases de servicios
     * REST que estaran disponibles en el proyecto
     * @param recursos Set de objetos Class donde deben agregarse las clases de servicio
     */
    private void agregaRecursosREST(Set<Class<?>> recursos) {

    }

}

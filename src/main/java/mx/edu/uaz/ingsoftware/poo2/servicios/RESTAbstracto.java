package mx.edu.uaz.ingsoftware.poo2.servicios;

import mx.edu.uaz.ingsoftware.poo2.interfaces.DaoWeb;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.List;

/**
 * Esta clase implementa de manera abstracta la interface DaoWeb
 * para poder ser usado de manera generica por los diversos servicios
 * REST
 * @param <T>  Clase de Entidad con la que se trabajara
 */
public abstract class RESTAbstracto<T> implements DaoWeb<T> {
    private final Class<T> tipoClaseEntidad;

    /**
     * Constructor de la clase que recibe el tipo de clase de Entidad
     * para la cual se proporciona el servicio REST
     * @param tipoEntidad Clase de entidad con la que se trabajara
     */
    public RESTAbstracto(Class<T> tipoEntidad) {
        this.tipoClaseEntidad = tipoEntidad;
    }

    /**
     * Este metodo debe ser implementado por los REST a generar
     * y debe devolver el EntityManager que contiene
     * @return Objeto EntityManager de la clase concreta creada
     */
    protected abstract EntityManager getEntityManager();

    @Override
    public T get(Object id) {
        return getEntityManager().find(tipoClaseEntidad,id);
    }

    @Override
    public List<T> getAll() {
        CriteriaQuery cq=getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(tipoClaseEntidad));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Este metodo es para uso en depuracion y permite determinar
     * las violaciones a las restricciones que tuviera un objeto
     * a ser guardado o actualizado
     * @param ex  Excepcion de Violacion de Restricciones que se genero al guardar/actualizar
     */
    private void imprimeErroresValidacion(ConstraintViolationException ex) {
        System.err.println("Se encontraron los siguientes errores de validacion:");
        for (Iterator<ConstraintViolation<?>> it=ex.getConstraintViolations().iterator(); it.hasNext();) {
            ConstraintViolation<?> error=it.next();
            String causaError= error.getMessage();
            String enAtributo=error.getPropertyPath().toString();
            System.err.printf("Error en el atributo %s: %s\n",enAtributo,causaError);
        }
    }

    @Override
    public String save(Object id, T dato) {
        String resultado="false";
        try {
            if (get(id)==null) {
                getEntityManager().persist(dato);
                resultado = "true";
            }
        }
        catch (ConstraintViolationException ex) {
            // Datos invalidos de acuerdo a la definicion de la tabla
            imprimeErroresValidacion(ex);
        }
        catch (Exception ignored) {
            // En caso de requerir depurar descomente la siguiente linea
            //ignored.printStackTrace();
        }
        return resultado;
    }

    @Override
    public String update(Object id, T dato) {
        String resultado="false";
        EntityManager em=getEntityManager();
        try {
            if (get(id)!=null) {
                em.merge(dato);
                resultado="true";
            }
        }
        catch (ConstraintViolationException ex) {
            imprimeErroresValidacion(ex);
        }
        catch (Exception ignored) {
            // En caso de requerir depurar descomente la siguiente linea
            //ignored.printStackTrace();
        }
        return resultado;
    }

    @Override
    public String delete(Object id) {
        String resultado="false";
        EntityManager em=getEntityManager();
        try {
            T objeto= em.find(tipoClaseEntidad,id);
            if (objeto!=null) {
                em.remove(objeto);
                resultado="true";
            }
        }
        catch (Exception ignored) {
            // En caso de requerir depurar descomente la siguiente linea
            //ignored.printStackTrace();
        }
        return resultado;
    }
}

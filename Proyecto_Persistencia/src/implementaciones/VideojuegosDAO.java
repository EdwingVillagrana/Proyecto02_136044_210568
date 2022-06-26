/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementaciones;

import entidades.Videojuego;
import excepciones.PersistenciaException;
import interfaces.IConexionBD;
import interfaces.IVideojuegosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author edw_v
 */
public class VideojuegosDAO implements IVideojuegosDAO {

    private final IConexionBD conexionBD;

    /**
     * Constructor que inicializa la instancia de tipo IConexionBD al valior del
     * parámetro recibido.
     *
     * @param conexionBD parámetro de tipo conexionBD usado para inicializar la
     * instancia de la clase.
     */
    public VideojuegosDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public void agregar(Videojuego videojuego) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();

            em.persist(videojuego);

            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible agregar el nuevo videojuego");
        }
    }

    @Override
    public void actualizar(Videojuego videojuego) throws PersistenciaException {
        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            entityManager.getTransaction().begin();

            Videojuego videojuegoGuardado = consultarVideojuegoPorId(videojuego.getId());
            if (videojuegoGuardado == null) {
                throw new PersistenciaException("No se encontró el videojuego que quiere actualizar");
            }
            videojuegoGuardado.setNombre(videojuego.getNombre());
            videojuegoGuardado.setDesarrolladora(videojuego.getDesarrolladora());
            videojuegoGuardado.setPrecio(videojuego.getPrecio());
            videojuegoGuardado.setStock(videojuego.getStock());
            entityManager.merge(videojuegoGuardado);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("Problema al intentar actualizar el videojuego");
        }
    }

    @Override
    public void eliminar(Videojuego videojuego) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();

            Videojuego videojuegoGuardado = consultarVideojuegoPorId(videojuego.getId());
            if (videojuego == null) {
                throw new Exception("No se encontró el videojuego que desea eliminar");
            }
            em.remove(videojuego);

            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("Problema al intentar eliminar videojuego");
        }
    }

    @Override
    public Videojuego consultarVideojuegoPorId(Long id) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            
            Videojuego videojuego = em.find(Videojuego.class, id);
            
            em.getTransaction().commit();
            return videojuego;
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar el videojuego");
        }
    }

    @Override
    public List<Videojuego> consultarPorNombre(String nombre) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Videojuego> criteria = builder.createQuery(Videojuego.class);
            
            //INICIO CONFIGURACIONES DE CONSULTA
            Root<Videojuego> entidad = criteria.from(Videojuego.class);
            criteria.where(builder.like(entidad.get("nombre"), nombre));
            //FIN CONFIGURACIONES DE CONSULTA
            
            TypedQuery<Videojuego> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar el videojuego");
        }
    }

    @Override
    public List<Videojuego> consultarTodos() throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Videojuego> criteria = builder.createQuery(Videojuego.class);            
            TypedQuery<Videojuego> query = em.createQuery(criteria);
            
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar la lista de videojuegos");
        }
    }

}

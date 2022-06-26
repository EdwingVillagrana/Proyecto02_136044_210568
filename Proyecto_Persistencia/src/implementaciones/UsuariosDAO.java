/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementaciones;

import entidades.Usuario;
import excepciones.PersistenciaException;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;
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
 * @author Edwing Villagrana_Arturo Luna
 */
public class UsuariosDAO implements IUsuariosDAO {

    private final IConexionBD conexionBD;

    /**
     * Constructor que inicializa la instancia de tipo IConexionBD al valior del parámetro recibido.
     * @param conexionBD parámetro de tipo conexionBD usado para inicializar la instancia de la clase.
     */
    public UsuariosDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Agrega un nuevo usuario a la base de datos.
     * @param usuario parámetro con los datos del nuevo usuario a registrar.
     * @throws PersistenciaException lanza una excepción en caso de no poder agregar el registro en la base.
     */
    @Override
    public void agregar(Usuario usuario) throws PersistenciaException {

        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            
            em.persist(usuario);
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible agregar al nuevo usuario");
        }
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     * @param usuario objeto con los nuevos datos a guardar en el usuario seleccionado.
     * @throws PersistenciaException lanza una excepción en caso de no poder actualizar el registro en la base.
     */
    @Override
    public void actualizar(Usuario usuario) throws PersistenciaException {

        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            entityManager.getTransaction().begin();

            Usuario usuarioGuardado = consultarUsuarioPorId(usuario.getId());
            if (usuarioGuardado == null) {
                throw new PersistenciaException("No se encontró al usuario que quiere actualizar");
            }
            usuarioGuardado.setNombre(usuario.getNombre());
            usuarioGuardado.setTelefono(usuario.getTelefono());
            entityManager.merge(usuarioGuardado);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("Problema al intentar actualizar el usuario");
        }
    }

    /**
     * Elimina un usuario dentro de la base.
     * @param usuario objeto que contienen la información a buscar en la base para ser eliminada.
     * @throws PersistenciaException lanza una excepción en caso de no poder eliminar el registro en la base.
     */
    @Override
    public void eliminar(Usuario usuario) throws PersistenciaException {

        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();

            Usuario usuarioGuardado = consultarUsuarioPorId(usuario.getId());
            if (usuarioGuardado == null) {
                throw new Exception("No se encontró al usuario que desea eliminar");
            }
            em.remove(usuarioGuardado);

            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("Problema al intentar eliminar usuario");
        }
    }

    /**
     * Busca en la base de datos un registro con un id en específico.
     * @param id id específico a buscar en la base de datos.
     * @return retorna un objeto de tipo usuario con los datos del registro en caso de encontrarlo, null en caso contrario.
     * @throws PersistenciaException lanza una excepción en caso de no poder realizar la consulta dentro de la base.
     */
    @Override
    public Usuario consultarUsuarioPorId(Long id) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            
            Usuario usuario = em.find(Usuario.class, id);
            
            em.getTransaction().commit();
            return usuario;
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar el usuario");
        }
    }
    
    /**
     * Busca en la base de datos un registro con un nombre en específico.
     * @param nombre nombre específico a buscar en la base de datos.
     * @return retorna una lista de tipo usuario con los datos de los registros en caso de encontrarlos, null en caso contrario.
     * @throws PersistenciaException lanza una excepción en caso de no poder realizar la consulta.
     */
    public List<Usuario> consultarUsuarioPorNombre(String nombre) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
            
            //INICIO CONFIGURACIONES DE CONSULTA
            Root<Usuario> entidad = criteria.from(Usuario.class);
            criteria.where(builder.like(entidad.get("nombre"), nombre));
            //FIN CONFIGURACIONES DE CONSULTA
            
            TypedQuery<Usuario> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar el usuario");
        }
    }

    @Override
    public List<Usuario> consultarTodos() throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);            
            TypedQuery<Usuario> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar la lista de usuarios");
        }
    }
}

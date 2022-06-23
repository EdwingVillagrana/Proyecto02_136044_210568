/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementaciones;

import entidades.Usuario;
import excepciones.PersistenciaException;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;
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
public class UsuariosDAO implements IUsuariosDAO {

    private final IConexionBD conexionBD;

    public UsuariosDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

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

    @Override
    public void actualizar(Usuario usuario) throws PersistenciaException {

        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            entityManager.getTransaction().begin();

            Usuario usuarioGuardado = entityManager.find(Usuario.class, usuario.getId());
            if (usuarioGuardado == null) {
                throw new PersistenciaException("No se encontró al usuario que quiere actualizar");
            }
            usuarioGuardado.setNombre(usuario.getNombre());
            usuarioGuardado.setTelefono(usuario.getTelefono());
            entityManager.persist(usuarioGuardado);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("Problema al intentar actualizar el usuario");
        }
    }

    @Override
    public void eliminar(Usuario usuario) throws PersistenciaException {

        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            entityManager.getTransaction().begin();

            Usuario usuarioGuardado = entityManager.find(Usuario.class, usuario.getId());
            if (usuarioGuardado == null) {
                throw new Exception("No se encontró al usuario que desea eliminar");
            }
            entityManager.remove(usuarioGuardado);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("Problema al intentar eliminar usuario");
        }
    }

    @Override
    public Usuario consultarUsuarioPorId(Long id) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
            
            
            //INICIO CONFIGURACIONES DE CONSULTA
            Root<Usuario> entidad = criteria.from(Usuario.class);
            criteria.where(builder.equal(entidad.get("id"), id));
            //FIN CONFIGURACIONES DE CONSULTA
            
            TypedQuery<Usuario> query = em.createQuery(criteria);
            return query.getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar el usuario ");
        }
    }
}

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
            EntityManager entityManager = this.conexionBD.crearConexion();
            Usuario usuario = entityManager.find(Usuario.class, id);
            return usuario;
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar el usuario ");
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementaciones;

import entidades.Compra;
import entidades.Usuario;
import excepciones.PersistenciaException;
import interfaces.IComprasDAO;
import interfaces.IConexionBD;
import java.util.Calendar;
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
public class ComprasDAO implements IComprasDAO {

    private final IConexionBD conexionBD;

    public ComprasDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public void agregar(Compra compra) throws PersistenciaException {

        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(compra);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(ComprasDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible agregar la compra");
        }
    }

    @Override
    public List<Compra> consultarTodos() throws PersistenciaException {

        try {
            EntityManager em = this.conexionBD.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Compra> criteria = builder.createQuery(Compra.class);
            TypedQuery<Compra> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger(ComprasDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new PersistenciaException("No se pudo consultar la lista de compras");
        }
    }

    @Override
    public List<Compra> consultarPorUsuario(Usuario usuario) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Compra> criteria = builder.createQuery(Compra.class);

            //INICIO CONFIGURACIONES DE CONSULTA
            Root<Compra> entidad = criteria.from(Compra.class);
            criteria.where(builder.equal(entidad.get("id"), usuario.getId()));
            //FIN CONFIGURACIONES DE CONSULTA

            TypedQuery<Compra> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger(ComprasDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new PersistenciaException("No se pudo consultar la lista de compras de este usuario");
        }
    }

    @Override
    public List<Compra> consultarPorPeriodo(Calendar fechaInicio, Calendar fechaFin) throws PersistenciaException {

        try {
            EntityManager em = this.conexionBD.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Compra> criteria = builder.createQuery(Compra.class);

            //INICIO CONFIGURACIONES DE CONSULTA
            Root<Compra> entidad = criteria.from(Compra.class);
            criteria.where(builder.between(entidad.get("fechaCompra"), fechaInicio, fechaFin));
            //FIN CONFIGURACIONES DE CONSULTA

            TypedQuery<Compra> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger(ComprasDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new PersistenciaException("No se pudo consultar la lista de compras de este periodo");
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package implementaciones;

import entidades.DetallesCompra;
import excepciones.PersistenciaException;
import interfaces.IConexionBD;
import interfaces.IDetallesCompraDAO;
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
public class DetallesComprasDAO implements IDetallesCompraDAO{

    private final IConexionBD conexionBD;

    public DetallesComprasDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    @Override
    public void agregar(DetallesCompra detallesCompra) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(detallesCompra);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(DetallesComprasDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible agregarle detalles a la compra");
        }
    }

    @Override
    public List<DetallesCompra> consultarPorIdCompra(Long id) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<DetallesCompra> criteria = builder.createQuery(DetallesCompra.class);
            
            //INICIO CONFIGURACIONES DE CONSULTA
            Root<DetallesCompra> entidad = criteria.from(DetallesCompra.class);
            criteria.where(builder.equal(entidad.get("compra"), id));
            //FIN CONFIGURACIONES DE CONSULTA
            
            TypedQuery<DetallesCompra> query = em.createQuery(criteria);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se pudo consultar el usuario");
        }
    }

}

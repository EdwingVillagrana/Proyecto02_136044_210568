/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Compra;
import entidades.Usuario;
import excepciones.PersistenciaException;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author edw_v
 */
public interface IComprasDAO {
    public void agregar (Compra compra) throws PersistenciaException;
    public List<Compra> consultarTodos()throws PersistenciaException;
    public List<Compra> consultarPorUsuario(Usuario usuario) throws PersistenciaException;
    public List<Compra> consultarPorPeriodo(Calendar fechaInicio, Calendar fechaFin) throws PersistenciaException;
}

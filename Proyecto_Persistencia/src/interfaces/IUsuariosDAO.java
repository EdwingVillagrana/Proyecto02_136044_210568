/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Usuario;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author edw_v
 */
public interface IUsuariosDAO {
    public void agregar (Usuario usuario) throws PersistenciaException;
    public void actualizar(Usuario usuario) throws PersistenciaException;
    public void eliminar(Usuario usuario) throws PersistenciaException;
    public Usuario consultarUsuarioPorId(Long id)throws PersistenciaException;
    public List<Usuario> consultarUsuarioPorNombre(String nombre) throws PersistenciaException;
}

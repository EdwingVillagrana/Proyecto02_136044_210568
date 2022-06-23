/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import entidades.Videojuego;
import excepciones.PersistenciaException;

/**
 *
 * @author edw_v
 */
public interface IVideojuegosDAO {
    public void agregar (Videojuego videojuego) throws PersistenciaException;
    public void actualizar(Videojuego videojuego) throws PersistenciaException;
    public Videojuego consultarVideojuegoPorId(Long id)throws PersistenciaException;
    public void eliminar(Videojuego videojuego) throws PersistenciaException;
}

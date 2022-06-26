/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import entidades.Compra;
import entidades.DetallesCompra;
import entidades.Usuario;
import entidades.Videojuego;
import excepciones.PersistenciaException;
import implementaciones.ComprasDAO;
import implementaciones.ConexionBD;
import implementaciones.DetallesComprasDAO;
import implementaciones.UsuariosDAO;
import implementaciones.VideojuegosDAO;
import interfaces.IComprasDAO;
import interfaces.IConexionBD;
import interfaces.IDetallesCompraDAO;
import interfaces.IUsuariosDAO;
import interfaces.IVideojuegosDAO;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author edw_v
 */
public class Pruebas {

    public static void main(String[] args) {
        IConexionBD conexionBD = new ConexionBD();
        IUsuariosDAO usuariosDAO = new UsuariosDAO(conexionBD);
        IComprasDAO comprasDAO = new ComprasDAO(conexionBD);
        IVideojuegosDAO videojuegosDAO = new VideojuegosDAO(conexionBD);
        IDetallesCompraDAO detallesCompraDAO = new DetallesComprasDAO(conexionBD);
        
        Usuario usuario1 = new Usuario(2L, "Edwing Villagrana", "6442860852");
        Compra compra1 = new Compra(Calendar.getInstance(), 700D, new Usuario(4L));
        
        Videojuego videojuego1 = new Videojuego(3L,"Metroid", "Nintendo", 150, 700D);
        DetallesCompra detalles = new DetallesCompra(1, 700D, 700D, compra1, videojuego1);
        
        try {
            detallesCompraDAO.agregar(detalles);
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
        
//        try {
//            List<DetallesCompra> lista = detallesCompraDAO.consultarPorIdCompra(compra1);
//            for (int i = 0; i < lista.size(); i++) {
//                System.out.println(lista.get(i));
//            }
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            detallesCompraDAO.agregar(detallesCompra);
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }



//        try {
//            for(Videojuego videojuego : videojuegosDAO.consultarTodos()){
//                System.out.println(videojuego);
//            }
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }
        
//        try {
//            comprasDAO.agregar(compra1);
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }
        
//        try {
//            usuariosDAO.agregar(usuario1);
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }


    }

}

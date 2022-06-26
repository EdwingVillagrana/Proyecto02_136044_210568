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
import java.util.LinkedList;
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
        Compra compra1 = new Compra(Calendar.getInstance(), 2300D, new Usuario(7L));
        
        Videojuego videojuego1 = new Videojuego(3L,"Metroid", "Nintendo", 150, 700D);
        Videojuego videojuego2 = new Videojuego(1L,"Mario Bros. 2", "Nintendo", 350, 900D);
        DetallesCompra detalles1 = new DetallesCompra(2, 700D, 1400D, compra1, videojuego1);
        DetallesCompra detalles2 = new DetallesCompra(1, 900D, 900D, compra1, videojuego2);
        
        try {
            List<DetallesCompra> detalles = new LinkedList<>();
            detalles.add(detalles1);
            detalles.add(detalles2);
            compra1.setDetallesCompra(detalles);
            comprasDAO.agregar(compra1);
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

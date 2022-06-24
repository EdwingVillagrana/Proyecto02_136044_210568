/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import entidades.Compra;
import entidades.Usuario;
import excepciones.PersistenciaException;
import implementaciones.ComprasDAO;
import implementaciones.ConexionBD;
import implementaciones.UsuariosDAO;
import interfaces.IComprasDAO;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;
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

        Usuario usuario1 = new Usuario(1L, "Edwing Villagrana", "6442860852");
        Compra compra1 = new Compra(Calendar.getInstance(), 500D);

//        try {
//            usuario1.addCompra(compra1);
//            usuariosDAO.agregar(usuario1);
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }
//        Calendar fecha = Calendar.getInstance();
//        fecha.set(Calendar.DAY_OF_MONTH, 10);
//        Calendar fechaFin = Calendar.getInstance();
//        fechaFin.set(Calendar.MONTH, 7);
//        
//        try {
//            
//            List<Compra> listaCompras = comprasDAO.consultarPorUsuario(usuario1);
//            for(Compra compra : listaCompras){
//                System.out.println(compra.getUsuario()+ ", " + compra.getId() + ", " + compra.getTotal());
//            }
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }

        try {
            Usuario usuario = usuariosDAO.consultarUsuarioPorId(1L);
            if(usuario != null){
                System.out.println(usuario);
            }else{
                System.out.println("No se encontró al usuario");
            }
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }

}

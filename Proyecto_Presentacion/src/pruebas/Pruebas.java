/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pruebas;

import entidades.Usuario;
import excepciones.PersistenciaException;
import implementaciones.ConexionBD;
import implementaciones.UsuariosDAO;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;

/**
 *
 * @author edw_v
 */
public class Pruebas {
    
    public static void main(String[] args){
        IConexionBD conexionBD = new ConexionBD();
        IUsuariosDAO usuariosDAO = new UsuariosDAO(conexionBD);
//INICIO USUARIO
//--------------------------------------------------------------------------------------------    
//        Usuario usuario1 = new Usuario("Edwing Villagrana", "6442860852");
//        Usuario usuario2 = new Usuario("Arturo Luna", "6442867845");
//        Usuario usuario3 = new Usuario("America Gortares", "6441543938");
//        Usuario usuario4 = new Usuario(1L, "Capitan America", "5544123456");
//        Usuario usuario5 = new Usuario(2L, "Bruce Banner", "5578945612");
//        Usuario usuario6 = new Usuario(3L, "Thor Oddinson", "6448754213");
        
        //AGREGAR-CORRECTO
//        try {
//            
//            usuariosDAO.agregar(usuario1);
//            usuariosDAO.agregar(usuario2);
//            usuariosDAO.agregar(usuario3);
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }

        //ACTUALIZAR-CORRECTO
//        try {
//            usuariosDAO.actualizar(usuario4);
//            usuariosDAO.actualizar(usuario5);
//            usuariosDAO.actualizar(usuario6);
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }
        //CONSULTAR-CORRECTO
        try {
            System.out.println(usuariosDAO.consultarUsuarioPorId(2L));
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
        //ELIMINAR-CORRECTO
//        try {
//            usuariosDAO.eliminar(new Usuario(10L));
//        } catch (PersistenciaException e) {
//            System.out.println(e.getMessage());
//        }
//--------------------------------------------------------------------------------------------
//FIN USUARIO
        
    }

}

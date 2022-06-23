/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pruebas;

import entidades.Compra;
import entidades.Usuario;
import excepciones.PersistenciaException;
import implementaciones.ConexionBD;
import implementaciones.UsuariosDAO;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;
import java.util.Calendar;

/**
 *
 * @author edw_v
 */
public class Pruebas {
    
    public static void main(String[] args){
        IConexionBD conexionBD = new ConexionBD();
        IUsuariosDAO usuariosDAO = new UsuariosDAO(conexionBD);
        
        Usuario usuario1 = new Usuario("Edwing Villagrana", "6442860852");
        Compra compra1 = new Compra(Calendar.getInstance(), 500D);
        
        try {
            usuario1.addCompra(compra1);
            usuariosDAO.agregar(usuario1);
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }

}

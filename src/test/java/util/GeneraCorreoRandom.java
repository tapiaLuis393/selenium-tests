package util;

public class GeneraCorreoRandom {

    public static String generarCorreoAleatorio() {
        String dominio = "@defontana.cl";        
        String prefijo = "usuario";              
        int numero = (int) (Math.random() * 10000); 
        return prefijo + numero + dominio;
    }

}
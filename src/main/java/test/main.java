package test;

import entities.Laboratoire;
import services.LaboratoireService;

import java.sql.SQLException;

public class main {
    public static void main(String []args){
        LaboratoireService l =new LaboratoireService();
        try {
            for (Laboratoire laboratoire : l.Display()) {
                System.out.println(l.Display());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

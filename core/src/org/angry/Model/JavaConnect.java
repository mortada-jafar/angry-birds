package org.angry.Model;

/**
 * Created by MyBaby on 12/15/2015.
 */
import java.sql.*;
import javax.swing.*;

public abstract class JavaConnect {

    public  Connection ConnecerDB() {

        try {
            Class.forName("org.sqlite.JDBC");//for my sql ("com.mysql.jdbc.Driver")
            Connection conn=DriverManager.getConnection("jdbc:sqlite:angryBirds.sqlite"); //name of DB
            return conn;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }

    }
}

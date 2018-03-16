package org.angry.Model;

import org.angry.view.Box;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by MyBaby on 1/22/2016.
 */
public class InsertDb extends JavaConnect {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    PreparedStatement pstc = null;
    PreparedStatement pstr = null;

    final String circleTable = "circle";
    final String boxTable = "box";
    final String record = "last";


    public InsertDb() {

        if(!ControllerLogic.RECORDING) {
            deleteAll(circleTable);
            deleteAll(boxTable);
        }else {
            deleteAll(record);
        }
        conn = ConnecerDB();
        String sql = "INSERT INTO " + boxTable + " (x,y,width,height) values(?,?,?,?)";
        String sqlc = "INSERT INTO " + circleTable + " (x,y,r) values(?,?,?)";
        String sqlr = "INSERT INTO " + record + " (vel_x,vel_y,x,y) values(?,?,?,?)";

        try {
          if(!ControllerLogic.RECORDING) {
              pstc = conn.prepareStatement(sqlc);
              for (org.angry.view.Pig c : ControllerLogic.circleArray) {
                  pstc.setString(1, String.valueOf(c.pig2body.position.x));
                  pstc.setString(2, String.valueOf(c.pig2body.position.y));
                  pstc.setString(3, String.valueOf(c.getWidth()));
                  pstc.execute();
              }
              pst = conn.prepareStatement(sql);
              for (Box box : ControllerLogic.boxArray) {
                  pst.setString(1, String.valueOf(box.rect.getX() - box.rect.getWidth()));
                  pst.setString(2, String.valueOf(box.rect.getY() - box.rect.getHeight()));
                  pst.setString(3, String.valueOf(box.rect.getWidth() * 2));
                  pst.setString(4, String.valueOf(box.rect.getHeight() * 2));
                  pst.execute();
              }
          }else {
              pstr = conn.prepareStatement(sqlr);
              System.out.println(ControllerLogic.vel.x);
              pstr.setString(1, String.valueOf(ControllerLogic.vel.x));
              pstr.setString(2, String.valueOf(ControllerLogic.vel.y));
              pstr.setString(3, String.valueOf(ControllerLogic.POS.y));
              pstr.setString(4, String.valueOf(ControllerLogic.POS.y));
              pstr.execute();
          }
        } catch (SQLException | HeadlessException e) {
            System.out.println(e);
        } finally {
            try {
                if(!ControllerLogic.RECORDING) {
                    pst.close();
                    pstc.close();
                }else {
                    pstr.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    public void deleteAll(String tableName) {
        conn = ConnecerDB();
        String sql = "delete from " + tableName;
        try {
            pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (SQLException | HeadlessException e) {
            System.out.println(e);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

    }
}

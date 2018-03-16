package org.angry.Model;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import org.angry.view.Box;
import org.angry.view.Pig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by MyBaby on 1/22/2016.
 */


public class GetDb extends JavaConnect {
    ResultSet rs = null;
    ResultSet rsc = null;
    ResultSet rsr = null;
    PreparedStatement pst = null;
    PreparedStatement pstc = null;
    PreparedStatement pstr = null;
    final String boxTable = "box";
    final String circleTable = "circle";
    final String record = "last";
    World world;

    public GetDb(World world) {
        this.world = world;
        Connection conn = ConnecerDB();
        String sql = "select * from " + boxTable;
        String sqlc = "select * from " + circleTable;
        String sqlr = "select * from " + record;
        try {
            pst = conn.prepareStatement(sql);
            pstc = conn.prepareStatement(sqlc);
            pstr = conn.prepareStatement(sqlr);
            rs = pst.executeQuery();
            rsc = pstc.executeQuery();
            rsr = pstr.executeQuery();
            while (rs.next()) {
                Rectangle rect = new Rectangle(Float.valueOf(rs.getString("x")), Float.valueOf(rs.getString("y")), Float.valueOf(rs.getString("width")), Float.valueOf(rs.getString("height")));
                ControllerLogic.boxArray.add(new Box(world, rect,.5f,.5f));
            }
            while (rsc.next()) {
                Ellipse e = new Ellipse(Float.valueOf(rsc.getString("x")), Float.valueOf(rsc.getString("y")), Float.valueOf(rsc.getString("r")), Float.valueOf(rsc.getString("r")));
                ControllerLogic.circleArray.add(new Pig(world, e));
            }
            if (rsr.next()) {
                ControllerLogic.vel.set(Float.valueOf(rsr.getString("vel_x")), Float.valueOf(rsr.getString("vel_y")));
                ControllerLogic.POS.set(Float.valueOf(rsr.getString("x")), Float.valueOf(rsr.getString("y")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                rs.close();
                rsr.close();
            } catch (SQLException ex) {
                Logger.getLogger(GetDb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

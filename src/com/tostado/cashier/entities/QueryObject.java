package com.tostado.cashier.entities;

import java.sql.*;

public class QueryObject {

    protected final  String connectionURL = "jdbc:mysql://remotemysql.com:3306/QkOSgiDCNF";
    protected final  String connectionUser = "QkOSgiDCNF";
    protected final  String connectionPassword = "Z5ifiRZ0rP";

    protected String statement;
    protected Connection conn = null;
    protected PreparedStatement stmt = null;


    protected boolean query(String statement) {
        try {
            conn = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
            stmt = conn.prepareStatement(statement);
            int result = stmt.executeUpdate();
            if (result > 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return false;
    }
}

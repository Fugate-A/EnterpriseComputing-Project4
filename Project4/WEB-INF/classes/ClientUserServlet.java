/* Name: Andrew Fugate
 Course: CNT 4714 – Fall 2024 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: December 1, 2024
*/
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ClientUserApp")
public class ClientUserServlet extends HttpServlet {
    private Connection conn;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String sqlStatement = request.getParameter("sql");
        String lowerCaseStatement = sqlStatement != null ? sqlStatement.toLowerCase().trim() : "";
        String resultMessage = "";

        try {
            establishDatabaseConnection();

            if (lowerCaseStatement.startsWith("select")) {
                resultMessage = executeSelectQuery(sqlStatement);
            } else if (isDMLCommand(lowerCaseStatement)) {
                executeUpdateQuery(sqlStatement);
                resultMessage = generateSuccessMessage("SQL statement executed successfully.");
            } else {
                resultMessage = generateErrorMessage("Invalid SQL command. Only SELECT, INSERT, UPDATE, DELETE, or REPLACE are allowed.");
            }
        } catch (SQLException e) {
            resultMessage = generateErrorMessage("SQL Error: " + e.getMessage());
        } finally {
            closeDatabaseConnection();
        }

        HttpSession session = request.getSession();
        session.setAttribute("message", resultMessage);
        session.setAttribute("sql", sqlStatement);
        response.sendRedirect("clientHome.jsp");
    }

    private void establishDatabaseConnection() throws SQLException {
        Properties dbProperties = new Properties();
        ServletContext context = getServletContext();

        try (InputStream inputStream = context.getResourceAsStream("/WEB-INF/lib/client.properties")) {
            if (inputStream == null) {
                throw new IOException("Database properties file not found.");
            }

            dbProperties.load(inputStream);

            String dbDriver = dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS");
            String dbUrl = dbProperties.getProperty("MYSQL_DB_URL");
            String dbUser = dbProperties.getProperty("MYSQL_DB_USERNAME");
            String dbPassword = dbProperties.getProperty("MYSQL_DB_PASSWORD");

            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new SQLException("Failed to establish database connection: " + e.getMessage(), e);
        }
    }

    private String executeSelectQuery(String sqlStatement) throws SQLException {
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {
            return ResultSetToHTML.getHtml(resultSet);
        }
    }
    
    private void executeUpdateQuery(String sqlStatement) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(sqlStatement);
        }
    }

    private boolean isDMLCommand(String sqlStatement) {
        return sqlStatement.startsWith("insert") || sqlStatement.startsWith("update") 
                || sqlStatement.startsWith("delete") || sqlStatement.startsWith("replace");
    }

    private String generateSuccessMessage(String message) {
        return "<table><tr bgcolor='green'><th style='text-align:center; background-color: green;'><font color='#ffffff'><b>" 
                + message + "</b></font></th></tr></table>";
    }

    private String generateErrorMessage(String message) {
        return "<table><tr bgcolor='red'><th style='text-align:center; background-color: red;'><font color='#ffffff'><b>" 
                + message + "</b></font></th></tr></table>";
    }

    private void closeDatabaseConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
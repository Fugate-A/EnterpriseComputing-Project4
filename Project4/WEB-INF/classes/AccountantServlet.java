/* Name: Andrew Fugate
 Course: CNT 4714 – Fall 2024 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: December 1, 2024
*/
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

@WebServlet("/AccountantUserApp")
public class AccountantServlet extends HttpServlet {
    private Connection connection;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reportType = request.getParameter("query");
        String htmlResult = "";

        try {
            establishDatabaseConnection();

            htmlResult = executeStoredProcedure(reportType);

        } catch (SQLException e) {
            htmlResult = "<h3 style='color:red;'>Error: Unable to process your request. " + e.getMessage() + "</h3>";
            e.printStackTrace();
        } finally {
            closeDatabaseConnection();
        }

        HttpSession session = request.getSession();
        session.setAttribute("message", htmlResult);
        response.sendRedirect("accountantHome.jsp");
    }

    private void establishDatabaseConnection() throws SQLException {
        Properties dbProperties = new Properties();
        ServletContext context = getServletContext();

        try (InputStream inputStream = context.getResourceAsStream("/WEB-INF/lib/theaccountant.properties")) {
            if (inputStream == null) {
                throw new IOException("Database properties file not found!");
            }

            dbProperties.load(inputStream);

            String dbDriver = dbProperties.getProperty("MYSQL_DB_DRIVER_CLASS");
            String dbUrl = dbProperties.getProperty("MYSQL_DB_URL");
            String dbUsername = dbProperties.getProperty("MYSQL_DB_USERNAME");
            String dbPassword = dbProperties.getProperty("MYSQL_DB_PASSWORD");

            try {
                Class.forName(dbDriver);
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC Driver not found: " + e.getMessage(), e);
            }

        } catch (IOException e) {
            throw new SQLException("Failed to load database properties: " + e.getMessage(), e);
        }
    }

    private String executeStoredProcedure(String reportType) throws SQLException {
        if (connection == null) {
            throw new SQLException("Database connection not established.");
        }

        CallableStatement callableStatement;
        ResultSet resultSet;
        String procedureCall;

        switch (reportType) {
            case "SumServlet":
                procedureCall = "{CALL Get_The_Sum_Of_All_Parts_Weights()}";
                break;
            case "MaxValueServlet":
                procedureCall = "{CALL Get_The_Maximum_Status_Of_All_Suppliers()}";
                break;
            case "TotalNumberShipments":
                procedureCall = "{CALL Get_The_Total_Number_Of_Shipments()}";
                break;
            case "NameNumJobMostWorkers":
                procedureCall = "{CALL Get_The_Name_Of_The_Job_With_The_Most_Workers()}";
                break;
            case "NameStatusSupplier":
                procedureCall = "{CALL List_The_Name_And_Status_Of_All_Suppliers()}";
                break;
            default:
                throw new SQLException("Invalid report type.");
        }

        callableStatement = connection.prepareCall(procedureCall);
        resultSet = callableStatement.executeQuery();

        String htmlTable = generateHtmlTable(resultSet);

        resultSet.close();
        callableStatement.close();

        return htmlTable;
    }

    private String generateHtmlTable(ResultSet resultSet) throws SQLException {
        StringBuilder html = new StringBuilder();
        html.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");

        html.append("<tr>");
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            html.append("<th style='background-color: lightgrey; padding: 8px;'>")
                .append(resultSet.getMetaData().getColumnName(i))
                .append("</th>");
        }
        html.append("</tr>");

        while (resultSet.next()) {
            html.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                html.append("<td style='padding: 8px;'>")
                    .append(resultSet.getString(i))
                    .append("</td>");
            }
            html.append("</tr>");
        }

        html.append("</table>");
        return html.toString();
    }

    private void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
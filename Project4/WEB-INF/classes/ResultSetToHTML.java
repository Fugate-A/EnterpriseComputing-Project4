/* Name: Andrew Fugate
 Course: CNT 4714 – Fall 2024 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: December 1, 2024
*/
import java.sql.*;

public class ResultSetToHTML {
    public static synchronized String getHtml(ResultSet results) throws SQLException {
        if (results == null) {
            return "<p>No results to display</p>";
        }

        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();
        StringBuilder html = new StringBuilder();
        
        html.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        html.append("<tr style='background-color: #f2f2f2;'>");

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            html.append("<th style='padding: 8px; text-align: left;'>").append(columnName).append("</th>");
        }
        html.append("</tr>");
        while (results.next()) {
            html.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                String cellValue = results.getString(i);
                html.append("<td style='padding: 8px;'>")
                    .append(cellValue != null ? cellValue : "")
                    .append("</td>");
            }
            html.append("</tr>");
        }

        html.append("</table>");

        return html.toString();
    }
}
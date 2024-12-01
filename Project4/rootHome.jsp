<!DOCTYPE html>

<%
    // Retrieve session attributes
    String message = (String) session.getAttribute("message");
    String user = (String) session.getAttribute("user");
    String sqlStatement = (String) session.getAttribute("sql");
%>

<html>
<head>
    <title>Root User Home - Project 4</title>
    <style>
        body {
            background: black;
            text-align: center;
            font-family: Arial, sans-serif;
        }
        h1 {
            color: yellow;
            font-size: 28pt;
        }
        h2 {
            color: lime;
            font-size: 24pt;
        }
        input {
            color: yellow;
            background: #665D1E;
            font-weight: bold;
            font-size: 16pt;
        }
        input[type="submit"] {
            color: lime;
        }
        p {
            color: black;
            font-size: 13pt;
        }
        span {
            color: yellow;
            font-size: 20pt;
        }
        table {
            font-family: Verdana;
            border: 3px solid black;
            margin-left: auto;
            margin-right: auto;
        }
        textarea {
            background: blue;
            color: white;
            font-family: Verdana;
            font-size: 15pt;
            width: 1000px;
            height: 300px;
        }
        th {
            padding: 5px;
            border: 1px solid black;
            background: red;
        }
        td {
            padding: 5px;
            border: 1px solid black;
        }
        tr:nth-child(even) {
            background-color: lightgray;
        }
        tr:nth-child(odd) {
            background-color: white;
        }
    </style>
    <script>
        function clearResults() {
            document.getElementById("message").innerHTML = "";
        }

        function resetForm() {
            document.getElementById("sql").value = "";
        }
    </script>
</head>
<body>
    <% if ("root".equals(user)) { %>
        <h1>Welcome, Root User, to the Fall 2024 Project 4 Enterprise System</h1>
        <h2>A Servlet/JSP-Based Multi-Tiered Enterprise Application Using a Tomcat Container</h2>
        <hr>
        <div style="color:white;">
            You are connected to the Project 4 Enterprise System database as a 
            <span style="color:red; font-size: 16px;"><%= user %></span>-level user.
        </div>
        <div style="color:white;">Please enter any SQL query or update command in the box below.</div>
        <br><br>
        <form action="RootUserServlet" method="post">
            <textarea id="sql" name="sql"><%= sqlStatement != null ? sqlStatement : "" %></textarea>
            <br><br>
            <input type="submit" value="Execute Command"> &nbsp; &nbsp; &nbsp;
            <input type="button" value="Reset Form" style="color: red;" onclick="resetForm()"> &nbsp; &nbsp; &nbsp;
            <input type="button" value="Clear Results" onclick="clearResults()">
        </form>
        <br><br><br>
        <div style="color:white;">All execution results will appear below this line.</div>
        <br>
        <hr>
        <br>
        <div style="color:white; font-weight: bold;">Execution Results:</div>
        <br>
        <div id="message">
            <%= message != null ? message : "" %>
        </div>
    <% } else { %>
        <script>
            window.location.replace("http://localhost:8080/Project4/authentication.html");
        </script>
    <% } %>
</body>
</html>

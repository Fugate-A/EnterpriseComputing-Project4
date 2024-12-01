<!DOCTYPE html>

<%
    // Retrieve session attributes
    String message = (String) session.getAttribute("message");
    String user = (String) session.getAttribute("user");
    message = message != null ? message : "";
    user = user != null ? user : "";
%>

<html>
<head>
    <title>Accountant User - Project 4</title>
    <style>
        body {
            background: black;
            color: white;
            text-align: center;
            font-family: Arial, sans-serif;
        }
        h1 {
            color: lime;
            font-size: 28pt;
        }
        h2 {
            color: orange;
            font-size: 24pt;
        }
        input[type="submit"], input[type="button"] {
            font-size: 16pt;
            font-weight: bold;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"] {
            color: lime;
            background-color: #004400;
        }
        input[type="button"] {
            color: red;
            background-color: #440000;
        }
        input[type="radio"] {
            margin-right: 10px;
            cursor: pointer;
        }
        .instructions {
            margin: 20px auto;
            font-size: 18pt;
            text-align: center;
        }
        .form-container {
            display: inline-block;
            background-color: #333;
            border-radius: 10px;
            padding: 20px;
            width: 80%;
            text-align: left;
        }
        .radio-option {
            margin: 10px 0;
            font-size: 16pt;
        }
        .results-container {
            margin-top: 30px;
        }
        .results-header {
            font-size: 18pt;
            font-weight: bold;
        }
        table {
            margin: 20px auto;
            width: 80%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid white;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #444;
        }
        tr:nth-child(even) {
            background-color: #666;
        }
        tr:nth-child(odd) {
            background-color: #555;
        }
        hr {
            border: 1px solid yellow;
            width: 90%;
            margin: 20px auto;
        }
    </style>
    <script>
        // Clear results
        function clearResults() {
            document.getElementById("message").innerHTML = "";
        }
    </script>
</head>
<body>
    <% if ("theaccountant".equals(user)) { %>
        <h1>Accountant User Dashboard</h1>
        <h2>Welcome to the Fall 2024 Project 4 System</h2>
        <hr>
        <div class="instructions">
            <p>You are logged in as an <span style="color: red;">accountant-level user</span>.</p>
            <p>Select one of the operations below and click "Execute Command" to view the results.</p>
        </div>
        <div class="form-container">
            <form action="AccountantServlet" method="post">
                <div class="radio-option">
                    <input type="radio" name="query" value="MaxValueServlet" required>
                    Get The Maximum Status Value Of All Suppliers
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="SumServlet">
                    Get The Total Weight Of All Parts
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="TotalNumberShipments">
                    Get The Total Number of Shipments
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="NameNumJobMostWorkers">
                    Get The Name and Number of Workers of the Job with the Most Workers
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="NameStatusSupplier">
                    List The Name And Status Of Every Supplier
                </div>
                <br>
                <div style="text-align: center;">
                    <input type="submit" value="Execute Command">
                    <input type="button" value="Clear Results" onclick="clearResults()">
                </div>
            </form>
        </div>
        <div class="results-container">
            <hr>
            <p class="results-header">Execution Results:</p>
            <div id="message">
                <%= message %>
            </div>
        </div>
    <% } else { %>
        <script>
            window.location.replace("/Project4/authentication.html");
        </script>
    <% } %>
</body>
</html>
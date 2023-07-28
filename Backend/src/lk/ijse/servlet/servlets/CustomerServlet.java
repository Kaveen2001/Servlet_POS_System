package lk.ijse.servlet.servlets;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "CustomerServlet",urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    // Access an ODBC Connection Pool (JNDI - Java Naming Directory Interface)
    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

                            // Servlet CRUD Operations
    // Save Customer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Customer POST Method Invoked..!");

        /*resp.setStatus(404); // only status code-no error message
        resp.sendError(404); // status and relevant error message for 404*/

        String id = req.getParameter("customerID");
        String name = req.getParameter("customerName");
        String address = req.getParameter("customerAddress");
        String salary = req.getParameter("customerSalary");
        PrintWriter respWriter = resp.getWriter();

        System.out.println(id+" "+name+" "+address+" "+salary);

        // The Media Type of the Content of the response
        resp.setContentType("application/json");

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pstn = connection.prepareStatement("insert into Customer values(?,?,?,?)");
            pstn.setObject(1,id);
            pstn.setObject(2,name);
            pstn.setObject(3,address);
            pstn.setObject(4,salary);

            if (pstn.executeUpdate() > 0) {

                // Generate a custom response with JSON Object
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED); // Status Code --> 201
                objectBuilder.add("status",200);
                objectBuilder.add("message","Customers Added Successful..!");
                objectBuilder.add("data","");
                respWriter.print(objectBuilder.build());
            }

        } catch (SQLException e) {

            // Generate a custom response Error Message with JSON Object
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status",400);
            objectBuilder.add("message","Error..!");
            objectBuilder.add("data",e.getLocalizedMessage());
            respWriter.print(objectBuilder.build());

            resp.setStatus(HttpServletResponse.SC_OK); // Status Code --> 200
            e.printStackTrace();
        }
    }

    // Update Customer
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Customer PUT Method Invoked..!");

        // JSON Processing (How to retrieve data from JSON request using JSON Processing Spec)
        JsonReader jsonReader = Json.createReader(req.getReader());
        JsonObject jsonObject = jsonReader.readObject();
        String customerID = jsonObject.getString("id");
        String customerName = jsonObject.getString("name");
        String customerAddress = jsonObject.getString("address");
        String customerSalary = jsonObject.getString("salary");

        System.out.println(customerID+" "+customerName+" "+customerAddress+" "+customerSalary);

        PrintWriter respWriter = resp.getWriter();

        // The Media Type of the Content of the response
        resp.setContentType("application/json");

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pstn = connection.prepareStatement("Update Customer set name=?,address=?,salary=? where id=?");
            pstn.setObject(1,customerName);
            pstn.setObject(2,customerAddress);
            pstn.setObject(3,customerSalary);
            pstn.setObject(4,customerID);

            if (pstn.executeUpdate() > 0) {

                // Generate a custom response with JSON Object
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED); // Status Code --> 201
                objectBuilder.add("status",200);
                objectBuilder.add("message","Customer Updated Successful..!");
                objectBuilder.add("data","");
                respWriter.print(objectBuilder.build());

            } else {

                // Generate a custom response with JSON Object
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED); // Status Code --> 201
                objectBuilder.add("status",400);
                objectBuilder.add("message","Customer Updated Failed..!");
                objectBuilder.add("data","");
                respWriter.print(objectBuilder.build());
            }
        } catch (SQLException e) {

            // Generate a custom response with JSON Object
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status",500);
            objectBuilder.add("message","Customer Updated Failed..!");
            objectBuilder.add("data",e.getLocalizedMessage());
            respWriter.print(objectBuilder.build());
            e.printStackTrace();
        }
    }

    // Delete Customer
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Customer DELETE Method Invoked..!");

        String customerID = req.getParameter("CusID"); // Send data via Query String Value
        System.out.println(customerID);

        PrintWriter respWriter = resp.getWriter();

        // The Media Type of the Content of the response
        resp.setContentType("application/json");

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pstn = connection.prepareStatement("Delete from Customer where id=?");
            pstn.setObject(1,customerID);

            if (pstn.executeUpdate() > 0) {

                // Generate a custom response with JSON Object
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED); // Status Code --> 201
                objectBuilder.add("status",200);
                objectBuilder.add("message","Customers Deleted Successful..!");
                objectBuilder.add("data","");
                respWriter.print(objectBuilder.build());
            }

        } catch (SQLException e) {

            // Generate a custom response Error Message with JSON Object
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status",400);
            objectBuilder.add("message","Error..!");
            objectBuilder.add("data",e.getLocalizedMessage());
            respWriter.print(objectBuilder.build());
            e.printStackTrace();
        }
    }

    // Get All Customers
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Customer GET Method Invoked..!");

        /*String option = req.getParameter("option");
        switch (option) {
            case "SEARCH":
                System.out.println();
                break;
            case "GETALL":
                System.out.println();
                break;
        }*/

        try {

            /*// The Media Type of the Content of the response
            resp.setContentType("application/json");*/

            Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement("select * from Customer").executeQuery();

            // Using JSON API Array
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); // Create a JSON Array

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                double salary = resultSet.getDouble(4);
                System.out.println(id + " " + name +" " + address + " " +salary);

                // Create a JSON Object and store values
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("id",id);
                objectBuilder.add("name",name);
                objectBuilder.add("address",address);
                objectBuilder.add("salary",salary);

                // Add JSON Object to the JSON Array
                arrayBuilder.add(objectBuilder.build());
            }

            // Then print the JSON Array as the Response
            PrintWriter respWriter = resp.getWriter();

            // Generate a custom response with JSON Object
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status",200);
            objectBuilder.add("message","Get All Customers Done..!");
            objectBuilder.add("data",arrayBuilder.build());

            respWriter.print(objectBuilder.build());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

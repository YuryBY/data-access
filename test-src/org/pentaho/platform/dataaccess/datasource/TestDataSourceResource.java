package org.pentaho.platform.dataaccess.datasource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.pentaho.platform.dataaccess.datasource.beans.Connection;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class TestDataSourceResource {
  private static final String CONNECTION_NAME = "TylerSales";

  private static final String add_url =   "http://localhost:8080/pentaho/plugin/data-access/api/connection/add";


  private static final String update_url =   "http://localhost:8080/pentaho/plugin/data-access/api/connection/update";

  private static final String getURL =    "http://localhost:8080/pentaho/plugin/data-access/api/connection/get";

  private static final String delete_url ="http://localhost:8080/pentaho/plugin/data-access/api/connection/delete";

  private static Client client = null;

  public static void main(String[] args) {
    Connection conn = getConnectionByName(CONNECTION_NAME);
    
    if(conn == null)
      add();
    else
      update();
    conn = getConnectionByName(CONNECTION_NAME);
    if(conn != null){
      System.out.println("GetConn "+ conn.getName());
      delete();
    }
  }

  private static void update() {
    init();
    Connection connection = createConnectionObject();
    String conn = (new JSONObject(connection)).toString();
    System.out.println(conn);
    try {

      WebResource resource = client.resource(update_url);
      Response result = resource.accept(MediaType.APPLICATION_JSON).entity(conn).post(Response.class);
      System.out.println(result);
    } catch (Exception ex) {
      System.out.println("Error in update");
    }
  }

  private static void delete() {
    init();
    WebResource resource = client.resource(delete_url);
    Connection connection = createConnectionObject();
    String conn = (new JSONObject(connection)).toString();
    System.out.println(conn);
    try {
      Response result = resource
          //.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
          //.accept(MediaType.APPLICATION_JSON_TYPE)
          .accept(MediaType.APPLICATION_JSON)
          .entity(conn)
          .post(Response.class);
      System.out.println(result);
    } catch (Exception ex) {
      System.out.println("Error in Delete");
    }
  }

  private static void init() {
    ClientConfig clientConfig = new DefaultClientConfig();
    client = Client.create(clientConfig);
    client.addFilter(new HTTPBasicAuthFilter("joe", "password"));
  }

  private static void add() {
    init();
    Connection connection = createConnectionObject();
    String conn = (new JSONObject(connection)).toString();
    System.out.println(conn);
    try {

      WebResource resource = client.resource(add_url);
      Response result = resource.accept(MediaType.APPLICATION_JSON).entity(conn).post(Response.class);
      System.out.println(result);
    } catch (Exception ex) {
      System.out.println("Error in Add");
    }
  }

  private static Connection createConnectionObject() {  
    Connection connection = new Connection();
    connection.setDriverClass("org.hsqldb.jdbcDriver");
    connection.setUrl("jdbc:hsqldb:hsql://localhost:9001/sampledata");
    connection.setUsername("pentaho_user");
    connection.setPassword("password");
    connection.setName(CONNECTION_NAME);
    return connection;
  }

  private static Connection getConnectionByName(String aConnecitonName) {
    ClientConfig clientConfig = new DefaultClientConfig();
    Client client = Client.create(clientConfig);
    Connection connection = null;
    WebResource resource = client.resource(getURL);
    try {
      connection = resource.type(MediaType.APPLICATION_JSON)
          .type(MediaType.APPLICATION_XML).entity(aConnecitonName)
          .get(Connection.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return connection;
  }

  private static Connection createConnection(String name) {
    Connection conn = new Connection();
    conn.setName(name);
    return conn;
  }
}

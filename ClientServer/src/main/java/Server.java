import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    
    private static List<String> products = new ArrayList<>();
    
    public static void main(String[] args) {
        //iniciamos el servidor en el puesto 8080
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("Server Started");
            
            //creamos loop infinito para nuestro ambiente
            while(true){
                //esperamos una conexión de algún cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Enabled");
                
                //manejamos la conexión con el cliente mediante un nuevo hilo
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void handleClient(Socket clientSocket) {
        try {
            
            //creamos los streams para recibir los datos y devolver respuestas
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String request;
            
            while((request = input.readLine()) != null){
                System.out.println("Request recieved " + request);
                
                if(request.equalsIgnoreCase("LIST")){
                    //si la solicitud del cliente es list, entonces el programa mostrará todos los productos
                    output.println("Product list: ");
                    for(String product : products){
                        System.out.println(product);
                    }
                    output.println("End list");
                }else if(request.startsWith("ADD")){
                    //si la solicitud del cliente es add, agregamos un nuevo producto a la lista
                    String product = request.substring(4);
                    products.add(product);
                    output.println("Product added " +product);
                }else if (request.equalsIgnoreCase("EXIT")){
                    //si la soliitud del cliente es exit, cerramos la conexión
                    output.println("Ending connection");
                    break;
                }else{
                    //opción por si la solicitud es desconocida
                    output.println("Unknown Request");
                }
            
            }
                        
                
            //cerramos el socket luego de procesar las solicitudes
            clientSocket.close();
            System.out.println("Client disconnected");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
}

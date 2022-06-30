package vtp2022.practice;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;



public class HttpClientConnection implements Runnable {

    Socket socket;
    Boolean fileFound = true;
    ArrayList<File> fileList = new ArrayList<>();

    
    public HttpClientConnection(Socket socket, ArrayList<File> fileList){
        this.socket = socket;
        this.fileList = fileList;
    }

    @Override
    public void run(){
        try {
            HttpServer httpServer = new HttpServer(socket);
            String input = httpServer.read();
            System.out.println(input);
            String[] terms = input.split(" ");
            String methodName = terms[0];
            String resourceName = terms[1];
            if (resourceName.equals("/"))
                resourceName ="/index.html";
            resourceName = resourceName.replace("/", "");
            System.out.println("New Resource: " + resourceName);

            // resourceName = "xxxTESTINGxx";
            // methodName = "TEST";

            //Check first term terms[0]
            if (!methodName.equals("GET")){
                String response = "HTTP/1.1 405 Method Not Allowed\r\n\r\n" + methodName 
                                    + " not supported \r\n";
                httpServer.writeString(response);
                httpServer.close();
                System.out.println(methodName + " not allowed");
                socket.close();

            } else {
  
            System.out.println("Checking resource...");

            //Check resource terms[1]
            String response;
            for(File file: fileList){
                System.out.println("resource name " + resourceName);
                System.out.println("filename " + file.getName());
                if (file.getName().equals(resourceName)){
                    System.out.println(resourceName);
                    System.out.println(file.getName() + " is found");
                    FileInputStream fis = new FileInputStream(file);
                    System.out.println(file);
                    byte[] data = fis.readAllBytes();
                    fis.close();
                    if (resourceName.contains(".png")){
                        response = "HTTP/1.1 200 OK\r\n Content-Type: image/png\r\n\r\n";
                        System.out.println("resource contains png");
                    } else {
                        response = "HTTP/1.1 200 OK\r\n\r\n"; 
                        System.out.println("resource does not contain png");   
                    }
                    httpServer.writeString(response);
                    httpServer.writeBytes(data);
                    httpServer.flush();
                    fis.close();
                    fileFound=true;
                    System.out.println("fileFound is " + fileFound);
                    System.out.println("BREAK OUT OF FOR LOOP");
                    break;
                    
                } else fileFound = false;
            }
            

                if (!fileFound){
                    response = "HTTP/1.1 404 Not Found\r\n\r\n" + resourceName 
                    + " not found \r\n";
                    System.out.println(resourceName + " not found");
                    httpServer.writeString(response);
                }
                httpServer.close();
                socket.close();
                System.out.println("SOCKET CLOSE");
            }
        } catch (Exception e) {
        e.printStackTrace();
        }
    }
}

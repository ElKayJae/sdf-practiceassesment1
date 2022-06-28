package vtp2022.practice;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



public class HttpClientConnection implements Runnable {

    Socket socket;
    String stringPath;
    String stringPath2;
    File docRoot;
    File docRoot2;
    Boolean fileFound = false;
    
    public HttpClientConnection(Socket socket, String stringPath, String stringPath2){
        this.socket = socket;
        this.stringPath = stringPath;
        this.stringPath2 = stringPath2;
        this.docRoot = new File(stringPath);
        if (null != stringPath2)
        this.docRoot = new File(stringPath2);
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
            Path resourcePath = Paths.get(stringPath, resourceName);
            System.out.println(resourcePath);
            System.out.println(resourceName);

            // resourceName = "xxx";

            //Check first term terms[0]
            if (!methodName.equals("GET")){
                String response = "HTTP/1.1 405 Method Not Allowed\r\n\r\n" + methodName 
                                    + " not supported \r\n";
                httpServer.writeString(response);
                socket.close();
            }

            System.out.println("Checking resource...");

            //Check resource terms[1]
            ArrayList<File> directoryList = new ArrayList<>();
            String response;
            
            
            for (File file: docRoot.listFiles()){
                directoryList.add(file);  
            } 
            
            if (null!=docRoot2){
                for (File file: docRoot2.listFiles()) directoryList.add(file);
            }

            for (File file: docRoot.listFiles()){
                System.out.println(file); 
            } 
            

            for(File file: directoryList){
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
                    
                } else fileFound = true;

                }

                if (!fileFound){
                    response = "HTTP/1.1 404 Not Found\r\n\r\n" + resourceName 
                    + " not found \r\n";
                    System.out.println("file not found");
                    System.out.println(resourceName);
                    httpServer.writeString(response);
                }
                httpServer.close();
                socket.close();
        } catch (Exception e) {
        e.printStackTrace();
        }
    }




    public void checkPath(){
        if (!docRoot.exists()){
            System.out.println(docRoot +" does not exist");
            System.exit(1);
        } else if (!docRoot.isDirectory()){
                System.out.println(docRoot +" is not a directory");
                System.exit(1);
            } else if (!docRoot.canRead()){
                System.out.println(docRoot +" is not readable");
                System.exit(1);
            } else System.out.println(docRoot +" is readable");


            if (null!=stringPath2){
                if (!docRoot2.exists()){
                    System.out.println(docRoot2 + " does not exist");
                } else if (!docRoot2.isDirectory()){
                        System.out.println(docRoot2 + " is not a directory");
                        System.exit(1);
                    } else if (!docRoot2.canRead()){
                        System.out.println(docRoot2 + " is not readable");
                        System.exit(1);
                    } else System.out.println(docRoot2 + " is readable");       
            }
    }


}

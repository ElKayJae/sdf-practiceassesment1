package vtp2022.practice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//mvn compile exec:java -Dexec.mainClass="vtp2022.practice.Main"
//java -jar target/sdf-practice-assessment1-1.0-SNAPSHOT.jar
public class Main 
{
    public static int port = 3000;
    public static String docRoot ="./static";
    public static String docRoot2;
    public static void main( String[] args ) throws IOException {

        if (args.length>0){
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--port")){
                    port = Integer.parseInt(args[i+1]) ;               
            } else if (args[i].equals("--docRoot")){
                if (args[i+1].contains(":")){
                    String[] directories = args[i+1].split(":");
                    docRoot = directories[0];
                    docRoot2 = directories[1];
                }else docRoot = args[i+1];
            }
        }
    }

        System.out.println("Port: " + port);
        System.out.println("Root Directory: " +docRoot);
        System.out.println("Other Directory: " + docRoot2);
        System.out.println( "Hello World!" );

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        ServerSocket server = new ServerSocket(port);

        while (true) {
            System.out.println("Waiting for client connection");
            Socket sock = server.accept();
            System.out.println("Connected");
            HttpClientConnection thr = new HttpClientConnection(sock, docRoot, docRoot2);
            thr.checkPath();
            threadPool.submit(thr);
            System.out.println("Submitted to threadpool");
            
            
        }


    
    }
}

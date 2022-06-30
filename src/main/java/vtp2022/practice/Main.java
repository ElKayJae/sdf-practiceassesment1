package vtp2022.practice;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//mvn compile exec:java -Dexec.mainClass="vtp2022.practice.Main" -Dexec.args=""
//java -jar target/sdf-practice-assessment1-1.0-SNAPSHOT.jar
public class Main {

    public static int port = 3000;
    public static String[] stringDirectories = {"./static"};
    public static ArrayList<File> directoryList = new ArrayList<>();
    public static ArrayList<File> fileList = new ArrayList<>();
    
    public static void stringToFolder(){
        for (String docRoot: stringDirectories){
            File docRootFolder = new File(docRoot);
            directoryList.add(docRootFolder);
        }
        System.out.println("Directory List:");
        directoryList.forEach(directory->System.out.println(directory));
    }

    public static void folderToFileList(){
        for (File directory:directoryList){
            for (File file:directory.listFiles()){
                fileList.add(file);
            }
        }
        System.out.println("File List:");
        fileList.forEach(file -> System.out.println(file));
    }

    public static void checkPath(){
        boolean systemExit = false;
        for(File docRoot:directoryList){
            if (!docRoot.exists()){
                System.out.println(docRoot +" does not exist");
                systemExit=true;
            } else if (!docRoot.isDirectory()){
                    System.out.println(docRoot +" is not a directory");
                    systemExit=true;
                } else if (!docRoot.canRead()){
                    System.out.println(docRoot +" is not readable");
                    systemExit=true;
                } else System.out.println(docRoot +" is readable");
        }
        if (systemExit)
            System.exit(1);
    }

    public static void main( String[] args ) throws IOException {

        if (args.length>0){
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--port")){
                    port = Integer.parseInt(args[i+1]);
                } else if (args[i].equals("--docRoot")){
                     stringDirectories = args[i+1].split(":");
                    }
                }
            }

        System.out.println("Port: " + port);
        stringToFolder();
        checkPath();
        folderToFileList();

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        ServerSocket server = new ServerSocket(port);
        int count = 0;

        while (true) {
            System.out.println("Waiting for client connection "+ count);
            Socket sock = server.accept();
            System.out.println("Connected");
            HttpClientConnection thr = new HttpClientConnection(sock, fileList);
            threadPool.submit(thr);
            System.out.println("Submitted to threadpool " + count);
            count++;
            
            
        }


    
    }
}

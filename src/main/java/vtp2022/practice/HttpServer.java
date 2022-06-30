package vtp2022.practice;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpServer {

    public Socket socket;
    public InputStream is;
    public OutputStream os;
    public InputStreamReader isr;
    public BufferedReader br;
    
    public HttpServer(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
        this.isr = new InputStreamReader(this.is);
        this.br = new BufferedReader(this.isr);
        
    }

    public void writeBytes(byte[] buffer) throws Exception {
        writeBytes(buffer, 0, buffer.length);
    }
    
    public void writeBytes(byte[] buffer, int start, int offset) throws Exception {
        this.os.write(buffer, start, offset);
    }


    public void writeString(String string) throws Exception {
        writeBytes(string.getBytes());
    }

    public String read() throws IOException {
        return this.br.readLine();
        }

    public void close() throws IOException{
        this.os.flush();
        this.os.close();
    }

    public void flush() throws Exception {
        this.os.flush();
    }
            
    


    




    
}


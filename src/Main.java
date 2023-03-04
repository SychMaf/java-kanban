import logics.server.HttpTaskServer;
import logics.server.KVServer;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new KVServer().start();
            new HttpTaskServer().startServer();
        } catch (IOException e) {
            System.out.println("Ошибка при запуске");
        }
    }
}
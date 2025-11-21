//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        // 1. Создаем полного клиента (наследник от ClientLITTLE)
        ClientLITTLE little1 = new ClientLITTLE(1, "Саша", "Туманов");
        Client client1 = new Client(little1, "+79981234567", "alex@gmail.com");
        System.out.println("Полная версия client1:");
        System.out.println(client1);
        System.out.println("Краткая версия client1:");
        System.out.println(little1);
        System.out.println();

        // 2. Создаем клиента из строки
        String data = "2;Анна;Кузнецова;+79982345678;anna@mail.ru";
        Client client2 = new Client(data);
        System.out.println("Полная версия client2:");
        System.out.println(client2);
        System.out.println("Краткая версия client2:");
        System.out.println(client2.toShortString());
        System.out.println();

        try {
            try (InputStream is = Main.class.getResourceAsStream("Client.json")) {
                if (is != null) {
                    String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    JSONObject json = new JSONObject(jsonString);
                    Client readerFromFile = new Client(json);
                    System.out.println("Client (JSON): " + (readerFromFile));
                    return;
                }

                System.err.println("Файл Client.json не найден в ресурсах");
            }

        } catch (Exception e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}

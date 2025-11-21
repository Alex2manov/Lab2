import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Client_rep_json extends ClientRepository {
    private String filePath;

    public Client_rep_json(String filePath) {
        this.filePath = filePath;
        this.clients = new ArrayList<>();
        loadFromFile();
    }

    // a. Чтение всех значений из файла
    @Override
    public void loadFromFile() {
        try (InputStream is = new FileInputStream(filePath)) {
            String jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            // Парсинг
            JSONObject json = new JSONObject(jsonString);
            JSONArray clientsArray = json.getJSONArray("clients");

            clients.clear();
            for (int i = 0; i < clientsArray.length(); i++) {
                JSONObject clientJson = clientsArray.getJSONObject(i);
                Client client = new Client(clientJson);
                clients.add(client);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден, создается новый: " + filePath);
            // Создаем пустой список
            clients = new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            clients = new ArrayList<>();
        }
    }

    // b. Запись всех значений в файл
    @Override
    public void saveToFile() {
        saveToFile(this.filePath);
    }
    // Перегрузка
    @Override
    public void saveToFile(String customFilePath) {
        try (FileWriter writer = new FileWriter(customFilePath)) {
            JSONObject root = new JSONObject();
            JSONArray clientsArray = new JSONArray();

            for (Client client : clients) {
                JSONObject clientJson = new JSONObject();
                clientJson.put("ClientId", client.getClientId());
                clientJson.put("firstName", client.getFirstName());
                clientJson.put("lastName", client.getLastName());
                clientJson.put("phone", client.getPhone());
                clientJson.put("email", client.getEmail());
                clientsArray.put(clientJson);
            }

            root.put("clients", clientsArray);
            writer.write(root.toString(4)); // Форматирование с отступами
            writer.flush();

        } catch (Exception e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

}
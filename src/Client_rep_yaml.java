import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Client_rep_yaml extends ClientRepository {
    private String filePath;
    private Yaml yaml;

    public Client_rep_yaml(String filePath) {
        this.filePath = filePath;
        this.yaml = new Yaml();
        this.clients = new ArrayList<>();
        loadFromFile();
    }

    // a. Чтение всех значений из файла
    @Override
    public void loadFromFile() {
        try (InputStream is = new FileInputStream(filePath)) {
            // Загружаем данные из YAML
            Map<String, Object> data = yaml.load(is);
            if (data != null && data.containsKey("clients")) {
                List<Map<String, Object>> clientsList = (List<Map<String, Object>>) data.get("clients");

                clients.clear();

                for (Map<String, Object> clientData : clientsList) {
                    // Создаем JSON-like объект из YAML данных
                    Map<String, Object> clientJson = new LinkedHashMap<>();
                    clientJson.put("ClientId", clientData.get("ClientId"));
                    clientJson.put("firstName", clientData.get("firstName"));
                    clientJson.put("lastName", clientData.get("lastName"));
                    clientJson.put("phone", clientData.get("phone"));
                    clientJson.put("email", clientData.get("email"));

                    // Создаем клиента через JSONObject
                    Client client = new Client(new org.json.JSONObject(clientJson));
                    clients.add(client);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("YAML файл не найден, создается новый: " + filePath);
            clients = new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Ошибка чтения YAML файла: " + e.getMessage());
            clients = new ArrayList<>();
        }
    }

    // b. Запись всех значений в файл
    @Override
    public void saveToFile() {
        saveToFile(this.filePath);
    }

    @Override
    public void saveToFile(String customFilePath) {
        try (FileWriter writer = new FileWriter(customFilePath)) {
            // Создаем структуру данных для YAML
            Map<String, Object> root = new LinkedHashMap<>();
            List<Map<String, Object>> clientsList = new ArrayList<>();

            for (Client client : clients) {
                Map<String, Object> clientData = new LinkedHashMap<>();
                clientData.put("ClientId", client.getClientId());
                clientData.put("firstName", client.getFirstName());
                clientData.put("lastName", client.getLastName());
                clientData.put("phone", client.getPhone());
                clientData.put("email", client.getEmail());
                clientsList.add(clientData);
            }

            root.put("clients", clientsList);

            // Записываем в YAML формате
            yaml.dump(root, writer);

        } catch (Exception e) {
            System.err.println("Ошибка записи в YAML файл: " + e.getMessage());
        }
    }
}

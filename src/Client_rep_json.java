import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Client_rep_json {
    private List<Client> clients;
    private String filePath;

    public Client_rep_json(String filePath) {
        this.filePath = filePath;
        this.clients = new ArrayList<>();
        loadFromFile();
    }

    // a. Чтение всех значений из файла
    private void loadFromFile() {
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
    public void saveToFile() {
        saveToFile(this.filePath);
    }
    // Перегрузка
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

    // c. Получить объект по ID
    public Client getById(int id) {
        for (Client client : clients) {
            if (client.getClientId() == id) {
                return client;
            }
        }
        throw new IllegalArgumentException("Клиент с ID " + id + " не найден");
    }

    // d. Получить список k по счету n объектов класса
    public List<ClientLITTLE> get_k_n_short_list(int k, int n) {
        List<ClientLITTLE> result = new ArrayList<>();
        int startIndex = (n - 1) * k;
        // Пустой список, если начальный индекс вне диапазона
        if (startIndex < 0 || startIndex >= clients.size()) {
            return result;
        }

        int endIndex = Math.min(startIndex + k, clients.size());

        for (int i = startIndex; i < endIndex; i++) {
            Client client = clients.get(i);
            // Создаем краткую версию
            ClientLITTLE shortClient = new ClientLITTLE(
                    client.getClientId(),
                    client.getFirstName(),
                    client.getLastName()
            );
            result.add(shortClient);
        }

        return result;
    }

    // e. Сортировать элементы по выбранному полю
    public void sortByField(String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название поля не может быть пустым");
        }
        switch (fieldName.toLowerCase()) {
            case "lastname":
                clients.sort(Comparator.comparing(Client::getLastName));
                break;
            case "firstname":
                clients.sort(Comparator.comparing(Client::getFirstName));
                break;
            case "id":
                clients.sort(Comparator.comparingInt(Client::getClientId));
                break;
            case "email":
                clients.sort(Comparator.comparing(Client::getEmail));
                break;
            default:
                throw new IllegalArgumentException("Неизвестное поле для сортировки: " + fieldName);
        }
    }

    // Метод для генерации следующего ID
    private int generateNextId() {
        if (clients.isEmpty()) {
            return 1;
        }
        // Находим макс ID среди существующих клиентов
        int maxId = clients.stream()
                .mapToInt(Client::getClientId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

    // f. Добавить объект в список (при добавлении сформировать новый ID)
    public void addClient(ClientLITTLE clientInfo, String phone, String email) {
        int newId = generateNextId(); // новый ID
        Client newClient = new Client(
                new ClientLITTLE(newId, clientInfo.getFirstName(), clientInfo.getLastName()),
                phone,
                email
        );
        clients.add(newClient);
    }

    // Перегруженный метод
    public void addClient(String firstName, String lastName, String phone, String email) {
        int newId = generateNextId(); // новый ID
        ClientLITTLE clientInfo = new ClientLITTLE(newId, firstName, lastName);
        Client newClient = new Client(clientInfo, phone, email);
        clients.add(newClient);
    }

    // g. Заменить элемент списка по ID
    public void updateClient(int id, String firstName, String lastName, String phone, String email) {

        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            if (client.getClientId() == id) {
                // Берем текущие значения, если новые не указаны
                String newFirstName = (firstName != null) ? firstName : client.getFirstName();
                String newLastName = (lastName != null) ? lastName : client.getLastName();
                String newPhone = (phone != null) ? phone : client.getPhone();
                String newEmail = (email != null) ? email : client.getEmail();

                Client updatedClient = new Client(
                        new ClientLITTLE(id, newFirstName, newLastName),
                        newPhone,
                        newEmail
                );
                clients.set(i, updatedClient);
                return;
            }
        }
        throw new IllegalArgumentException("Клиент с ID " + id + " не найден для обновления");
    }

    // Обновить только email
    public void updateClientEmail(int id, String newEmail) {
        updateClient(id, null, null, null, newEmail);
    }

    // Обновить только телефон
    public void updateClientPhone(int id, String newPhone) {
        updateClient(id, null, null, newPhone, null);
    }

    // Обновить имя
    public void updateClientFirstName(int id, String newFirstName) {
        updateClient(id, newFirstName, null, null, null);
    }

    // Обновить фамилию
    public void updateClientLastName(int id, String newLastName) {
        updateClient(id, null, newLastName, null, null);
    }

    // Обновить все данные
    public void updateClientFull(int id, String newFirstName, String newLastName, String newPhone, String newEmail) {
        updateClient(id, newFirstName, newLastName, newPhone, newEmail);
    }

    // h. Удалить элемент списка по ID
    public void deleteClient(int id) {
        boolean removed = clients.removeIf(client -> client.getClientId() == id);
        if (!removed) {
            throw new IllegalArgumentException("Клиент с ID " + id + " не найден для удаления");
        }
    }

    // i. Получить количество элементов
    public int get_count() {
        return clients.size();

    }


    public void printAllClients() {
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    public static void main(String[] args) {
        Client_rep_json repository = new Client_rep_json("clients.json");

        // Добавляем тестовых клиентов, если файл пустой
        if (repository.get_count() == 0) {
            repository.addClient("Иван", "Петров", "+79991112233", "ivan@mail.ru");
            repository.addClient("Мария", "Сидорова", "+79992223344", "maria@yandex.ru");
            repository.addClient("Алексей", "Козлов", "+79993334455", "alex@google.com");
            repository.saveToFile();
        }

        System.out.println("Всего клиентов: " + repository.get_count());

        // Получение по ID
        Client client = repository.getById(1);
        if (client != null) {
            System.out.println("Клиент с ID=1: " + client);
        }

        // Получаем первые 2 кратких клиента
        List<ClientLITTLE> shortList = repository.get_k_n_short_list(2, 1);
        System.out.println("Первые 2 кратких клиента:");
        for (ClientLITTLE shortClient : shortList) {
            System.out.println(shortClient.toShortString());
        }

        // Сортировка по фамилии
        repository.sortByField("lastname");
        System.out.println("После сортировки по фамилии:");
        repository.printAllClients();

        // Тестируем обновление
        repository.updateClientEmail(2, "maria.new@company.ru");
        System.out.println("После обновления email у клиента ID=2:");
        System.out.println(repository.getById(2));

         //Тестируем удаление
        repository.deleteClient(99);
        System.out.println("После удаления клиента с ID=3:");
        repository.printAllClients();

        // ДОБАВЛЯЕМ НОВОГО КЛИЕНТА
        repository.addClient("Екатерина", "Волкова", "+79994445566", "ekaterina@mail.ru");
        System.out.println("Добавлен новый клиент: Екатерина Волкова");

        // ДОБАВЛЯЕМ КЛИЕНТА С ИСПОЛЬЗОВАНИЕМ ClientLITTLE
        ClientLITTLE newClientInfo = new ClientLITTLE(0, "Анна", "Морозова");
        repository.addClient(newClientInfo, "+79996667788", "anna.morozova@company.com");
        System.out.println("Добавлен новый клиент: Анна Морозова");

        // Сохраняем изменения
        repository.saveToFile();
    }
}
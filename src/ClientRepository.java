import java.util.ArrayList;
import java.util.List;

public abstract class ClientRepository {
    protected List<Client> clients;

    // Конструктор
    public ClientRepository() {
        this.clients = new ArrayList<>();
    }

    // Абстрактные методы
    public abstract void loadFromFile();
    public abstract void saveToFile();
    public abstract void saveToFile(String customFilePath);


    // c. Получить объект по ID
    public Client getById(int id) {
        for (Client client : clients) {
            if (client.getClientId() == id) {
                return client;
            }
        }
        throw new IllegalArgumentException("Клиент с ID " + id + " не найден");
    }

    // d. Получить список k по счету n объектов класса short
    public List<ClientLITTLE> get_k_n_short_list(int k, int n) {
        List<ClientLITTLE> result = new ArrayList<>();
        int startIndex = (n - 1) * k;

        if (startIndex < 0 || startIndex >= clients.size()) {
            return result;
        }

        int endIndex = Math.min(startIndex + k, clients.size());

        for (int i = startIndex; i < endIndex; i++) {
            Client client = clients.get(i);
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
                clients.sort(java.util.Comparator.comparing(Client::getLastName));
                break;
            case "firstname":
                clients.sort(java.util.Comparator.comparing(Client::getFirstName));
                break;
            case "id":
                clients.sort(java.util.Comparator.comparingInt(Client::getClientId));
                break;
            case "email":
                clients.sort(java.util.Comparator.comparing(Client::getEmail));
                break;
            default:
                throw new IllegalArgumentException("Неизвестное поле для сортировки: " + fieldName);
        }
    }

    // ПРОВЕРКА НА УНИКАЛЬНОСТЬ НОМЕРА И ИМЕЙЛА КЛИЕНТА
    protected Client findClientByPhone(String phone) {
        for (Client client : clients) {
            if (client.getPhone().equals(phone)) {
                return client;
            }
        }
        return null;
    }

    protected Client findClientByEmail(String email) {
        for (Client client : clients) {
            if (client.getEmail().equals(email)) {
                return client;
            }
        }
        return null;
    }

    // Валидация уникальности клиента
    private void validateUniqueContacts(String phone, String email) {

        Client phoneClient = findClientByPhone(phone);
        if (phoneClient != null) {
            throw new IllegalArgumentException(
                    "Телефон " + phone + " уже используется клиентом: " + phoneClient
            );
        }

        Client emailClient = findClientByEmail(email);
        if (emailClient != null) {
            throw new IllegalArgumentException(
                    "Email " + email + " уже используется клиентом: " + emailClient
            );
        }
    }

    // f. Добавить объект в список (при добавлении сформировать новый ID)
    public void addClient(ClientLITTLE clientInfo, String phone, String email) {
        validateUniqueContacts(phone, email);

        int newId = generateNextId();
        Client newClient = new Client(
                new ClientLITTLE(newId, clientInfo.getFirstName(), clientInfo.getLastName()),
                phone,
                email
        );
        clients.add(newClient);
    }

    public void addClient(String firstName, String lastName, String phone, String email) {
        validateUniqueContacts(phone, email);

        int newId = generateNextId();
        ClientLITTLE clientInfo = new ClientLITTLE(newId, firstName, lastName);
        Client newClient = new Client(clientInfo, phone, email);
        clients.add(newClient);
    }

    // g. Заменить элемент списка по ID
    public void updateClient(int id, String firstName, String lastName, String phone, String email) {
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            if (client.getClientId() == id) {
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

    public void updateClientEmail(int id, String newEmail) {
        updateClient(id, null, null, null, newEmail);
    }

    public void updateClientPhone(int id, String newPhone) {
        updateClient(id, null, null, newPhone, null);
    }

    public void updateClientFirstName(int id, String newFirstName) {
        updateClient(id, newFirstName, null, null, null);
    }

    public void updateClientLastName(int id, String newLastName) {
        updateClient(id, null, newLastName, null, null);
    }

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

    // Вспомогательные методы
    protected int generateNextId() {
        if (clients.isEmpty()) {
            return 1;
        }
        int maxId = clients.stream()
                .mapToInt(Client::getClientId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

    public void printAllClients() {
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }
}

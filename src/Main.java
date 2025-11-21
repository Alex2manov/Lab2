//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        ClientRepository jsonRepo = new Client_rep_json("clients.json");
        testRepository(jsonRepo, "JSON");

        ClientRepository yamlRepo = new Client_rep_yaml("clients.yaml");
        testRepository(yamlRepo, "YAML");

        jsonRepo.saveToFile();
        yamlRepo.saveToFile();
        System.out.println("\nДанные сохранены в оба формата");
    }

    private static void testRepository(ClientRepository repo, String format) {
        System.out.println("Всего клиентов в " + format + ": " + repo.get_count());

        // Получение по ID
        Client client = repo.getById(1);
        System.out.println("Клиент с ID=1: " + client);

        // Пагинация
        var shortList = repo.get_k_n_short_list(2, 1);
        System.out.println("Первые 2 кратких клиента:");
        for (var shortClient : shortList) {
            System.out.println("  - " + shortClient.toShortString());
        }

        // Добавление нового клиента
        repo.addClient("Новый", "Клиент", "+79940000000", "new240@client.ru");
        System.out.println("Добавлен новый клиент. Теперь всего: " + repo.get_count());

        // Сортировка
        repo.sortByField("lastname");
        System.out.println("После сортировки по фамилии:");
        repo.printAllClients();

    }
}

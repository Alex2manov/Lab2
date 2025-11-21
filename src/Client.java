import org.json.JSONObject;
import java.util.Objects;
import java.util.regex.Pattern;

public class Client extends ClientLITTLE {
    private String phone;
    private String email;

    private static final int MAX_PHONE_LENGTH = 20;
    private static final int MAX_EMAIL_LENGTH = 255;
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9\\s\\-\\(\\)]{7,20}$"
    );
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // 1. ОСНОВНОЙ КОНСТРУКТОР
    public Client(ClientLITTLE little, String phone, String email) {
        super(little.getClientId(), little.getFirstName(), little.getLastName());
        validatePhone(phone);
        validateEmail(email);
        this.phone = phone;
        this.email = email;
    }

    // 2. КОНСТРУКТОР ИЗ СТРОКИ
    public Client(String data) {
        this(
                new ClientLITTLE(
                        Integer.parseInt(data.split(";")[0].trim()),
                        data.split(";")[1].trim(),
                        data.split(";")[2].trim()
                ),
                data.split(";")[3].trim(),
                data.split(";")[4].trim()
        );
    }

    // 3. КОНСТРУКТОР ИЗ JSON
    public Client(JSONObject json) {
        this(
                new ClientLITTLE(
                        json.getInt("ClientId"),
                        json.getString("firstName"),
                        json.getString("lastName")
                ),
                json.getString("phone"),
                json.getString("email")
        );
    }

    // Полная строка
    @Override
    public String toString() {
        return String.format("Client{id=%d, firstName='%s', lastName='%s', phone='%s', email='%s'}",
                clientId, firstName, lastName, phone, email);
    }

    // Сравнение объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        if (!super.equals(o)) return false;

        Client other = (Client) o;
        return Objects.equals(phone, other.phone) &&
                Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), phone, email);
    }

    // Геттеры
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Валидация телефона
    private void validatePhone(String phone) {
        validateNotNullOrBlank(phone, "Телефон");
        validateMaxLength(phone, MAX_PHONE_LENGTH, "Телефон");
        validatePattern(phone, PHONE_PATTERN, "Неверный формат телефона");

        String digitsOnly = phone.replaceAll("\\D", "");
        if (digitsOnly.length() != 11) {
            throw new IllegalArgumentException("Номер телефона должен содержать ровно 11 цифр");
        }
        if (!digitsOnly.startsWith("7") && !digitsOnly.startsWith("8")) {
            throw new IllegalArgumentException("Российский номер должен начинаться с 7 или 8");
        }
    }

    // Валидация email
    private void validateEmail(String email) {
        if (email != null && !email.isBlank()) {
            validateMaxLength(email, MAX_EMAIL_LENGTH, "Email");
            validatePattern(email, EMAIL_PATTERN, "Неверный формат email");
        }
    }

    // Методы валидации
    private void validateNotNullOrBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " не может превышать " + maxLength + " символов");
        }
    }

    private void validatePattern(String value, Pattern pattern, String errorMessage) {
        if (value != null && !pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
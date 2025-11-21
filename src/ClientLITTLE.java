import java.util.Objects;
import java.util.regex.Pattern;

public class ClientLITTLE {
    protected int clientId;
    protected String firstName;
    protected String lastName;

    private static final int MAX_NAME_LENGTH = 100;
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-Zа-яА-ЯёЁ\\s\\-']{2,100}$"
    );

    // Конструкторы
    public ClientLITTLE(int clientId, String firstName, String lastName) {
        validateId(clientId);
        validateName(firstName, "Имя");
        validateName(lastName, "Фамилия");

        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Краткое строковое представление
    public String toShortString() {
        String firstInitial = firstName == null || firstName.isEmpty() ? "" :
                firstName.charAt(0) + ".";
        String middleInitial = "";

        return String.format("%s %s%s", lastName, firstInitial, middleInitial).trim();
    }

    @Override
    public String toString() {
        return toShortString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientLITTLE)) return false;
        ClientLITTLE other = (ClientLITTLE) o;
        return this.toShortString().equals(other.toShortString());
    }

    @Override
    public int hashCode() {
        return toShortString().hashCode();
    }

    // Геттеры
    public int getClientId() { return clientId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

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

    private void validateName(String name, String fieldName) {
        validateNotNullOrBlank(name, fieldName);
        validateMaxLength(name, MAX_NAME_LENGTH, fieldName);
        validatePattern(name, NAME_PATTERN, fieldName + " содержит недопустимые символы");
    }

    private void validateId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID клиента не может быть отрицательным");
        }
    }
}
package faang.school.achievement.exceptions;

public class MessageMappingException extends RuntimeException {
    public MessageMappingException(String message) {
        super("MessageMappingException: " + message);
    }
}

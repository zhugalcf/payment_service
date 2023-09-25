package faang.school.paymentservice.messaging;

public interface MessagePublisher<E> {
    void publish(E event);
}

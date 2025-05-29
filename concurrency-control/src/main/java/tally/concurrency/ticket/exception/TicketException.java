package tally.concurrency.ticket.exception;

public class TicketException extends RuntimeException {
    public TicketException(String message) {
        super(message);
    }

    public static class NotEnoughTicketCodeException extends TicketException {
        public NotEnoughTicketCodeException() {
            super("Not enough ticket code.");
        }
    }

    public static class NoAvailableTicketCodeException extends TicketException {
        public NoAvailableTicketCodeException() {
            super("No available ticket code.");
        }
    }

    public static class TicketCodeUseFailedException extends TicketException {
        public TicketCodeUseFailedException() {
            super("Ticket code use failed.");
        }
    }
}

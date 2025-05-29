package tally.concurrency.ticket.model;

import tally.concurrency.ticket.domain.TicketCode;

public class TicketInfo {

    public static class TicketUsedInfo {
        private final String code;

        public TicketUsedInfo(final String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static TicketUsedInfo of(final TicketCode code) {
            return new TicketUsedInfo(code.getCode());
        }
    }
}

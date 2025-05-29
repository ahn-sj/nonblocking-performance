package tally.concurrency.ticket.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_codes")
@EntityListeners(AuditingEntityListener.class)
public class TicketCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_code_id")
    private Long id;

    private Long userId;

    private String code;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    protected TicketCode() {}

    public static TicketCode issue(final String code) {
        Assert.notNull(code, "code must not be null");
        final TicketCode ticketCode = new TicketCode();
        ticketCode.code = code;
        ticketCode.status = TicketStatus.ISSUED;
        return ticketCode;
    }

    public void use(final Long userId) {
        Assert.notNull(userId, "userId must not be null");
        if (this.userId != null) {
            throw new IllegalStateException("Ticket code already used.");
        }
        this.userId = userId;
        this.status = TicketStatus.USED;
    }

    public String getCode() {
        return code;
    }

    public void unused() {
        if (this.status != TicketStatus.ISSUED) {
            throw new IllegalStateException("Ticket code is not in unused state.");
        }
        this.status = TicketStatus.UNUSED;
    }
}

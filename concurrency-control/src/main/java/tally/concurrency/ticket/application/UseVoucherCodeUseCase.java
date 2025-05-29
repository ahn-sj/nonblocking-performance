package tally.concurrency.ticket.application;

import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import tally.concurrency.ticket.domain.TicketCode;
import tally.concurrency.ticket.domain.TicketStatus;
import tally.concurrency.ticket.model.TicketInfo;
import tally.concurrency.ticket.persistence.TicketCodeJpaRepository;

import java.util.Objects;
import java.util.Optional;

import static tally.concurrency.ticket.exception.TicketException.NotEnoughTicketCodeException;

@Component
public class UseVoucherCodeUseCase {
    private final TicketCodeJpaRepository ticketCodeJpaRepository;

    public UseVoucherCodeUseCase(final TicketCodeJpaRepository ticketCodeJpaRepository) {
        this.ticketCodeJpaRepository = Objects.requireNonNull(ticketCodeJpaRepository);
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public TicketInfo.TicketUsedInfo execute(
            Long userId
    ) {
        final Optional<TicketCode> firstUnusedTicket = ticketCodeJpaRepository.findTopByStatusOrderByCreatedAtDesc(TicketStatus.UNUSED);
        return firstUnusedTicket
                .map(code -> {
                    System.out.println("티켓 코드 사용: " + code.getCode());
                    code.use(userId);
                    return TicketInfo.TicketUsedInfo.of(code);
                })
                .orElseThrow(NotEnoughTicketCodeException::new);
    }

    @Recover
    public TicketInfo.TicketUsedInfo recover(
            ObjectOptimisticLockingFailureException exception,
            Long userId
    ) {
        throw new NotEnoughTicketCodeException();
    }

    @Transactional
    public TicketInfo.TicketUsedInfo executeV2(
            Long userId
    ) {
        ticketCodeJpaRepository.markAsUsed(userId);

    }
}

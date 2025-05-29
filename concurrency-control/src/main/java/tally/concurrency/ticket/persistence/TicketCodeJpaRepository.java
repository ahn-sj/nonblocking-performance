package tally.concurrency.ticket.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tally.concurrency.ticket.domain.TicketCode;
import tally.concurrency.ticket.domain.TicketStatus;

import java.util.Optional;

public interface TicketCodeJpaRepository extends JpaRepository<TicketCode, Long> {
    Optional<TicketCode> findTopByStatusOrderByCreatedAtDesc(TicketStatus status);

    @Modifying
    @Query("UPDATE TicketCode t " +
            "SET t.status = 'USED', " +
            "t.userId = :userId " +
            "WHERE t.id = :id " +
            "AND t.status = 'UNUSED'")
    int markAsUsed(@Param("id") Long userid, @Param("userId") Long userId);
}

package tally.concurrency.ticket.concurrency;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tally.concurrency.ticket.application.UseVoucherCodeUseCase;
import tally.concurrency.ticket.domain.TicketCode;
import tally.concurrency.ticket.model.TicketInfo;
import tally.concurrency.ticket.persistence.TicketCodeJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class ConcurrencyTicketCodeTest {

    @Autowired
    protected TicketCodeJpaRepository ticketCodeJpaRepository;

    @Autowired
    protected UseVoucherCodeUseCase useCase;

    @Autowired
    protected EntityManagerFactory emf;

    @DisplayName("동시에 여러 요청이 들어와도 중복 없이 티켓 코드가 사용된다")
    @Test
    void concurrentTicketUseTest() throws InterruptedException {
        // given: 미사용 티켓 1개만 저장된 상태
        티켓_코드_생성(1);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<TicketInfo.TicketUsedInfo>> results = new ArrayList<>();

        // when: 여러 스레드가 동시에 요청
        for (int i = 0; i < threadCount; i++) {
            final long userId = i + 1L;
            Future<TicketInfo.TicketUsedInfo> future = executor.submit(() -> {
                try {
                    return useCase.execute(userId);
                } catch (Exception e) {
                    return null;
                } finally {
                    latch.countDown();
                }
            });
            results.add(future);
        }

        latch.await();
        executor.shutdown();

        // then: 단 하나의 결과만 성공, 나머지는 실패(null)
        List<TicketInfo.TicketUsedInfo> successResults = results.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        assertThat(successResults).hasSize(1);
        assertThat(successResults.get(0).getCode()).isEqualTo("TICKET-1");
    }

    @DisplayName("동시에 여러 요청이 들어와도 중복 없이 각각 티켓 코드가 사용된다")
    @Test
    void concurrentMultiTicketUseTest() throws InterruptedException {
        // given: 미사용 티켓 10개만 저장된 상태
        티켓_코드_생성(10);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<TicketInfo.TicketUsedInfo>> results = new ArrayList<>();

        // when: 여러 스레드가 동시에 요청
        for (int i = 0; i < threadCount; i++) {
            final long userId = i + 1L;
            Future<TicketInfo.TicketUsedInfo> future = executor.submit(() -> {
                try {
                    return useCase.execute(userId);
                } catch (Exception e) {
                    return null;
                } finally {
                    latch.countDown();
                }
            });
            results.add(future);
        }

        latch.await();
        executor.shutdown();
    }

    private void 티켓_코드_생성(final int count) {
        for (int i = 1; i <= count; i++) {
            TicketCode ticket = TicketCode.issue("TICKET-" + i);
            ticket.unused();
            ticketCodeJpaRepository.save(ticket);
        }
    }
}

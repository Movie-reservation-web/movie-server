package study.movie.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.movie.domain.payment.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByMerchantUid(String merchantUid);
}

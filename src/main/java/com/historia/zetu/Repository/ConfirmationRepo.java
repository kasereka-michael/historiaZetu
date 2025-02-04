package com.historia.zetu.Repository;

import com.historia.zetu.model.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConfirmationRepo extends JpaRepository<Confirmation, Long> {
    @Query("SELECT c FROM Confirmation c " +
            "WHERE c.users.id = :userId " +
            "AND c.otp = :otp")
    Confirmation findByUsersIdAndOtp(@Param("userId") Long userId, @Param("otp") String otp);
}

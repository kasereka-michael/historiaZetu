package com.historia.zetu.Repository;

import com.historia.zetu.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.email = :email AND u.active = true")
    Optional<Users> findByEmailAndActiveIsTrue(@Param("email") String email);

    Optional<Users> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.active = true WHERE u.id = :userId")
    int setActiveTrue(@Param("userId") Long userId);

}

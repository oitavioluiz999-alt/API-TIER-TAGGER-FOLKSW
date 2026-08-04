package com.tiertagger.api.repository;

import com.tiertagger.api.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("UPDATE Session s SET s.revoked = true WHERE s.refreshToken = :token")
    void revokeByRefreshToken(@Param("token") String token);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt < :now OR s.revoked = true")
    int deleteExpired(@Param("now") OffsetDateTime now);
}


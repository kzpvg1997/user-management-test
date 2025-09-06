package co.com.globalogic.usermanagement.infrastructure.repository.autentication;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UserAuthenticationTableRepository extends JpaRepository<UserAuthenticationTable,Long> {

    @Query("SELECT ua FROM UserAuthenticationTable ua WHERE ua.user.id = :userId")
    Optional<UserAuthenticationTable> findByUser(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserAuthenticationTable ua " +
            "SET ua.token = :token, ua.lastLogin = :lastLogin " +
            "WHERE ua.user.id = :userId")
    int updateAuthentication(@Param("userId") String userId,
                             @Param("token") String token,
                             @Param("lastLogin") LocalDateTime lastLogin);
}

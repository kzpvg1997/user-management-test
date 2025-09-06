package co.com.globalogic.usermanagement.infrastructure.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserTableRepository extends JpaRepository<UserTable,String> {

    @Query("SELECT u FROM UserTable u WHERE u.email = :email")
    Optional<UserTable> findByEmail(@Param("email") String email);
}

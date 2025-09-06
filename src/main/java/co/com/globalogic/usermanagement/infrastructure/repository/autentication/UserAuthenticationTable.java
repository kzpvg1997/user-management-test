package co.com.globalogic.usermanagement.infrastructure.repository.autentication;

import co.com.globalogic.usermanagement.infrastructure.repository.user.UserTable;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_authentication")
public class UserAuthenticationTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lastLogin;

    @Column(length = 1024)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserTable user;
}


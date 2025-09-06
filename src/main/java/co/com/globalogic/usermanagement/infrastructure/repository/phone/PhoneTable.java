package co.com.globalogic.usermanagement.infrastructure.repository.phone;


import co.com.globalogic.usermanagement.infrastructure.repository.user.UserTable;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class PhoneTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "country_code")
    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTable user;
}

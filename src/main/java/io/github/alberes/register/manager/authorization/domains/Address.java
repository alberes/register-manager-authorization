package io.github.alberes.register.manager.authorization.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "address")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "public_area", length = 100, nullable = false)
    private String publicArea;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "additional_address",length = 100, nullable = true)
    private String additionalAddress;

    @Column(name = "neighborhood", length = 100, nullable = false)
    private String neighborhood;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @Column(name = "state", length = 50, nullable = false)
    private String state;

    @Column(name = "zip_code", length = 8, nullable = false)
    private String zipCode;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;
}

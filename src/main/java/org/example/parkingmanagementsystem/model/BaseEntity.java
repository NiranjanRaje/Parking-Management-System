package org.example.parkingmanagementsystem.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Data
//@Getter, @Setter
@NoArgsConstructor
@MappedSuperclass
@AllArgsConstructor
public class BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;


    @Column(name = "version", nullable = false)
    private int version;


    @Column(name = "active_flag", nullable = false)
    private boolean activeFlag;


    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @PrePersist
    protected void onCreate() {
//        created = new Date();
//        lastUpdated = new Date();
//        version = 1;
        activeFlag = true;
//        isApproved = false;
    }

//    @PreUpdate
//    protected void onUpdate() {
//        lastUpdated = new Date();
//    }
}

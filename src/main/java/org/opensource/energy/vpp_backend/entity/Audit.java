package org.opensource.energy.vpp_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    @CreatedDate
    @JsonIgnore
    @Column(name = "created_at")
    protected Instant createdAt;

    @LastModifiedDate
    @JsonIgnore
    @Column(name = "modified_at")
    protected Instant modifiedAt;

    public Long getCreatedAtTimestamp() {
        return createdAt != null ? createdAt.getEpochSecond() : 0L;
    }

    public Long getModifiedAtTimestamp() {
        return modifiedAt != null ? modifiedAt.getEpochSecond() : 0L;
    }
}
package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeline implements Persistable<String> {
    @Id
    private String matchId;

    @Transient
    private boolean isNew = true;

    @OneToOne(mappedBy = "timeline", cascade = CascadeType.ALL, orphanRemoval = true)
    private MetadataTimeLine metadata;

    @OneToOne(mappedBy = "timeline", cascade = CascadeType.ALL, orphanRemoval = true)
    private InfoTimeLine info;

    @Override
    public String getId() {
        return matchId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}

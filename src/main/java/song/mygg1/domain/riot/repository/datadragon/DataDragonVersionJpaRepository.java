package song.mygg1.domain.riot.repository.datadragon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.datadragon.DataDragonVersion;

@Repository
public interface DataDragonVersionJpaRepository extends JpaRepository<DataDragonVersion, String> {

}

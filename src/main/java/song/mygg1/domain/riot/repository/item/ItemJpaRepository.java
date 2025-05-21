package song.mygg1.domain.riot.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.item.Item;

import java.util.List;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Integer> {
    @Query("select i " +
            " from Item i " +
            " join i.maps m " +
            "where key(m) = :mapId " +
            "  and m = true ")
    List<Item> findItemsByMap(@Param("mapId") String mapId);
}

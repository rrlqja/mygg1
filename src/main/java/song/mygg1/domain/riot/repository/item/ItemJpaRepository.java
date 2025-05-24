package song.mygg1.domain.riot.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.item.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Integer> {
    @Query("select i " +
            " from Item i " +
            " join i.maps m " +
            "where key(m) = :mapId " +
            "  and m = true ")
    List<Item> findItemsByMap(@Param("mapId") String mapId);

    @Query("select distinct i" +
            " from Item i " +
            " join i.maps m11 " +
            " join i.maps m12 " +
            " join i.maps m30 " +
            "where i.into is empty " +
            "  and i.from is not empty " +
            "  and key(m11) = 11 and m11 = true " +
            "  and key(m12) = 12 and m12 = true " +
            "  and key(m30) = 30 and m30 = false ")
    List<Item> findCoreItemList();

    @Query("select i.name " +
            " from Item i " +
            "where i.id = :id")
    Optional<String> findItemNameById(@Param("id") Integer id);
}

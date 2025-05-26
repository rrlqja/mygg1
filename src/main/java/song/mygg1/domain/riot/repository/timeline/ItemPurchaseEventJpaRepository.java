package song.mygg1.domain.riot.repository.timeline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.timeline.events.ItemPurchaseEvent;

import java.util.List;

@Repository
public interface ItemPurchaseEventJpaRepository extends JpaRepository<ItemPurchaseEvent, Long> {
    @Query("select ipe " +
            " from ItemPurchaseEvent ipe " +
            " join fetch ipe.frame f " +
            "where f.id.matchId in :matchIds " +
            "order by ipe.timestamp asc")
    List<ItemPurchaseEvent> findByMatchIds(@Param("matchIds") List<String> matchIds);
}

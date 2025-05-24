package song.mygg1.domain.riot.dto.match.championbuild;

import lombok.Data;

@Data
public class ItemStatCounter {
    private Integer purchaseCount = 0;
    private Integer winCount = 0;

    public void incrementPurchase() {
        this.purchaseCount++;
    }

    public void incrementWin() {
        this.winCount++;
    }
}

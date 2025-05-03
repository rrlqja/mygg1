package song.mygg1.domain.riot.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum QueueType {
    SOLO_RANK(420, "솔로 랭크"),
    FLEX_RANK(440, "자유 랭크"),
    NORMAL_BLIND(430, "일반 게임"),
    ARAM(450, "무작위 총력전"),
    ARENA(1700, "아레나"),
    UNKNOWN(-1, "알 수 없음")
    ;

    private final int id;
    private final String displayName;

    private static final Map<Integer, QueueType> LOOKUP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(q -> q.id, q -> q));

    QueueType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static QueueType fromId(int id) {
        return LOOKUP.getOrDefault(id, UNKNOWN);
    }
}

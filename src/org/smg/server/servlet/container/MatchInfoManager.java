
package org.smg.server.servlet.container;

import java.util.Map;

import org.smg.server.servlet.container.MatchInfo.GameOverReason;

import com.google.appengine.labs.repackaged.com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList;

public class MatchInfoManager {
    private static MatchInfoManager instance = null;

    private Map<Integer, MatchInfo> matchInfoMap;

    private MatchInfoManager() {
        matchInfoMap = Maps.newConcurrentMap();

        // For test only.
        generateTestData();
    }

    private void generateTestData() {
        matchInfoMap.put(123123, new MatchInfo(123123, 456, ImmutableList.<Integer> of(42, 43), 42,
                0, GameOverReason.NOT_OVER));
    }

    public static MatchInfoManager getInstance() {
        if (instance == null) {
            instance = new MatchInfoManager();
        }
        return instance;
    }

    public MatchInfo getMatchInfo(int matchId) {
        return matchInfoMap.get(matchId);
    }
}

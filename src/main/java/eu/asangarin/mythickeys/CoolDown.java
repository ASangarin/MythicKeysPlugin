package eu.asangarin.mythickeys;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CoolDown {

    /**
     * ConcurrentHashMap(UUID, Map()): UUID is player's uuid, Map↓ <br>
     * Map(String, Long): String is KeyID, Long is whenever the cooldown ends.
     */
    private static final ConcurrentHashMap<UUID, Map<String, Long>> cdMap = new ConcurrentHashMap<>();

    public static boolean isCooling(UUID playerUUID, String keyID) {
        return getKeyMap(playerUUID).getOrDefault(keyID, 0L) >= now();
    }

    public static Long getCdLeft(UUID playerUUID, String keyID) {
        return getCdEnd(playerUUID, keyID) - now();
    }

    /**
     * @return The end time of cd in ms
     */
    public static Long getCdEnd(UUID playerUUID, String keyID) {
        return getKeyMap(playerUUID).getOrDefault(keyID, 0L);
    }

    public static void setCdToMsLater(UUID playerUUID, String keyID, Long msLater) {
        setCdToTimestamp(playerUUID, keyID, now() + msLater);
    }

    public static void setCdToTimestamp(UUID playerUUID, String keyID, Long msTimestamp) {
        Map<String, Long> keyMap = getKeyMap(playerUUID);
        keyMap.put(keyID, msTimestamp);
        cdMap.put(playerUUID, keyMap);
    }

    private static Map<String, Long> getKeyMap(UUID playerUUID) {
        return cdMap.computeIfAbsent(playerUUID, k -> new HashMap<>());
    }

    private static long now() {
        return System.currentTimeMillis();
    }

}

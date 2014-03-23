package org.smg.server.servlet.container;

import java.util.List;
import org.smg.server.database.DatabaseDriver;
import com.google.appengine.api.datastore.Entity;

public class ContainerVerification {
  
  /**
   * Verify if a accessSignature is associated with a playerId
   * @param accessSignature
   * @param playerId
   * @return
   */
  public static boolean accessSignatureVerify(String accessSignature, long playerId) {
    Entity entity = DatabaseDriver.getEntityByKey(ContainerConstants.PLAYER, playerId);
    if (entity.getProperty(ContainerConstants.ACCESS_SIGNATURE).equals(accessSignature)) {
      return true;
    }
    return false;
  }
  
  /**
   * Verify if a accessSignature is associated with one of playerId in list playerIds
   * @param accessSignature
   * @param playerIds
   * @return
   */
  public static boolean accessSignatureVerify(String accessSignature, List<Long> playerIds) {
    for (long playerId : playerIds) {
      if (accessSignatureVerify(accessSignature,playerId)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Verify if all playerIds is valid
   * @param playerIds
   * @return
   */
  public static boolean playerIdsVerify(List<Long> playerIds) {
    for (Long playerId : playerIds) {
      if (!playerIdVerify(playerId)) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Verify if a playerId is valid
   * @param playerId
   * @return
   */
  public static boolean playerIdVerify(long playerId) {
    Entity entity = DatabaseDriver.getEntityByKey(ContainerConstants.PLAYER, playerId);
    if (entity == null) {
      return false;
    }
    return true;
  }
  
  /**
   * Verify if a matchId is valid
   * @param matchId
   * @return
   */
  public static boolean matchIdVerify(long matchId) {
    Entity entity = DatabaseDriver.getEntityByKey(ContainerConstants.MATCH, matchId);
    if (entity == null) {
      return false;
    }
    return true;
  }
  
  /**
   * Verify if a gameId is valid
   * @param gameId
   * @return
   */
  public static boolean gameIdVerify(long gameId) {
    Entity entity = DatabaseDriver.getEntityByKey(ContainerConstants.GAME, gameId);
    if (entity == null) {
      return false;
    }
    return true;
  }
}

package org.smg.server.servlet.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smg.server.database.DatabaseDriver;
import org.smg.server.database.models.Player;
import org.smg.server.database.models.Player.PlayerProperty;
import org.smg.util.AccessSignatureUtil;
import org.smg.util.CORSUtil;
import org.smg.util.JSONUtil;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * Function: Insert a player Url pattern: /players
 * 
 * @author Archer
 * 
 *         TODO combine this with /players/{playerId}
 */
@SuppressWarnings("serial")
public class PlayersServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		CORSUtil.addCORSHeader(resp);
		resp.setContentType("text/plain");
		String playerIdStr = null;
		if (req.getPathInfo().length() != 0 && req.getPathInfo() != null)
			playerIdStr = req.getPathInfo().substring(1);
		else
			playerIdStr = null;
		long playerId = Long.parseLong(playerIdStr);
		JSONObject returnValue = new JSONObject();
		Map<String, Object> map = req.getParameterMap();
		try {
			Player player = DatabaseDriver.getPlayerById(playerId);
		} catch (EntityNotFoundException e) {
			try {
				returnValue.put("ERROR", "WRONG_PLAYE_ID");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// resp.getWriter().println("Player: " + playerId);
		// resp.getWriter().println("Parameter: ");
		// for (String key : map.keySet()) {
		// resp.getWriter().print("    " + key + ":" + map.get(key));
		// }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter res = resp.getWriter();
		CORSUtil.addCORSHeader(resp);

		BufferedReader br = new BufferedReader(new InputStreamReader(
				req.getInputStream()));
		String json = new String();
		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = br.readLine()) != null)
			buffer.append(line);
		json = buffer.toString();
		Map<String, Object> map = JSONUtil.parse(json);
		String originalString = (String) map.get("PASSWORD");
		JSONObject returnValue = new JSONObject();
		if (originalString.length() < 6) {
			try {
				returnValue.put("ERROR", "PASSWORD_TOO_SHORT");
				returnValue.write(resp.getWriter());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		Player player = JSONUtil.jSON2Player(json);
		String saveResult = DatabaseDriver.savePlayer(player);
		if (saveResult.equals("UPDATED_PLAYER")) {
			try {
				returnValue.put("PLAYERID",
						player.getProperty(PlayerProperty.PLAYERID));
				returnValue.put("ACCESSSIGNATURE",
						player.getProperty(PlayerProperty.ACCESSSIGNATURE));
				returnValue.write(resp.getWriter());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else if (saveResult.equals("WRONG_ACCESS_SIGNATURE")) {
			try {
				returnValue.put("ERROR", "WRONG_ACCESS_SIGNATURE");
				returnValue.write(resp.getWriter());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else if (saveResult.equals("WRONG_PLAYER_ID")) {
			try {
				returnValue.put("ERROR", "WRONG_PLAYER_ID");
				returnValue.write(resp.getWriter());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else if (saveResult.equals("EMAIL_EXITSTS")) {
			try {
				returnValue.put("ERROR", "EMAIL_EXISTS");
				returnValue.write(resp.getWriter());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else if (saveResult.startsWith("SUCCESS:")) {
			try {

				returnValue.put("PLAYERID", saveResult.split(":")[1]);
				returnValue.put("ACCESSSIGNATURE", saveResult.split(":")[2]);
				returnValue.write(resp.getWriter());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		return;

	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		CORSUtil.addCORSHeader(resp);
	}

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		CORSUtil.addCORSHeader(resp);
	}
}
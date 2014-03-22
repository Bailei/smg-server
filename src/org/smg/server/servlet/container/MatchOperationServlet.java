
package org.smg.server.servlet.container;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.smg.server.servlet.container.GameApi.GameState;
import org.smg.server.servlet.container.GameApi.Operation;
import org.smg.util.CORSUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MatchOperationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPost(req, resp);
        PrintWriter res = resp.getWriter();
        CORSUtil.addCORSHeader(resp);
        resp.setHeader("Content-Type", "application/json");
        try {
            JsonObject jobject = new JsonParser().parse(req.getParameter("")).getAsJsonObject();
            JsonElement opsLstJson = jobject.get(Constants.JSON_OPERATIONS);
            JsonElement accessSingatureJson = jobject.get(Constants.JSON_ACCESS_SIGNATURE);

            // Get GameState for current player.
            GameStateManager gsm = GameStateManager.getInstance();
            String accessSignature = accessSingatureJson.getAsString();
            GameState gameState = gsm.getGameStateByAS(accessSignature);

            // Convert json string to Operation list.
            List<Map<String, String>> operationsList;
            ObjectMapper mapper = new ObjectMapper();

            operationsList = mapper.readValue(opsLstJson.getAsString(),
                    new TypeReference<List<Map<String, String>>>() {
                    });
            List<Operation> operations = GameStateManager
                    .messageToOperationList(operationsList);

            // Generate return data.
            gameState.makeMove(operations);
            int playerId = gsm.getPlayerIdByAccessSignature(accessSignature);
            Map<String, Object> returnState = gameState.getStateForPlayerId(playerId);

            String rtnJson = mapper.writeValueAsString(returnState);
            res.print(rtnJson);
            res.close();
        } catch (Exception e) {
            // Parse failed. Json not valid.
            res.print(Constants.JSON_FAILED);
            res.close();
            return;
        }
    }
}

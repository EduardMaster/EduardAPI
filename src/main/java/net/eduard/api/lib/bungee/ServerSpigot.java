package net.eduard.api.lib.bungee;

import net.eduard.api.lib.database.annotations.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@TableName("network_servers")
public class ServerSpigot implements Serializable {
    private String name = "lobby01";
    private String type = "lobby";
    private String subType = "principal";
    private int max = 80;
    private int count;
    private ServerState state = ServerState.OFFLINE;
    private String host = "localhost";
    private int port = 25565;

    private List<String> players = new ArrayList<>();

    public ServerSpigot(String name) {
        this.name = name;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public ServerState getState() {
        return state;
    }

    public void setState(ServerState state) {
        this.state = state;
    }

    public boolean canConnect() {
        return isOnline() && !isFull() && !isRestarting() && !isGameStarted();
    }


    public boolean isRestarting() {
        return isState(ServerState.RESTARTING);
    }

    private boolean isState(ServerState state) {
        return this.state == state;
    }

    public boolean isGameStarted() {
        return isState(ServerState.IN_GAME);
    }

    public boolean isOffline() {
        return isState(ServerState.OFFLINE);
    }

    public boolean isDisabled() {
        return isState(ServerState.DISABLED);
    }

    public boolean isOnline() {
        return !isOffline() && !isDisabled();
    }

    public boolean isFull() {
        return players.size() == max;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


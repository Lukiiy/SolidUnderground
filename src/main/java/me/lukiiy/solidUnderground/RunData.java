package me.lukiiy.solidUnderground;

import java.util.HashMap;
import java.util.Map;

public class RunData {
    public boolean inHole;
    public Map<String, Long> times = new HashMap<>();

    public String serialize() {
        if (times.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Long> entry : times.entrySet()) sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        return sb.toString();
    }

    public void deserialize(String data) {
        if (data == null || data.isEmpty()) return;

        String[] parts = data.split(";", -1);
        for (String part : parts) {
            if (part.isEmpty()) continue;

            String[] stuff = part.split("=", 2);
            if (stuff.length != 2) continue;

            String key = stuff[0];
            String value = stuff[1];

            try { times.put(key, Long.parseLong(value)); } catch (Exception ignored) {}
        }
    }
}

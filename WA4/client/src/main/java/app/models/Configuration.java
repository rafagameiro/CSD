package app.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Configuration {
    private boolean isBFT;
    private int n;
    private int f;

    public void init() {
        Map<String, String> config = loadConfig();
        String s = config.remove("system.servers.num");
        n = Integer.parseInt(s);
        s = config.remove("system.servers.f");
        f = Integer.parseInt(s);
        s = config.remove("system.bft");
        isBFT = Boolean.parseBoolean(s);
    }

    private Map<String, String> loadConfig() {
        Map<String, String> res = new HashMap<>();

        try {
            String sep = System.getProperty("file.separator");
            String path = "config" + sep + "system.config";
            FileReader fr = new FileReader(path);
            BufferedReader rd = new BufferedReader(fr);
            String line = null;

            while ((line = rd.readLine()) != null) {
                if (!line.startsWith("#")) {
                    StringTokenizer str = new StringTokenizer(line, "=");
                    if (str.countTokens() > 1) {
                        res.put(str.nextToken().trim(), str.nextToken().trim());
                    }
                }
            }

            fr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public int getReplyQuorum() {
        return isBFT ? (int) Math.ceil((double) ((n + f) / 2)) + 1 : (int) Math.ceil((double) (n / 2)) + 1;
    }
}

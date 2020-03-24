package g2.labs.repository;

import com.google.gson.*;
import g2.labs.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.nio.file.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Repository
public class UserRepository {
    private static final int INITIAL_CAPACITY = 100;
    private static final String FILE_LOCATION = ".config/.data/users/users.json";
    private static final String FILE_NOT_FOUND = "The users file does not exist.";

    private static Gson gson;
    private JsonArray usersList;
    private Map<String, String> users;

    public UserRepository() {
        this.users = new HashMap<>(INITIAL_CAPACITY);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void createUser(String username, String password) throws UserAlreadyExistsException {
        String user = users.putIfAbsent(username, password);
        if (user != null) {
            throw new UserAlreadyExistsException();
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("name", username);
        obj.addProperty("password", password);
        usersList.add(obj);
        writeJson();
    }

    public boolean userExists(String username, String password) {
        return password.equals(users.get(username));
    }

    @PostConstruct
    private void loadUsers() {

        FileReader fr;
        try {
            fr = new FileReader(FILE_LOCATION);
            usersList = (new Gson()).fromJson(fr, JsonArray.class);

            Iterator<JsonElement> it = usersList.iterator();
            while(it.hasNext()) {
                JsonObject elem = it.next().getAsJsonObject();

                this.users.put(elem.get("name").getAsString(), elem.get("password").getAsString());
            }

        } catch (Exception e) {
            createDirectories();
        }
    }

    private void createDirectories() {
        
        try {
            
            Path path = Paths.get(FILE_LOCATION);
            Files.createDirectories(path.getParent());
            new File(FILE_LOCATION).createNewFile();
            usersList = new JsonArray();

        } catch(IOException e) {
            System.err.println(FILE_NOT_FOUND);
        }
    }

    private void writeJson() {

        FileWriter fw;
        try {
            fw = new FileWriter(FILE_LOCATION);
            gson.toJson(usersList, fw);
            fw.close();
        } catch(IOException e) {
            System.err.println(FILE_NOT_FOUND);
        }
    }
}

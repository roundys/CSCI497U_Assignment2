import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static final String IP_ADDR = "";

    private static Scanner in = new Scanner(System.in);
    private static HttpClient client = HttpClient.newHttpClient();


    public static void main(String[] args) {
        int registerOrLogin = promptRegisterOrLogin();
        String idToken;
        if (registerOrLogin == 1) {
            register();
        }

        idToken = login();

        int createOrJoin = promptCreateOrJoin();
        if (createOrJoin == 1) {
            createGame(idToken);
        } else {
            joinGame();
        }
    }

    private static int promptRegisterOrLogin() {
        System.out.println("Select one of the following options:");
        System.out.println("\t1) Create a new account");
        System.out.println("\t2) Login to an existing account");
        System.out.print("> ");
        int registerOrLogin = in.nextInt();
        if (registerOrLogin < 1 || registerOrLogin > 2) {
            System.out.println("Type 1 or 2.");
            return promptCreateOrJoin();
        }
        return registerOrLogin;
    }

    private static void register() {
        System.out.println("CREATE A NEW USER:");
        //username
        System.out.print("What's your new username? ");
        String username = in.next();
        //password
        System.out.print("What's your new password? ");
        String password = in.next();
        //email
        System.out.print("What's your email? ");
        String email = in.next();
        //phone_num
        System.out.print("What's your phone number? ");
        String phoneNumber = in.nextLine();

        //Create JSON to send to server
        JSONObject requestJSON = new JSONObject()
                .put("username", username)
                .put("password", password)
                .put("phoneNumber", phoneNumber)
                .put("email", email);
        System.out.println(requestJSON);

        //POST this JSON to the server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IP_ADDR + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJSON.toString()))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("response: " + response.statusCode());
        System.out.println(response.body());
        JSONObject responseJSON = new JSONObject(response.body());
        System.out.println("username = " + responseJSON.get("username"));
        System.out.println("phoneNumber = " + responseJSON.get("phoneNumber"));
    }

    private static String login() {
        System.out.println("LOGIN:");
        //username
        System.out.print("What's your username? ");
        String username = in.next();
        //password
        System.out.print("What's your password? ");
        String password = in.next();

        //Create JSON to send to server
        JSONObject requestJSON = new JSONObject()
                .put("username", username)
                .put("password", password);
        System.out.println(requestJSON);

        //POST this JSON to the server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IP_ADDR + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJSON.toString()))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("response: " + response.statusCode());
        System.out.println(response.body());
        JSONObject responseJSON = new JSONObject(response.body());
        System.out.println("idToken = " + responseJSON.get("idToken"));
        return responseJSON.get("idToken").toString();
    }

    private static int promptCreateOrJoin() {
        System.out.println("Select one of the following options:");
        System.out.println("\t1) Create a game");
        System.out.println("\t2) Join a game");
        System.out.print("> ");
        int createOrJoin = in.nextInt();
        if (createOrJoin < 1 || createOrJoin > 2) {
            System.out.println("Type 1 or 2.");
            return promptCreateOrJoin();
        }
        return createOrJoin;
    }

    private static void createGame(String idToken) {
        System.out.println("CREATING A GAME:");
        System.out.print("What's your opponent's email? ");
        String opponentEmail = in.nextLine();

        //Create JSON to send to server
        JSONObject requestJSON = new JSONObject()
                .put("opponent", opponentEmail);

        //TODO: is email the right thing to be sending? looks like tutorial uses username

        //POST the JSON to the server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IP_ADDR + "/games"))
                .header("Content")
    }

    private static void joinGame() {
        System.out.print("Enter a game ID: ");
        String gameID = in.next();
    }
}

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    //IP address of server
    private static final String IP_ADDR = "";

    //Scanner that reads command line input
    private static Scanner in = new Scanner(System.in);

    private static HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        int registerOrLogin = promptRegisterOrLogin();
        if (registerOrLogin == 1) {
            register();
        }

        User user = login();

        int createOrJoin = promptCreateOrJoin();
        String gameID;
        if (createOrJoin == 1) {
            gameID = createGame(user.idToken);
        } else {
            gameID = joinGame();
        }
        //TODO: Figure out game logic - when to wait, prompt user to quit, etc.
        while (true) {
            displayStatus(gameID, user);
            performMove(gameID, user);
        }
    }

    //Asks user if they want to create a new account, or login to an existing account
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

    //Registers a new user and prompts for data
    private static void register() {
        System.out.println("CREATE A NEW USER:");
        //username
        System.out.print("What's your new username? ");
        String username = in.next();
        //password
        System.out.print("What's your new password? ");
        String password = in.next();

        //TODO (optional): hide password input
        //TODO (optional): second password input, check that the passwords match

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

    //Logs in an existing user
    private static User login() {
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
        User user = new User();
        user.username = username;
        user.password = password;
        user.idToken = responseJSON.get("idToken").toString();
    }

    //Ask user if they want to create a new game or join an existing one
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

    private static String createGame(String idToken) {
        System.out.println("CREATING A GAME:");
        System.out.print("What's your opponent's username? ");
        String opponentUsername = in.nextLine();

        //Create JSON to send to server
        JSONObject requestJSON = new JSONObject()
                .put("opponent", opponentUsername);

        //POST the JSON to the server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IP_ADDR + "/games"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJSON.toString()))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("response: " + response.statusCode());
        System.out.println(response.body());
        JSONObject responseJSON = new JSONObject(response.body());
        System.out.println("gameID = " + responseJSON.get("gameID"));
        return responseJSON.get("gameID").toString();
    }

    //join a game
    private static String joinGame() {
        System.out.print("Enter a game ID: ");
        String gameID = in.next();

        //TODO (optional): Send gameID to server to see if it's real, prompt again if not
        return gameID;
    }

    //get a specified game's status
    private static JSONObject getGameStatus(String gameID) {
        //GET the game status from the server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IP_ADDR + "/games/" + gameID))
                .GET()
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("response: " + response.statusCode());
        System.out.println(response.body());
        JSONObject responseJSON = new JSONObject(response.body());
        return responseJSON;
    }

    //display board
    private static void displayStatus(String gameID, User user) {
        JSONObject gameStatus = getGameStatus(gameID);
        System.out.println("   1   2   3");
        System.out.printf("A: %s | %s | %s\n", gameStatus.get("A1"), gameStatus.get("A2"), gameStatus.get("A3"));
        System.out.println("  -----------");
        System.out.printf("B: %s | %s | %s\n", gameStatus.get("B1"), gameStatus.get("B2"), gameStatus.get("B3"));
        System.out.println("  -----------");
        System.out.printf("C: %s | %s | %s\n\n", gameStatus.get("C1"), gameStatus.get("C2"), gameStatus.get("C3"));
        if (gameStatus.get("lastMoveBy").equals(user.username)) {
            System.out.println("It's not your turn.");
        } else {
            System.out.println("It's your turn.");
        }
    }

    //User plays a move
    private static JSONObject performMove(String gameID, User user) {
        System.out.print("What space do you want to change? ");
        String changedSpace = in.next();
        //TODO (optional): make sure the space the user enters is valid
        System.out.print("What do you want to change that space to? ");
        String changedSpaceValue = in.next();

        //Create JSON to send to server
        JSONObject requestJSON = new JSONObject()
                .put("gameId", gameID)
                .put("user", user.username)
                .put("changedSpace", changedSpace)
                .put("changedSpaceValue", changedSpaceValue);

        //POST the JSON to the server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IP_ADDR + "/games/" + gameID))
                .header("Content-Type", "application/json")
                .header("Authorization", user.idToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestJSON.toString()))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("response: " + response.statusCode());
        System.out.println(response.body());
        JSONObject responseJSON = new JSONObject(response.body());
        return responseJSON;

    }

}

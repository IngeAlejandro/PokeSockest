package proyectosockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(5000);
        System.out.println("Servidor Iniciado");
        try {
            while (true) {
                Game game = new Game();
                Game.Player playerA = game.new Player(listener.accept(), "PIKACHU");
                Game.Player playerB = game.new Player(listener.accept(), "CHARMANDER");
                playerA.setOpponent(playerB);
                playerB.setOpponent(playerA);
                game.currentPlayer = playerA;
                playerA.start();
                playerB.start();
            }
        } finally {
            listener.close();
        }
    }
}

class Game {

    Player currentPlayer;
    private final int[] health = {150, 150};

    public boolean hasWinner() {
        return health[0] <= 0 || health[1] <= 0;
    }

    public synchronized void cambioTurno(int p, int dmg, Player player) {
        health[p] -= dmg;
        currentPlayer = currentPlayer.opponent;
        currentPlayer.ataque(dmg);
    }

    class Player extends Thread {

        String p;
        Socket socket;
        Player opponent;
        BufferedReader input;
        PrintWriter output;

        public Player(Socket socket, String p) {
            this.socket = socket;
            this.p = p;
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + p);
                output.println("LOG Esperando Oponente");
            } catch (IOException e) {
                System.out.println("Jugador Desconectado: " + e);
            }
        }

        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        public void ataque(int dmg) {
            output.println("ATK " + dmg);
            output.println(hasWinner() ? "DEFEAT" : "");
        }

        @Override
        public void run() {
            try {
                output.println("LOG Batalla lista");

                if ("PIKACHU".equals(p)) {
                    output.println("LOG Tu turno");
                } else {
                    output.println("LOG Es turno de tu oponente");
                }

                // Repeatedly get commands from the client and process them.
                while (true) {
                    String command = input.readLine();
                    System.out.println("INPUT " + command);
                    if (command.startsWith("PIKACHU")) {
                        int dmg = Integer.parseInt(command.substring(8));
                        cambioTurno(0, dmg, this);
                        output.println("DMG");
                        output.println(hasWinner() ? "VICTORY" : "");
                    } else if (command.startsWith("CHARMANDER")) {
                        int dmg = Integer.parseInt(command.substring(11));
                        cambioTurno(1, dmg, this);
                        output.println("DMG");
                        output.println(hasWinner() ? "VICTORY" : "");
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Jugador Desconectado: " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }

        }
    }
}

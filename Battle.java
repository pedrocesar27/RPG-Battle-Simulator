import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    // Atributos da classe
    private final List<Character> teamA; // Time do jogador
    private final List<Character> teamB; // Time inimigo
    private final Scanner in;            // Objeto para ler a entrada do jogador

    // Construtor que inicializa a batalha com os times e o scanner
    public Battle(List<Character> teamA, List<Character> teamB, Scanner in) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.in = in;
    }
    
    /**
     * Contém o loop principal da batalha, que executa rounds e turnos
     * até que um dos times seja completamente derrotado.
     */
    public void run() {
        System.out.println("\n--- The Battle Begins! ---");

        int round = 1;
        // O loop continua enquanto ambos os times tiverem pelo menos um membro vivo
        while (isTeamAlive(teamA) && isTeamAlive(teamB)) {
            System.out.println("\n=== Round " + round + " ===");
            printTeams();

            // Pega a ordem de turno de todos os personagens vivos
            List<Character> turnOrder = allAlive();
            for (Character actor : turnOrder) {
                // Pula o turno se o personagem morreu neste mesmo round antes de sua vez
                if (!actor.isAlive()) continue;

                // Determina quem são os aliados e inimigos do personagem atual
                boolean isPlayer = teamA.contains(actor);
                List<Character> allies = isPlayer ? teamA : teamB;
                List<Character> enemies = isPlayer ? teamB : teamA;

                // Polimorfismo:
                // A Batalha não sabe o que o personagem fará, apenas manda ele agir
                actor.takeTurn(allies, enemies, in, isPlayer);

                // Verifica se a batalha terminou após a ação do personagem
                if (!isTeamAlive(enemies)) {
                    break;
                }
            }
            round++;
        }

        // Exibe o resultado final da batalha
        System.out.println("\n--- Battle Result ---");
        if (isTeamAlive(teamA)) {
            System.out.println("You win! 🎉");
        } else {
            System.out.println("Defeat... 💀");
        }
    }
    
    // Método para imprimir o status atual de ambos os times
    private void printTeams() {
        System.out.println("Your Party:");
        for (Character c : teamA) System.out.println("  " + c.status());
        System.out.println("Enemies:");
        for (Character c : teamB) System.out.println("  " + c.status());
    }

    /**
     * Gerencia a seleção de alvo (via console) 
     * É estático para que as classes de personagem (Warrior, Mage) possam chamá-lo diretamente
     */
    public static Character pickTarget(List<Character> candidates, Scanner in, boolean isHuman, String prompt) {
        List<Character> alive = alive(candidates);
        if (alive.isEmpty()) return null;

        if (!isHuman) {
            // IA: escolhe um alvo vivo aleatório
            return alive.get(new Random().nextInt(alive.size()));
        }
        
        // Jogador humano: mostra um menu para escolha
        System.out.println(prompt);
        for (int i = 0; i < alive.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, alive.get(i).status());
        }

        // Loop para garantir que o jogador insira uma opção válida
        while (true) {
            try {
                int v = Integer.parseInt(in.nextLine().trim());
                if (v >= 1 && v <= alive.size()) {
                    return alive.get(v - 1);
                }
            } catch (Exception ignored) {}
            System.out.print("Enter a valid number: ");
        }
    }

    // Retorna uma lista com todos os personagens vivos de ambos os times
    private List<Character> allAlive() {
        List<Character> all = new ArrayList<>();
        all.addAll(alive(teamA));
        all.addAll(alive(teamB));
        return all;
    }

    // Verifica se um time ainda possui personagens vivos
    public static boolean isTeamAlive(List<Character> team) {
        for (Character c : team) {
            if (c.isAlive()) return true;
        }
        return false;
    }

    // Retorna uma nova lista contendo apenas os personagens vivos de uma lista de entrada
    public static List<Character> alive(List<Character> chars) {
        List<Character> list = new ArrayList<>();
        for (Character c : chars) {
            if (c.isAlive()) list.add(c);
        }
        return list;
    }
}
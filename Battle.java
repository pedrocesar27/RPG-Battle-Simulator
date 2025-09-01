import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    // Atributos da classe
    private final List<Character> teamA; // Time do jogador
    private final List<Character> teamB; // Time inimigo
    private final Scanner in;            // Objeto para ler a entrada do jogador
    private final Random rng = new Random(); // Adicionado para sorteios de chance

    // Construtor que inicializa a batalha com os times e o scanner
    public Battle(List<Character> teamA, List<Character> teamB, Scanner in) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.in = in;
    }
    
    /**
     * Contém o loop principal da batalha, que executa rounds e turnos
     * até que um dos times seja completamente derrotado.
     * ESTA VERSÃO FOI MODIFICADA PARA TURNOS DE EQUIPE.
     */
    public void run() {
        System.out.println("\n--- The Battle Begins! ---");

        // SORTEIO: Define qual equipe começa com 50% de chance.
        List<Character> startingTeam;
        List<Character> secondTeam;

        if (rng.nextBoolean()) {
            startingTeam = teamA;
            secondTeam = teamB;
            System.out.println("\nYour team has the initiative and attacks first!");
        } else {
            startingTeam = teamB;
            secondTeam = teamA;
            System.out.println("\nThe enemy team is faster and attacks first!");
        }

        int round = 1;
        // O loop continua enquanto ambos os times tiverem pelo menos um membro vivo
        while (isTeamAlive(teamA) && isTeamAlive(teamB)) {
            System.out.println("\n=== Round " + round + " ===");
            printTeams();

            // Executa o turno da primeira equipe
            executeTeamTurn(startingTeam, secondTeam);

            // Verifica se a batalha acabou antes do turno da segunda equipe
            if (!isTeamAlive(secondTeam)) {
                break;
            }

            // Executa o turno da segunda equipe
            executeTeamTurn(secondTeam, startingTeam);
            
            round++;
        }

        // Exibe o resultado final da batalha
        System.out.println("\n--- Battle Result ---");
        if (isTeamAlive(teamA)) {
            System.out.println("You win!");
        } else {
            System.out.println("Defeat...");
        }
    }
    
    /**
     * Executa o turno completo de uma equipe, personagem por personagem.
     * @param actingTeam A equipe que está agindo
     * @param opposingTeam A equipe que está sendo atacada
     */
    private void executeTeamTurn(List<Character> actingTeam, List<Character> opposingTeam) {
        boolean isPlayerTurn = (actingTeam == this.teamA);
        
        System.out.println("\n--- " + (isPlayerTurn ? "Player's Turn" : "Enemy's Turn") + " ---");

        // Itera sobre uma cópia da lista para evitar problemas se um personagem morrer
        for (Character actor : new ArrayList<>(alive(actingTeam))) {
            if (!actor.isAlive() || !isTeamAlive(opposingTeam)) {
                continue; // Pula se o ator morreu ou se a equipe inimiga foi derrotada
            }

            if (isPlayerTurn) {
                // Jogador age normalmente, chamando o método takeTurn do personagem
                System.out.println("\n-- " + actor.status() + "'s Action --");
                actor.takeTurn(actingTeam, opposingTeam, in, true);
            } else {
                // Inimigo age automaticamente através de uma função de controle
                performEnemyAction(actor, opposingTeam);
            }
        }
    }

    /**
     * Controla a lógica de decisão e ação para um personagem inimigo
     * Inclui o delay de 4 segundos e a lógica da barreira reativa
     * @param enemy O personagem inimigo que está agindo
     * @param playerTeam A equipe do jogador (alvos)
     */
    private void performEnemyAction(Character enemy, List<Character> playerTeam) {
        try {
            System.out.println("\n-- " + enemy.status() + " is preparing to act... --");
            Thread.sleep(4000); // ATRASO DE 4 SEGUNDOS
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // A IA do inimigo é controlada aqui. Para Healer e Mage, a IA deles decide a ação.
        // Para os outros, a ação padrão é atacar.
        if (enemy instanceof Healer || enemy instanceof Mage) {
            // O Healer/Mage inimigo decidirá se usa uma habilidade ou ataca
            boolean specialUsed = enemyAiSpecialAbility(enemy, playerTeam);
            if(specialUsed) return; // Se usou habilidade especial, o turno acaba.
        }

        // Ação padrão para todos os inimigos que não usaram habilidade: atacar.
        Character target = pickTarget(playerTeam, in, false, ""); // Escolhe um alvo aleatório
        if (target != null) {
            System.out.printf("%s decides to attack %s!%n", enemy.name, target.name);

            // BARREIRA REATIVA:
            // Se o alvo for um Mago do jogador, oferece a chance de usar a barreira
            if (target instanceof Mage) {
                System.out.print(target.name + " is under attack! Attempt to use reactive barrier? (60% chance) [Y/N]: ");
                String choice = in.nextLine().trim();
                if (choice.equalsIgnoreCase("Y")) {
                    if (rng.nextDouble() < 0.60) { // 60% de chance de sucesso
                        System.out.println("Barrier successful! The attack will be absorbed.");
                        ((Mage) target).setBarrier(true);
                    } else {
                        System.out.println("Barrier failed! The attack goes through.");
                    }
                }
            }
            // Finalmente, o inimigo executa o ataque.
            enemy.basicAttack(target);
        }
    }

    /**
     * Lógica para ações dos inimigos
     * @return true se uma habilidade foi usada, false caso contrário.
     */
    private boolean enemyAiSpecialAbility(Character enemy, List<Character> playerTeam){
        // Lógica para o Curandeiro Inimigo
        if (enemy instanceof Healer) {
            for (Character ally : teamB) {
                if (ally.isAlive() && ally.hp < ally.maxHp / 2) {
                    enemy.takeTurn(teamB, playerTeam, in, false); // Healer inimigo decide curar
                    return true;
                }
            }
        }
        // Lógica para o Mago Inimigo
        if (enemy instanceof Mage) {
            if (enemy.hp < enemy.maxHp / 2 && rng.nextDouble() < 0.3) {
                enemy.takeTurn(teamB, playerTeam, in, false); // Caso o mago inimigo decida usar uma barreira
                return true;
            }
        }
        return false;
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
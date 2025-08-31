import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. INICIALIZAÇÃO
        // Cria um Scanner para ler a entrada do jogador durante toda a partida
        Scanner scanner = new Scanner(System.in);
        System.out.println(" WELCOME TO THE RPG BATTLE SIMULATOR ");
        System.out.println("======================================");

        // 2. CRIAÇÃO DO GRUPO DO JOGADOR
        System.out.println("\nAssembling your party of heroes...");
        List<Character> playerParty = new ArrayList<>();
        playerParty.add(new Warrior("Sir Kael, the Valiant"));
        playerParty.add(new Mage("Elara, the Arcane"));
        playerParty.add(new Healer("Lia, the Cleric"));
        playerParty.add(new Assassin("Zane, the Shadow"));
        System.out.println("Your party is formed!");

        // 3. CRIAÇÃO DO GRUPO INIMIGO
        System.out.println("\nA group of fierce monsters blocks your path!");
        List<Character> enemyParty = new ArrayList<>();
        enemyParty.add(new Warrior("Brute Orc"));
        enemyParty.add(new Mage("Goblin Shaman"));
        enemyParty.add(new Archer("Goblin Sharpshooter"));
        System.out.println("Prepare for battle!");

        // 4. INÍCIO DA BATALHA
        // Cria uma nova instância da Batalha, passando os dois times e o scanner
        Battle battle = new Battle(playerParty, enemyParty, scanner);
        
        // O método run() contém todo o loop e a lógica do jogo
        battle.run(); 

        // 5. ENCERRAMENTO
        System.out.println("\n======================================");
        System.out.println(" The battle simulator has ended. ");
        
        // Fecha o scanner para liberar os recursos
        scanner.close();
    }
}
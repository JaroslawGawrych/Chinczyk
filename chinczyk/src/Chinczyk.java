import java.util.Random;
import java.util.Scanner;

public class Chinczyk {

    public static char[][] board;//[13][27]
    public static int playerCount;//2-4
    public static char[] colors;
    public static char[][] p;//positions[color][how many]
    public static char[][] h;//homes[color][position]
    public static char[][] s;//stash[color][position]
    public static int[][] players;//[color][finished]

    public static void main(String[] args) {
        start();
    }

    public static void start(int players, int[][] pos, int[] rolls, int[] decision){
        playerCount = players;
    }
    public static void start(){

        Scanner sc = new Scanner(System.in);
        System.out.println("podaj ilość graczy (2-4): ");
        playerCount = sc.nextInt();
        while(playerCount < 2 || playerCount > 4){
            System.out.println("podaj ilość graczy (2-4): ");
            playerCount = sc.nextInt();
        }
        h = new char[4][4];
        s = new char[4][4];

        colors = new char[] {'a','b','c','d'};
        players = new int[playerCount][2];
        for (int i = 0; i < playerCount; i++) {
            players[i][0] = i;
        }

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < players[i].length; j++) {
                System.out.print(players[i][j]);
            }
            System.out.println();
        }

        initPositions();
        generateBoard();
        printBoard();

        int turn = dice(0,playerCount);
//        pullOut(1);
//        move(10, 1);
//        move(11, 38);
//        move(12, 37);



        while (!checkEnd()){
            System.out.println("ruch gracza: " + colors[turn]);
            int d6 = dice(1,6);

            int[] pos = search(colors[turn]);
            boolean check = true;
            for (int i = 0; i < pos.length; i++) {
                if(pos[i]>=0){
                    check = false;
                    break;
                }
            }
            if(check) {
                System.out.println("brak dostępnych ruchów, wyprowadzono pionka");
                pullOut(turn);
                turn++;
                if(turn >= playerCount)
                    turn=0;
                continue;
            }


            System.out.println("wylosowano: " + d6);

            if(d6 == 6){
                System.out.println("czy chcesz wyprowadzić pionek? (0 - nie, 1 - tak): ");
                int decision = sc.nextInt();
                while (decision != 0 && decision != 1) {
                    System.out.println("czy chcesz wyprowadzić pionek? (0 - nie, 1 - tak): ");
                    decision = sc.nextInt();
                }
                if(decision==1){
                    pullOut(turn);
                    turn++;
                    if(turn >= playerCount)
                        turn=0;
                    continue;
                }
            }

            System.out.println("wybierz jeden z dostępnych pionków: ");

            for (int i = 0; i < pos.length; i++) {
                if(pos[i]>=0)
                    System.out.print(pos[i] + " ");
            }
            System.out.println();
            int tmp = sc.nextInt();

            while(tmp != pos[0] && tmp != pos[1] && tmp != pos[2] && tmp != pos[3]){
                System.out.println("wybierz jeden z dostępnych pionków : ");
                for (int i = 0; i < pos.length; i++) {
                    if(pos[i]>=0)
                        System.out.print(pos[i] + " ");
                }
                System.out.println();
                tmp = sc.nextInt();
            }

            move(tmp, d6);

            turn++;
            if(turn >= playerCount)
                turn=0;
        }

        System.out.println("end");
    }

    public static int[] search(char color){
        int[] pos = {-1,-1,-1,-1};
        int index = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i][0] == color)
                pos[index++] = i;
        }
        return pos;
    }

    //checks if everyone finished
    public static boolean checkEnd(){
        for (int i = 0; i < players.length; i++) {
            if(players[i][1]==0)
                return false;
        }
        return true;
    }
    //dice roll n - m
    public static int dice(int n, int m) {
        return new Random().nextInt(m) + n;
    }
    //initialises starting positions
    public static void initPositions() {
        p = new char[40][2];
        for (int i = 0; i < p.length; i++) {
            p[i][0] = 'x';
            p[i][1] = '0';
        }
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[i].length; j++) {
                h[i][j] = ' ';
            }
        }
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length; j++) {

                if (i < colors.length) {
                    s[i][j] = colors[i];
                } else {
                    s[i][j] = ' ';
                }
            }
        }
    }
    //generates board
    public static void generateBoard(){
        board = new char[][] { // 13 x 27
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','0',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',s[3][0],' ',s[3][1],' ',' ',' ',' ',' ',p[38][0],' ',p[39][0],' ',p[0][0],' ',' ',' ',' ',' ',' ',s[0][0],' ',s[0][1],' ',' '},
                {' ',' ',' ',s[3][2],' ',s[3][3],' ',' ',' ',' ',' ',p[37][0],' ',h[0][3],' ',p[1][0],' ',' ',' ',' ',' ',' ',s[0][2],' ',s[0][3],' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[36][0],' ',h[0][2],' ',p[2][0],' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[35][0],' ',h[0][1],' ',p[3][0],' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {'3','0',' ',p[30][0],' ',p[31][0],' ',p[32][0],' ',p[33][0],' ',p[34][0],' ',h[0][0],' ',p[4][0],' ',p[5][0],' ',p[6][0],' ',p[7][0],' ',p[8][0],' ',' ',' '},
                {' ',' ',' ',p[29][0],' ',h[3][3],' ',h[3][2],' ',h[3][1],' ',h[3][0],' ',' ',' ',h[1][0],' ',h[1][1],' ',h[1][2],' ',h[1][3],' ',p[9][0],' ',' ',' '},
                {' ',' ',' ',p[28][0],' ',p[27][0],' ',p[26][0],' ',p[25][0],' ',p[24][0],' ',h[2][0],' ',p[14][0],' ',p[13][0],' ',p[12][0],' ',p[11][0],' ',p[10][0],' ','1','0'},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[23][0],' ',h[2][1],' ',p[15][0],' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[22][0],' ',h[2][2],' ',p[16][0],' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',s[2][0],' ',s[2][1],' ',' ',' ',' ',' ',p[21][0],' ',h[2][3],' ',p[17][0],' ',' ',' ',' ',' ',s[1][0],' ',s[1][1],' ',' ',' '},
                {' ',' ',' ',s[2][2],' ',s[2][3],' ',' ',' ',' ',' ',p[20][0],' ',p[19][0],' ',p[18][0],' ',' ',' ',' ',' ',s[1][2],' ',s[1][3],' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','2','0',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}
        };
    }

    //prints board
    public static void printBoard() {
        System.out.println("---------------------------");
        for (int i = 0; i < board.length; i++) {

            for (int j = 0; j < board[i].length; j++) {

                System.out.print(board[i][j]);

            }

            System.out.println();

        }

    }
    //takes out a piece from a player stash
    public static boolean pullOut(int color){
        int n = 0;
        switch (color) {
            case 1 -> n += 10;
            case 2 -> n += 20;
            case 3 -> n += 30;
        }
        for (int j = 0; j < s[color].length; j++) {
            char tmp = s[color][j];
            if(tmp!=' ') {
                s[color][j] = ' ';
                if (p[n][0] != 'x' && p[n][0] != tmp){
                    beat(n);
                }
                p[n][0] = tmp;
                p[n][1] ++;
                generateBoard();
                printBoard();
                return true;
            }
        }
        return false;
    }
    //beats a piece on an index
    public static void beat(int index){
        char c = p[index][0];
        for (int i = 0; i < colors.length; i++) {
            if(c == colors[i])
            {

                for (int j = 0; j < s[i].length && p[index][1] > '0'; j++) {

                    if(s[i][j] == ' '){
                        s[i][j] = c;
                        p[index][1]--;

                    }
                }
            }
        }
    }
    //checks if a piece is back/over its starting position with next move

    public static boolean checkStart(int index, char color, int pos){
        for (int i = 0; i < colors.length; i++) {
            if(color == colors[i]) {
                if(i==0 && index >= 0 && pos > 0 && pos > index)
                    return true;
                if(index >= i*10 && pos < i*10) {
//                    System.out.println(".");
                    return true;
                }
            }
        }
        return false;
    }
    //moves a piece on a board
    public static boolean move(int i, int n) {
        if (i < 40) {
            int index = i + n;
            if(index > 39){
                index -= 39;
            }
            char tmp = p[i][0];
            if(p[i][1] <= '1') {
                p[i][0] = 'x';
            }
            p[i][1]--;
            if(checkStart(index, tmp, i)){
//                System.out.println("bb");
                for (int j = 0; j < colors.length; j++) {
                    if (tmp == colors[j]) {
//                        System.out.println("tu");
                        for (int k = 0; k < h[j].length; k++) {
//                            System.out.println("aa");
                            if(h[j][k] == ' ') {
//                                System.out.println("tutaj");
                                h[j][k] = tmp;
                                checkVictory(j);
                                generateBoard();
                                printBoard();
                                return true;
                            }
                        }
                    }
                }
                System.out.println("?");
                return false;
            }
            if (p[index][0] != 'x' && p[index][0] != tmp){
                beat(i + n);
            }
            p[index][0] = tmp;
            p[index][1] ++;

            generateBoard();
            printBoard();
            return true;
        } else {
            System.out.println("???");
            return false;
        }
    }
    //checks if a player (color) won
    public static boolean checkVictory(int color) {
        for (int j = 0; j < h[color].length; j++) {
            if (h[color][j] == ' ') {
                return false;
            }
        }
        players[color][1] = 1;
        return true;
    }
}

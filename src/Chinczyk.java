import java.util.Random;
import java.util.Scanner;

public class Chinczyk {

    public static char[][] board;
    public static char[][] h;
    public static char[][] s;
    public static char[] colors;
    public static int playerCount;

    public static char[][] p;

    public static char[][][] z;
    public static int[][] players;

    public static void main(String[] args) {
//        start(4, new int[][]{{'a',3},{'b',6},{'c',22},{'d',31}}, new int[] {2}, new int[] {3} );
        start();
    }
    public static int dice(int min, int max) {
        return new Random().nextInt(max) + min;
    }

    public static void start(){

        Scanner sc = new Scanner(System.in);
        System.out.println("ilość graczy (2-4): ");
        playerCount = sc.nextInt();
        while(playerCount < 2 || playerCount > 4){
            System.out.println("ilość graczy (2-4): ");
            playerCount = sc.nextInt();
        }
        initialisePositions();
        generateBoard();
        printBoard();
        int turn = dice(0,playerCount);
        while (!GameOver()){
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
                generateBoard();
                printBoard();
                continue;
            }
            System.out.println("wylosowano: " + d6);

            if(d6 == 6){
                System.out.println("wyprowadzić pionek? (0 - nie 1 - tak): ");
                int decision = sc.nextInt();
                while (decision != 0 && decision != 1) {
                    System.out.println("wyprowadzić pionek? (0 - nie 1 - tak): ");
                    decision = sc.nextInt();
                }
                if(decision==1){
                    pullOut(turn);
                    turn++;
                    if(turn >= playerCount)
                        turn=0;
                    generateBoard();
                    printBoard();
                    continue;
                }
            }
            System.out.println("wybierz jednego z dostępnych pionków: ");

            for (int i = 0; i < pos.length; i++) {
                if(pos[i]>=0)
                    System.out.print(pos[i] + " ");
            }
            System.out.println();
            int tmp = sc.nextInt();

            while(tmp != pos[0] && tmp != pos[1] && tmp != pos[2] && tmp != pos[3]){
                System.out.println("wybierz jednego z dostępnych pionków: ");
                for (int i = 0; i < pos.length; i++) {
                    if(pos[i]>=0)
                        System.out.print(pos[i] + " ");
                }
                System.out.println();
                tmp = sc.nextInt();
            }

            move(tmp, d6, colors[turn]);

            turn++;
            if(turn >= playerCount)
                turn=0;

            generateBoard();
            printBoard();
        }

        System.out.println("end");
    }
    public static boolean GameOver(){
        for (int i = 0; i < players.length; i++) {
            if(players[i][1]==0)
                return false;
        }
        return true;
    }


    public static void start(int playersAmount, int[][] start, int[] rolls, int[] dec){
        playerCount = playersAmount;
        initialisePositions();
        for (int i = 0; i < start.length; i++) {
            for (int j = 0; j < colors.length; j++) {
                if(start[i][0] == colors[j]){
                    pullOut(j);
                    move(j*10, start[i][1] - j*10, colors[j]);
                }
            }
        }
        generateBoard();
        printBoard();
        int turn = 0;
        for (int i = 0; i < colors.length; i++) {
            if(p[dec[0]][0] == colors[i])
                turn = i;
        }

        for (int i = 0; i < rolls.length; i++) {
            move(dec[i], rolls[i], colors[turn]);
            turn++;
            if(turn >= playerCount)
                turn=0;
            generateBoard();
            printBoard();
        }
        System.out.println("end");
    }
    public static boolean putInSafeZone(int n, char color){
        for (int k = 0; k < s[n /10].length; k++) {
            if(z[n /10][k][0] == ' ' || z[n /10][k][0] == 'x' ||z[n /10][k][0] == color)
            {
                if(z[n /10][k][0] != color){
                    z[n /10][k][0] = color;
                }

                z[n /10][k][1]++;
                return true;
            }
        }
        return false;
    }
    public static void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
    public static void initialisePositions() {
        z = new char[4][4][2];
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[i].length; j++) {
                if(j == 0)
                    z[i][j][0] = 'x';
                else
                    z[i][j][0] = ' ';
                z[i][j][1] = '0';
            }
        }
        h = new char[4][4];
        s = new char[4][4];
        colors = new char[] {'a','b','c','d'};

        players = new int[playerCount][2];
        for (int i = 0; i < playerCount; i++) {
            players[i][0] = i;
        }

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
    public static void generateBoard(){
        board = new char[][] {
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','0',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',s[3][0],' ',s[3][1],' ',' ',' ',' ',' ',p[38][0],' ',p[39][0],' ',z[0][0][0],z[0][1][0],z[0][2][0],z[0][3][0],' ',' ',' ',s[0][0],' ',s[0][1],' ',' '},
                {' ',' ',' ',s[3][2],' ',s[3][3],' ',' ',' ',' ',' ',p[37][0],' ',h[0][3],' ',p[1][0],' ',' ',' ',' ',' ',' ',s[0][2],' ',s[0][3],' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[36][0],' ',h[0][2],' ',p[2][0],' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {'3','0',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[35][0],' ',h[0][1],' ',p[3][0],' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {z[3][3][0],z[3][2][0],z[3][1][0],z[3][0][0],' ',p[31][0],' ',p[32][0],' ',p[33][0],' ',p[34][0],' ',h[0][0],' ',p[4][0],' ',p[5][0],' ',p[6][0],' ',p[7][0],' ',p[8][0],' ',' ',' '},
                {' ',' ',' ',p[29][0],' ',h[3][3],' ',h[3][2],' ',h[3][1],' ',h[3][0],' ',' ',' ',h[1][0],' ',h[1][1],' ',h[1][2],' ',h[1][3],' ',p[9][0],' ',' ',' '},
                {' ',' ',' ',p[28][0],' ',p[27][0],' ',p[26][0],' ',p[25][0],' ',p[24][0],' ',h[2][0],' ',p[14][0],' ',p[13][0],' ',p[12][0],' ',p[11][0],' ',z[1][0][0],z[1][1][0],z[1][2][0],z[1][3][0]},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[23][0],' ',h[2][1],' ',p[15][0],' ',' ',' ',' ',' ',' ',' ',' ',' ','1','0'},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',p[22][0],' ',h[2][2],' ',p[16][0],' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',s[2][0],' ',s[2][1],' ',' ',' ',' ',' ',p[21][0],' ',h[2][3],' ',p[17][0],' ',' ',' ',' ',' ',s[1][0],' ',s[1][1],' ',' ',' '},
                {' ',' ',' ',s[2][2],' ',s[2][3],' ',' ',z[2][3][0],z[2][2][0],z[2][1][0],z[2][0][0],' ',p[19][0],' ',p[18][0],' ',' ',' ',' ',' ',s[1][2],' ',s[1][3],' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ','2','0',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}
        };
    }


    public static int[] search(char color){
        int[] pos = {-1,-1,-1,-1};
        int index = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i][0] == color)
                pos[index++] = i;
        }
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[i].length; j++) {
                if(z[i][j][0] == color)
                    if(i == 0) {
                        pos[index++] = 0;
                    }else
                        pos[index++] = i*10;
            }
        }
        return pos;
    }
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
                if (p[n][0] != tmp){
                    putInSafeZone(n,tmp);
                    return true;
                }
                p[n][0] = tmp;
                p[n][1] ++;
                return true;
            }
        }
        return false;
    }

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
    public static boolean DidPlayerFinish(int color) {
        for (int j = 0; j < h[color].length; j++) {
            if (h[color][j] == ' ') {
                return false;
            }
        }
        players[color][1] = 1;
        return true;
    }

    public static boolean checkLap(int index, char color, int position){
        for (int i = 0; i < colors.length; i++) {
            if(color == colors[i]) {
                if(i==0 && index >= 0 && position > 0 && position > index)
                    return true;
                if(index >= i*10 && position < i*10) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean move(int i, int n, char col) {

            int index = i + n;
            if(index > 39){
                index -= 40;
            }

            if(i ==10|| i ==20|| i ==0|| i ==30){
                for (int j = 0; j < z[i /10].length; j++) {
                    if(z[i /10][j][0] == col) {
                        if (z[i / 10][j][1] <= '1') {
                            if(j==0)
                                z[i / 10][j][0] = 'x';
                            else
                                z[i / 10][j][0] = ' ';
                        }
                    }
                    z[i /10][j][1]--;
                }
            }else {

                if (p[i][1] <= '1') {
                    p[i][0] = 'x';

                }

                p[i][1]--;
            }
            if(checkLap(index, col, i)){
                for (int j = 0; j < colors.length; j++) {
                    if (col == colors[j]) {
                        for (int k = 0; k < h[j].length; k++) {
                            if(h[j][k] == ' ') {
                                h[j][k] = col;
                                DidPlayerFinish(j);
                                return true;
                            }
                        }
                    }
                }

                return false;
            }
            if ((p[index][0] != 'x' && p[index][0] != col) || (z[index/10][0][0] != 'x' && z[index/10][0][0]!= col)){
                if(index == 0 || index == 10 || index == 20 || index == 30){
                    putInSafeZone(index, col);
                    return true;
                }
                beat(i + n);
            }
            p[index][0] = col;
            p[index][1] ++;

            return true;

    }


}

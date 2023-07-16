import java.util.Scanner;
public class Main {
    public static void main(String[] yolo) {
        Scanner scan = new Scanner(System.in);
        int stop = 0;
        System.out.println("Введите сообщение в виде 7 цифр из бинарного набора:");
        String code = scan.nextLine();
        scan.close();
        if (code.length() != 7) System.out.println("Введены некорректные данные.");
        else {
            String[] mass = code.split("");
            int[] mas = new int[7];
            for (int i = 0; i < 7; i++) {
                if (mass[i].equals("1") || mass[i].equals("0")) mas[i] = Integer.valueOf(mass[i]);
                else {
                    stop++;
                    if (stop == 1) System.out.println("Введены некорректные данные.");
                }
            }
            if (stop == 0) {
                int s1 = (mas[0] + mas[2] + mas[4] + mas[6]) % 2;
                int s2 = (mas[1] + mas[2] + mas[5] + mas[6]) % 2;
                int s3 = (mas[3] + mas[4] + mas[5] + mas[6]) % 2;
                String str = Integer.toString(s1) + Integer.toString(s2) + Integer.toString(s3);
                switch (str) {
                    case "000":
                        System.out.print("Правильное сообщение: " + code + "." + "\n");
                        System.out.print("Поздравляем! Бит с ошибкой отсутствует!" + "\n");
                        break;
                    case "001":
                        if (mas[3] == 0) mas[3] = 1;
                        else mas[3] = 0;
                        System.out.print("Правильное сообщение: ");
                        for (int i = 0; i < 7; i++) System.out.print(mas[i]);
                        System.out.print("." + "\n");
                        System.out.print("Бит с ошибкой: побочный бит r3. Позиция: 0001000." + "\n");
                        break;
                    case "010":
                        if (mas[1] == 0) mas[1] = 1;
                        else mas[1] = 0;
                        System.out.print("Правильное сообщение: ");
                        for (int i = 0; i < 7; i++) System.out.print(mas[i]);
                        System.out.print("." + "\n");
                        System.out.print("Бит с ошибкой: побочный бит r2. Позиция: 0100000." + "\n");
                        break;
                    case "011":
                        if (mas[5] == 0) mas[5] = 1;
                        else mas[5] = 0;
                        System.out.print("Правильное сообщение: ");
                        for (int i = 0; i < 7; i++) System.out.print(mas[i]);
                        System.out.print("." + "\n");
                        System.out.print("Бит с ошибкой: информационный бит i3. Позиция: 0000010." + "\n");
                        break;
                    case "100":
                        if (mas[0] == 0) mas[0] = 1;
                        else mas[0] = 0;
                        System.out.print("Правильное сообщение: ");
                        for (int i = 0; i < 7; i++) System.out.print(mas[i]);
                        System.out.print("." + "\n");
                        System.out.print("Бит с ошибкой: побочный бит r1. Позиция: 1000000." + "\n");
                        break;
                    case "101":
                        if (mas[4] == 0) mas[4] = 1;
                        else mas[4] = 0;
                        System.out.print("Правильное сообщение: ");
                        for (int i = 0; i < 7; i++) System.out.print(mas[i]);
                        System.out.print("." + "\n");
                        System.out.print("Бит с ошибкой: информационный бит i2. Позиция: 0000100." + "\n");
                        break;
                    case "110":
                        if (mas[2] == 0) mas[2] = 1;
                        else mas[2] = 0;
                        System.out.print("Правильное сообщение: ");
                        for (int i = 0; i < 7; i++) System.out.print(mas[i]);
                        System.out.print("." + "\n");
                        System.out.print("Бит с ошибкой: информационный бит i1. Позиция: 0010000." + "\n");
                        break;
                    case "111":
                        if (mas[6] == 0) mas[6] = 1;
                        else mas[6] = 0;
                        System.out.print("Правильное сообщение: ");
                        for (int i = 0; i < 7; i++) System.out.print(mas[i]);
                        System.out.print("." + "\n");
                        System.out.print("Бит с ошибкой: информационный бит i4. Позиция: 0000001." + "\n");
                        break;
                }
            }
        }
    }
}

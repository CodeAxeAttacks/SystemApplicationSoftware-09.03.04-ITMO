// lab1, variant: 367868
public class Main {
    public static void main(String[] yeyo) {
        int counter = 0; // зададим счётчик
        // пункт 1: создадим одномерный массив типа int
        int[] a = new int[10];
        // заполним этот массив чётными числами из промежутка [6; 24] через цикл for
        for (int i = 0; i < 10; i++) {
            a[i] = 6 + counter * 2;
            counter++;
        }
        // пункт 2: создадим одномерный массив типа double
        double[] x = new double[18];
        // заполним этот массив случайными числами в диапазоне [-15.0; 9.0) при помощи метода Math.random() и цикла for
        for (int i = 0; i < 18; i++) x[i] = Math.random() * 24.0 - 15.0;
        // пункт 3: создадим двумерный массив типа double
        double[][] mas = new double[10][18];
        // используя циклы for, заполним массив
        for (int i = 0; i < 10; i++) {
            // условие 1:
            if (a[i] == 18)
                for (int j = 0; j < 18; j++)
                    mas[i][j] = Math.atan((2.0 / 3.0) * Math.pow(Math.E, -(Math.abs(x[j]))));
            // условие 2:
            else if (a[i] == 6 || a[i] == 10 || a[i] == 12 || a[i] == 20 || a[i] == 22)
                for (int j = 0; j < 18; j++)
                    mas[i][j] = Math.pow((3 + (x[j] / 2.0) * (x[j] / 2.0)) / (Math.pow((x[j] / (x[j] - 4)), 3)), (2 * Math.atan(Math.pow(Math.E, -(Math.abs(x[j]))))));
            // условие 3:
            else
                for (int j = 0; j < 18; j++)
                    mas[i][j] = Math.pow(Math.E, ((Math.log(Math.abs(x[j]))) * Math.pow(Math.cos(x[j]) - 3, 2)) / 2.0);
        }
        // пункт 4: выведем полученный двумерный массив, используя:
        // два цикла for, формат вывода "%.2f" и тернарный оператор воизбежание лишнего пробела после последнего эл. строки
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 18; j++) {
                System.out.printf("%.2f", mas[i][j]);
                System.out.print((j != 17) ? " " : "");
            }
            System.out.print("\n");
        }
    }
}
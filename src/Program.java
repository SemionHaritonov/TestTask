import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
public class Program {
    public static void main(String[] args) {

        try{
            Debt TestDebt = new Debt(); // Объект кредит
            TestDebt.Scan();    //Считывание и разделка строки
            //TestDebt.displayInfo(); //Вывод считанных параметров
            TestDebt.Count();   //Считает переплату
            //System.out.println(String.format("%.1f", TestDebt.OverPay));  //В таком случае разделитель целой части "'"
            System.out.println(TestDebt.OverPay);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}
class Debt {
    double Debt, Pay, Percent;   //Сумма кредита, ежемесячный платеж, процент
    String Type;                //Тип кредита
    double OverPay = 0;           //Результат переплата

    //Вывод введенной информации по кредиту
    void displayInfo() {
        System.out.printf("Debt: %.2f  Pay: %.2f  Percent: %.2f  Type: %s \n", Debt, Pay, Percent, Type);
    }

    //Получение входных данных
    void Scan() throws Exception{
        Scanner in = new Scanner(System.in);
        String DebtData = in.nextLine();    //Входные данные в виде строки
        in.close();
        DebtData = DebtData.trim();    //Ой-вэй
        String[] SplitData = DebtData.split(" ", 4);    //Разбиение на 4 подстроки по пробелу

        try{
            Debt = Float.parseFloat(SplitData[0]);
            Pay = Float.parseFloat(SplitData[1]);
            Percent = Float.parseFloat(SplitData[2]);
            Type = SplitData[3].trim();
        }
        catch(Exception ex){
            throw new Exception("throws Exception");        //В случае ввода ерунды вместо чисел выдать ошибку с текстом
        }

        if (Debt <= 0 || Pay <= 0 || Percent < 0) throw new Exception("throws Exception");
        if (!(Type.equals("business") || Type.equals("human"))) throw new Exception("throws Exception");
        }

    //Расчет переплаты
    void Count() throws Exception{
            int i = 0;      //Счетчик платежей
            switch (Type) {
                case "human":
                    double CurrentDebtH = round(Debt * (1 + Percent / 100));  //Переменная для хранения
                    //Текущего долга. Значение по условию для человека

                    //Тут проверка, что кредит вообще можно выплатить с такими процентами и платежом
                    if (CurrentDebtH - 12 * Pay > 0 && round((CurrentDebtH - 12 * Pay) * (1 + Percent / 100)) >= CurrentDebtH)
                            throw new Exception("throws Exception");
                    while (CurrentDebtH >= Pay) {
                        i++;
                        CurrentDebtH -= Pay;
                        OverPay += Pay;     //Сейчас это по сути все платежи

                        /* Было нужно для отладки
                        System.out.printf("i: %d \n", i);
                        System.out.printf("CurrentDebt: %f \n", CurrentDebtH);
                        System.out.printf("OverPay: %f \n", OverPay);
                        System.out.println(Debt);
                        */

                         //Если прошел 12й платеж, увеличить текущий долг на годовой процент
                         if (i == 12) {
                             i = 0;
                             CurrentDebtH = round(CurrentDebtH * (1 + Percent / 100));
                            }
                        }
                        OverPay = OverPay + CurrentDebtH - Debt; //Переплата = Все платежи + Остаток(<Платежа) - Стартовая сумма
                    break;
//
                case "business":
                    double CurrentDebtB = round(Debt);  //Переменная для хранения текущего долга

                    //Тут проверка, что кредит вообще можно выплатить с такими процентами и платежом
                    if (CurrentDebtB - 12 * Pay > 0 && round((CurrentDebtB - 12 * Pay) * (1 + Percent / 100)) >= CurrentDebtB)
                            throw new Exception("throws Exception");
                    while (CurrentDebtB >= Pay) {
                        i++;
                        CurrentDebtB -= Pay;
                        OverPay += Pay;

                        //Если прошел 12й платеж, увеличить текущий долг на годовой процент
                        if (i == 12) {
                            i = 0;
                            CurrentDebtB = round(CurrentDebtB * (1 + Percent / 100));
                        }
                    }
                    OverPay = OverPay + CurrentDebtB - Debt;
                    break;
 //

            }
    }

    //Округление чисел с плавающей точкой, нужно для деления/умножения
    private static double round(double value) {
        int places = 2;     //Копейка - сотая рубля
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
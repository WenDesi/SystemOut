import java.io.*;
import java.util.*;

public class Calculator
{
    static int totaltimes = 0, successtimes = 0, steps = 0, errortimes = 0;
    static File times=new File("times.txt");
    static Stack<Character> operator = new Stack<>();
    public static int Calculate(char op, int a1, int a2) throws Exception
    {
        switch(op)
        {
            case '+':
                steps++;
                System.out.print("第"+steps+"步"+":");
                System.out.printf("%d%c%d%c%d",a2,op,a1,'=',a2+a1);
                System.out.println();
                return a2 + a1;
            case '-':
                steps++;
                System.out.print("第"+steps+"步"+":");
                System.out.printf("%d%c%d%c%d",a2,op,a1,'=',a2-a1);
                System.out.println();
                return a2 - a1;
            case '*':
                steps++;
                System.out.print("第"+steps+"步"+":");
                System.out.printf("%d%c%d%c%d",a2,op,a1,'=',a2*a1);
                System.out.println();
                return a2 * a1;
            case '/':
                steps++;
                if(a1==0)
                {
                    errortimes++;
                    BufferedWriter bwt1=new BufferedWriter(new FileWriter(times));
                    bwt1.write(totaltimes+""+successtimes+""+errortimes+"");
                    bwt1.flush();
                    bwt1.close();
                    throw new Exception("Illegal parameter!");
                }
                System.out.print("第"+steps+"步"+":");
                System.out.printf("%d%c%d%c%d",a2,op,a1,'=',a2/a1);
                System.out.println();
                return a2 / a1;
            default:
                Integer.valueOf(-0);
        }
        errortimes++;
        BufferedWriter bwt2=new BufferedWriter(new FileWriter(times));
        bwt2.write(totaltimes+""+successtimes+""+errortimes+"");
        bwt2.flush();
        bwt2.close();
        throw new Exception("Illegal operator!");
    }
    public static int GetResult(String expr) throws Exception
    {
        Stack<Integer> number= new Stack<>();
        char[] arr = expr.toCharArray();
        int len = arr.length;
        for(int i = 0; i < len; i++){
            Character ch = arr[i];
            if(ch >= '0' && ch <= '9')
                number.push(Integer.valueOf(ch - '0'));
            else
                number.push(Calculate(ch, number.pop(), number.pop()));
        }
        return number.pop();
    }
    public static String Transform(String expression)
    {
        char[] arr = expression.toCharArray();
        int len = arr.length;
        String res = "";
        for(int i = 0; i < len; i++){
            char temp = arr[i];
            if(temp == ' ')
                continue;
            if(temp >= '0' && temp <= '9')
            {
                res+=temp;
                continue;
            }
            if(temp == '(')
                operator.push(temp);
            if(temp == '+' || temp == '-')
            {
                while(!operator.empty() && (operator.peek() != '('))
                    res+= operator.pop();
                operator.push(temp);
                continue;
            }
            if(temp == '*' || temp == '/')
            {
                while(!operator.empty() && (operator.peek() == '*' || operator.peek() == '/'))
                    res+= operator.pop();
                operator.push(temp);
                continue;
            }
            if(temp == ')')
            {
                while(!operator.empty() && (operator.peek() != '('))
                    res += operator.pop();
                operator.pop();
                continue;
            }
        }
        while(!operator.empty())
            res += operator.pop();
        return res;
    }

    public static void main(String[] args) throws Exception
    {
        Scanner s = new Scanner(System.in);
        String str =null;
        while(true)
        {
            str=s.next();
            if(str.equals("end"))
            {
                System.out.println("EXIT");
                break;
            }
            if (!times.exists())
                times.createNewFile();
            File expression = new File("expression.txt");
            BufferedReader readline = new BufferedReader(new FileReader(times));
            if (readline.readLine() != null) {
                //read
                BufferedReader br = new BufferedReader(new FileReader(times));
                String string = br.readLine();
                String[] newstring = string.split("");
                totaltimes = Integer.valueOf(newstring[0]);
                successtimes = Integer.valueOf(newstring[1]);
                errortimes = Integer.valueOf(newstring[2]);
                br.close();
            }



            totaltimes++;

            //write expression
            BufferedWriter bw = new BufferedWriter(new FileWriter(expression));
            bw.write(str);
            bw.flush();
            bw.close();


            steps=0;
            System.out.println("总共运行" + totaltimes + "次;" + "运行成功" + successtimes + "次;" + "错误运行" + errortimes + "次。");
            System.out.println(GetResult(Transform(str)));
            successtimes++;


            //write times
            BufferedWriter bwt = new BufferedWriter(new FileWriter(times));
            bwt.write(totaltimes + "" + successtimes + "" + errortimes + "");
            bwt.flush();
            bwt.close();
        }
        s.close();
        //5+2*(3*(3-1*2+1))
    }
}
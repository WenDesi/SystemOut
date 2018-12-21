import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CalculateModule
{
    public int Run(String expression)
    {
        String res=TransForm(expression);
        int n=GetCalculateResult(res);
        return n;
    }
    private int GetCalculateResult(String res)
    {
        Stack<Integer> number= new Stack<>();
        String[] strings=res.split(",");
        int len = strings.length;
        for(int i = 0; i < len; i++)
        {
            String s = strings[i];
            if(s.equals("+")||s.equals("-")||s.equals("*")||s.equals("/"))
            {
                int a1=number.pop();
                int a2=number.pop();
                number.push(Calculate(s,a1,a2));
            }
            else
            {
                number.push(Integer.valueOf(s));
            }
        }
        return number.pop();
    }
    private String TransForm(String expression)
    {
        Pattern p=Pattern.compile("[\\+\\-\\*\\/]|\\(|\\)|\\d+");
        Matcher m=p.matcher(expression.replaceAll(" +",""));
        Stack<String> operator = new Stack<>();
        StringBuilder sb=new StringBuilder();
        while(m.find())
        {
            String temp=m.group();
            if(temp.equals("("))
            {
                operator.push(temp);
            }
            else if(temp.equals("+")||temp.equals("-"))
            {
                while(!operator.empty() && (!operator.peek().equals("(")))
                    sb.append(operator.pop()+",");
                operator.push(temp);
            }
            else if(temp.equals("*")||temp.equals("/"))
            {
                while(!operator.empty() && (operator.peek().equals("*")||operator.peek().equals("/")))
                    sb.append(operator.pop()+",");
                operator.push(temp);
            }
            else if(temp.equals(")"))
            {
                while(!operator.empty() && ((!operator.peek().equals("("))))
                    sb.append(operator.pop()+",");
                operator.pop();
            }
            else
            {
                sb.append(temp+",");
            }
        }
        while(!operator.empty())
            sb.append(operator.pop()+",");
        String res=sb.toString();
        return res;
    }
    private int Calculate(String op, int a1, int a2)
    {
        int result=0;
        switch(op)
        {
            case "+":
                result=a2 + a1;
                break;
            case "-":
                result=a2 - a1;
                break;
            case "*":
                result=a2 * a1;
                break;
            case "/":
                result=a2 / a1;
                break;
            default:
                Integer.valueOf(-0);
        }
        return result;
    }
}
class ReadAndWriteFile
{
    public void CreateFile(File filename)
    {
        if(!filename.exists())
        {
            try{
                filename.createNewFile();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public int[] ReadTimes(File filename1) throws Exception
    {
        int tT=0,sT=0,eT=0;
        FileReader fRtimes=new FileReader(filename1);
        BufferedReader bRtimes=new BufferedReader(fRtimes);
        String str=bRtimes.readLine();
        if(str!=null)
        {
            String[] strings=str.split(",");
            tT=Integer.parseInt(strings[0]);
            sT=Integer.parseInt(strings[1]);
            eT=Integer.parseInt(strings[2]);
            bRtimes.close();
        }
        int[]times={tT,sT,eT};
        return times;
    }
    public void WriteTimes(File filename2,int tT,int sT,int eT) throws Exception
    {
        FileWriter fWtimes=new FileWriter(filename2);
        BufferedWriter bWtimes=new BufferedWriter(fWtimes);
        bWtimes.write(tT+","+sT+","+eT+",");
        bWtimes.flush();
        bWtimes.close();
    }
    public void WriteExpression(File expression,String str) throws Exception
    {
        FileWriter fw=new FileWriter(expression);
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write(str);
        bw.flush();
        bw.close();
    }
}


public class Function
{
    static int totalTimes = 0, successTimes = 0,errorTimes = 0,steps = 0;
    //static File CalculateTimesFilePath;
    //static File expression = new File("expression.txt");
    public static void main(String[] args) throws Exception
    {
        Scanner scanner=new Scanner(System.in);
        while(true)
        {
            String expression=scanner.next();
            if(expression.equals("end"))
            {
                System.out.println("EXIT");
                break;
            }
            String TimesFilepath="CalculateTimesFilePath.txt";
            File CalculateTimesFilePath=new File(TimesFilepath);
            String Expressionpath="ExpressionFilePath.txt";
            File ExpressionFilePath=new File(Expressionpath);

            ReadAndWriteFile rAw=new ReadAndWriteFile();
            rAw.CreateFile(CalculateTimesFilePath);
            rAw.CreateFile(ExpressionFilePath);
            int[] temp=rAw.ReadTimes(CalculateTimesFilePath);
            totalTimes=temp[0];
            successTimes=temp[1];
            errorTimes=temp[2];
            rAw.WriteExpression(ExpressionFilePath,expression);
            totalTimes++;
            //

            try {
                CalculateModule calculator=new CalculateModule();
                int n=calculator.Run(expression);
                System.out.println(n);
                successTimes++;
            }catch (Exception e)
            {
                e.getStackTrace();
                errorTimes++;
                e.printStackTrace();
                rAw.WriteTimes(CalculateTimesFilePath,totalTimes,successTimes,errorTimes);
            }
            System.out.println("总共运行"+totalTimes+"次;"+"运行成功"+successTimes+"次;"+"错误运行"+errorTimes+"次。");
            rAw.WriteTimes(CalculateTimesFilePath,totalTimes,successTimes,errorTimes);
        }
        scanner.close();
    }
}
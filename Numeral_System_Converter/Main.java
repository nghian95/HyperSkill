package converter;
import java.util.Scanner;

public class Main {
    static int sum = 0;
    static String result = "";
    static int sourceInt = 0;
    static boolean error = false;

    public static void main(String[] args) {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            double sourceNumber;
            String fracHexadecimal = "", intHexadecimal;
            int sourceRadix, targetRadix;
	    System.out.println("What is the source radix/base?");
            if (scanner.hasNextInt()) {
                sourceRadix = scanner.nextInt();
                if (sourceRadix > 36) {
                    error=true;
                    break;
                }
            } else {
                error = true;
                break;
            }
            int changedRadix = 0;
	    System.out.println("What is the number you want to convert?");
            if (scanner.hasNextDouble()) {
                sourceNumber = scanner.nextDouble();
            } else {
                scanner.nextLine();
                String sourceHexadecimal = scanner.nextLine();
                for (int i=0; i<sourceHexadecimal.length(); i++) {
                    if (Character.getNumericValue(sourceHexadecimal.charAt(i))>=sourceRadix){
                        error=true;
                        break;
                    }
                }
                if(error) {
                    break;
                }
                if (sourceHexadecimal.contains(".")) {
                    int decimalPoint = sourceHexadecimal.indexOf(".");
                    intHexadecimal = sourceHexadecimal.substring(0, decimalPoint);
                    fracHexadecimal = "0" + sourceHexadecimal.substring(decimalPoint);
                } else {
                    intHexadecimal = sourceHexadecimal;
                }
                sourceNumber = Integer.parseInt(intHexadecimal, sourceRadix);
                changedRadix = 10;
            }
	    System.out.println("What is the target radix/base?");
            if (scanner.hasNextInt()) {
                targetRadix = scanner.nextInt();
                if (targetRadix==0 || targetRadix>36 || targetRadix<0){
                    error=true;
                    break;
                }
            } else {
                error = true;
                break;
            }

            if (changedRadix == 0) {
                changedRadix = sourceRadix;
            }
            result = convertInteger(changedRadix, sourceNumber, targetRadix);
            double sourceFrac = sourceNumber - sourceInt;
            String frac;
            if (!fracHexadecimal.isEmpty()) {
                result += convertFrac(sourceRadix, fracHexadecimal, targetRadix);
            } else {
                frac = String.valueOf(sourceFrac);
                result += convertFrac(sourceRadix, frac, targetRadix);
            }
            break;
        }
        if (error) {
            System.out.println("error");
        } else {
            System.out.println("Result: " + result);
        }
    }

    public static String convertInteger(int sourceRadix, double sourceNumber, int targetRadix){
        sourceInt = (int)sourceNumber;
        String sourceN = Integer.toString(sourceInt);
        if (sourceRadix != 10 && sourceRadix>1) {
            sum=Integer.parseInt(sourceN, sourceRadix);
        } else if (sourceRadix==1) {
            sum=sourceN.length();
        } else {
            sum=sourceInt;
        }
        if (sum>0 && targetRadix != 1) {
            result=Integer.toString(sum,targetRadix);
        } else if (sourceInt==0){
            result="0";
        } else {
            StringBuilder radixString = new StringBuilder();
            for (int i = 0; i<sum; i++) {
                radixString.append(1);
            }
            result = radixString.toString();
        }
        return result;
    }

    public static String convertFrac(int sourceRadix, String frac, int targetRadix) {
        if (frac.equals("0.0")) {
            return "";
        }
	double decimalValue = 0;
        for (int i=2; i<frac.length(); i++) {
            decimalValue+=Character.getNumericValue(frac.charAt(i))/(Math.pow(sourceRadix,i-1));
        }
        StringBuilder sbFrac = new StringBuilder(".");
        double changingValue = decimalValue;
        for (int i=0; i<5; i++) {
            changingValue *= targetRadix;
            if (changingValue>=10) {
                sbFrac.append(Character.forDigit((int)changingValue,targetRadix));
            } else {
                sbFrac.append((int) changingValue);
            }
            changingValue-=(int)changingValue;
        }
        return sbFrac.toString();
    }

}

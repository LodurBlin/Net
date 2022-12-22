package net.game.Future;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
public class RegexMatches {

    public static void main( String args[] ) {
        String userInputPattern = args[0];
        try {
            Pattern.compile(userInputPattern);
        } catch (PatternSyntaxException exception) {
            System.err.println(exception.getDescription());
            System.exit(1);
        }
        System.out.println("Syntax is ok.");

        // Строка для сканирования, чтобы найти шаблон
        String str = "Крещение Руси произошло в 988 году! Не так ли?";
        String pattern = "(.*)(\\d+)(.*)";

        // Создание Pattern объекта
        Pattern r = Pattern.compile(pattern);

        // Создание matcher объекта
        Matcher m = r.matcher(str);
        if (m.find( )) {
            System.out.println("Найдено значение: " + m.group(0));
            System.out.println("Найдено значение: " + m.group(1));
            System.out.println("Найдено значение: " + m.group(2));
        }else {
            System.out.println("НЕ СОВПАДАЕТ");
        }
    }
}
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Test{
    public static void main(String[] args){
        Matcher matcher = Pattern.compile("(a(b)?)+").matcher("abaaababab");
        if(matcher.find()) System.out.println(matcher.group());
    }
}

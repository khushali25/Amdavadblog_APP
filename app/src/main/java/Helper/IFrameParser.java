package Helper;

import android.service.autofill.FieldClassification;
import android.service.autofill.RegexValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IFrameParser {
    public static String urlUpdate(String htmlContent)
    {
        String htmlResponse = htmlContent;
        String pattern = "\\ssrc=\"\\/\\/\\b(\\S *)\\b";
        String retval = "";

        Pattern p = Pattern.compile("\\ssrc=\"\\/\\/\\b(\\S *)\\b", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m1 = p.matcher(htmlResponse);

        if (m1.find()){

            if (m1.group().length() > 1)
            {
                retval = m1.group(1);
            }

            if (!(retval==null) || retval.equals(""))
            {
                if (!(retval.contains("http") || retval.contains("https")))
                {
                    htmlResponse = htmlResponse.replace(htmlResponse," src=https://" + retval);
                }
            }

        }

        return htmlResponse;
    }

    //public static string urlUpdate(string htmlContent)
    //{
    //    string htmlResponse = htmlContent;
    //    string OhtmlResponse = String.Empty;
    //    string oldPattern = @"\ssrc=""\b(\S*)\b";
    //    string pattern = @"\ssrc=""\/\/\b(\S *)\b";
    //    string httpPattern = @"\ssrc=""https:\/\/\b(\S *)\b";
    //    string retval = String.Empty;

    //    Match matchdec = Regex.Match(htmlResponse, pattern, RegexOptions.IgnorePatternWhitespace | RegexOptions.IgnoreCase);
    //    while (matchdec.Success)
    //    {
    //        if (matchdec.Groups.Count > 1)
    //        {
    //            retval = matchdec.Groups[1].Value;
    //        }
    //        matchdec = matchdec.NextMatch();
    //    }

    //    if (!string.IsNullOrEmpty(retval))
    //        if (!(retval.Contains("http") || retval.Contains("https")))
    //            htmlResponse = Regex.Replace(htmlResponse, pattern, " src=http://" + retval, RegexOptions.IgnorePatternWhitespace | RegexOptions.IgnoreCase);

    //    return htmlResponse;
    //}
}


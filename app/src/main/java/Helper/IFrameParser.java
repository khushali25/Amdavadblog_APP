//package Helper;
//
//import android.service.autofill.FieldClassification;
//import android.service.autofill.RegexValidator;
//
//public class IFrameParser {
//    public static String urlUpdate(String htmlContent)
//    {
//        String htmlResponse = htmlContent;
//        String pattern = @"\ssrc=""\/\/\b(\S *)\b";
//        String retval = "";
//
//        Match matchdec = Regex(htmlResponse, pattern, RegexOptions.IgnorePatternWhitespace | RegexOptions.IgnoreCase);
//        while (matchdec.Success)
//        {
//            if(matchdec.Groups.Count > 1)
//            {
//                retval = matchdec.Groups[1].Value;
//            }
//
//            if (!String.IsNullOrEmpty(retval))
//                if (!(retval.Contains("http") || retval.Contains("https")))
//                    htmlResponse = Regex.Replace(htmlResponse, pattern, " src=https://" + retval, RegexOptions.IgnorePatternWhitespace | RegexOptions.IgnoreCase);
//
//            matchdec = matchdec.NextMatch();
//        }
//
//        return htmlResponse;
//    }
//
//    //public static string urlUpdate(string htmlContent)
//    //{
//    //    string htmlResponse = htmlContent;
//    //    string OhtmlResponse = String.Empty;
//    //    string oldPattern = @"\ssrc=""\b(\S*)\b";
//    //    string pattern = @"\ssrc=""\/\/\b(\S *)\b";
//    //    string httpPattern = @"\ssrc=""https:\/\/\b(\S *)\b";
//    //    string retval = String.Empty;
//
//    //    Match matchdec = Regex.Match(htmlResponse, pattern, RegexOptions.IgnorePatternWhitespace | RegexOptions.IgnoreCase);
//    //    while (matchdec.Success)
//    //    {
//    //        if (matchdec.Groups.Count > 1)
//    //        {
//    //            retval = matchdec.Groups[1].Value;
//    //        }
//    //        matchdec = matchdec.NextMatch();
//    //    }
//
//    //    if (!string.IsNullOrEmpty(retval))
//    //        if (!(retval.Contains("http") || retval.Contains("https")))
//    //            htmlResponse = Regex.Replace(htmlResponse, pattern, " src=http://" + retval, RegexOptions.IgnorePatternWhitespace | RegexOptions.IgnoreCase);
//
//    //    return htmlResponse;
//    //}
//}
//

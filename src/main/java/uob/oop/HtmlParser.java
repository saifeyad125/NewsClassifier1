package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        //first I will locate where the index of the title is
        int start = _htmlCode.indexOf("<title>");
        if (start == -1) return "Title not found!";
        else {
            start = start + 7;
            int end = _htmlCode.indexOf("</title>");
            String title = _htmlCode.substring(start, end);
            //now I will remove the extra parts of the title
            end = title.indexOf("|") - 1;
            title = title.substring(0, end);
            return title;
        }


    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        //first I will locate the start and end of the body
        int start = _htmlCode.indexOf("articleBody");
        if (start == -1) return "Content not found!";
        else{
            start = start + 15;
            int end = _htmlCode.indexOf("mainEntityOfPage");
            end = end - 4;
            return _htmlCode.substring(start, end).toLowerCase();
        }
    }


}

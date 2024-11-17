package uob.oop;



public class NLP {
    /***
     * Clean the given (_content) text by removing all the characters that are not 'a'-'z', '0'-'9' and white space.
     * @param _content Text that need to be cleaned.
     * @return The cleaned text.
     */
    public static String textCleaning(String _content) {
        StringBuilder sbContent = new StringBuilder();
        char[] array = new char[_content.length()];
        array = _content.toCharArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = Character.toLowerCase(array[i]);
            if (Character.isLetter(array[i]) || Character.isDigit(array[i])|| Character.isSpaceChar(array[i])) {
                        sbContent.append(array[i]);
            }

        }
        return sbContent.toString().trim();
    }


    /***
     * Text lemmatization. Delete 'ing', 'ed', 'es' and 's' from the end of the word.
     * @param _content Text that need to be lemmatized.
     * @return Lemmatized text.
     */
    public static String textLemmatization(String _content) {
        StringBuilder sbContent = new StringBuilder();
        //TODO Task 2.2 - 3 marks
        String[] words = _content.split(" ");
        for (int i = 0; i < words.length; i++) {
            //get the index of the last 3 in order to see if it ends in 'ing'
            //3 different ifs for if the word is larger than 3 characters, equal to 2, or smaller than 2
            if (words[i].length() > 2) {
                int start = words[i].length() - 3;
                int end = words[i].length();
                String ending = words[i].substring(start, end);
                if (ending.equals("ing")) {
                    words[i] = words[i].substring(0, start);
                } else {
                    start = words[i].length() - 2;
                    end = words[i].length();
                    ending = words[i].substring(start, end);
                    if (ending.equals("ed") || ending.equals("es")) {
                        words[i] = words[i].substring(0, start);
                    } else {
                        start = words[i].length() - 1;
                        end = words[i].length();
                        ending = words[i].substring(start, end);
                        if (ending.equals("s")) {
                            words[i] = words[i].substring(0, start);
                        }
                    }

                }
                sbContent.append(words[i]).append(" ");


            }
            else if (words[i].length() == 2) {
                int start = words[i].length() - 2;
                int end = words[i].length();
                String ending = words[i].substring(start, end);
                if (ending.equals("ed") || ending.equals("es")){
                    words[i] = words[i].substring(0, start);
                }
                else{
                    start = words[i].length() - 1;
                    end = words[i].length();
                    ending = words[i].substring(start, end);
                    if (ending.equals("s")){
                        words[i] = words[i].substring(0, start);
                    }
                }
                sbContent.append(words[i]).append(" ");

            }
            else if (words[i].length() == 1){
                int start = words[i].length() - 1;
                int end = words[i].length();
                String ending = words[i].substring(start, end);
                if (ending.equals("s")){
                    words[i] = words[i].substring(0, start);
                }
                sbContent.append(words[i]).append(" ");
            }
            else{
                sbContent.append(words[i]).append(" ");
            }
        }



        return sbContent.toString().trim();
    }


    /***
     * Remove stop-words from the text.
     * @param _content The original text.
     * @param _stopWords An array that contains stop-words.
     * @return Modified text.
     */
    public static String removeStopWords(String _content, String[] _stopWords) {
        StringBuilder sbConent = new StringBuilder();
        //TODO Task 2.3 - 3 marks
        String[] words = _content.split(" ");
        boolean located = false;
        for (int i = 0; i< words.length; i++) {
            located = false;
            for (String stopWord : _stopWords) {
                if (words[i].equals(stopWord)) {
                    words[i] = "";
                    located = true;
                    break;
                }
            }
            //we use the located boolean to see if the variable at this index is a stopword
            //or not, and if it isn't, then we would append it to sbConent.
            if (!located) sbConent.append(words[i]).append(" ");
        }

        return sbConent.toString().trim();
    }

}

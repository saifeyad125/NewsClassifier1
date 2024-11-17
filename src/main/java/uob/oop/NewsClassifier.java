package uob.oop;

import java.text.DecimalFormat;

public class NewsClassifier {
    public String[] myHTMLs;
    public String[] myStopWords = new String[127];
    public String[] newsTitles;
    public String[] newsContents;
    public String[] newsCleanedContent;
    public double[][] newsTFIDF;

    private final String TITLE_GROUP1 = "Osiris-Rex's sample from asteroid Bennu will reveal secrets of our solar system";
    private final String TITLE_GROUP2 = "Bitcoin slides to five-month low amid wider sell-off";

    public Toolkit myTK;

    public NewsClassifier() {
        myTK = new Toolkit();
        myHTMLs = myTK.loadHTML();
        myStopWords = myTK.loadStopWords();

        loadData();
    }

    public static void main(String[] args) {
        NewsClassifier myNewsClassifier = new NewsClassifier();

        myNewsClassifier.newsCleanedContent = myNewsClassifier.preProcessing();

        myNewsClassifier.newsTFIDF = myNewsClassifier.calculateTFIDF(myNewsClassifier.newsCleanedContent);

        //Change the _index value to calculate similar based on a different news article.
        double[][] doubSimilarity = myNewsClassifier.newsSimilarity(0);

        System.out.println(myNewsClassifier.resultString(doubSimilarity, 10));

        String strGroupingResults = myNewsClassifier.groupingResults(myNewsClassifier.TITLE_GROUP1, myNewsClassifier.TITLE_GROUP2);
        System.out.println(strGroupingResults);
    }

    public void loadData() {
        int size = myHTMLs.length;
        newsTitles = new String[size];
        newsContents = new String[size];
        for (int i = 0; i<myHTMLs.length; i++) {
            newsTitles[i] = HtmlParser.getNewsTitle(myHTMLs[i]);
            newsContents[i] = HtmlParser.getNewsContent(myHTMLs[i]);
        }

    }

    public String[] preProcessing() {
        String[] myCleanedContent = null;
        myCleanedContent = new String[myHTMLs.length];
        for (int i = 0; i < myHTMLs.length; i++) {
            myCleanedContent[i] = NLP.textCleaning(newsContents[i]);
            myCleanedContent[i] = NLP.textLemmatization(myCleanedContent[i]);
            myCleanedContent[i] = NLP.removeStopWords(myCleanedContent[i], myStopWords);

        }


        return myCleanedContent;
    }

    public double[][] calculateTFIDF(String[] _cleanedContents) {
        String[] vocabularyList = buildVocabulary(_cleanedContents);
        double[][] myTFIDF = null;
        //TF: num of times word appears/total words in a document
        //IDF: total num of documents/num of documents where word appears
        //TFIDF = TF*IDF

        myTFIDF = new double[_cleanedContents.length][vocabularyList.length];
        int N = _cleanedContents.length;
        // first for loop for going through the articles
        for (int i = 0; i < _cleanedContents.length; i++) {
            //I will assign a number for each article starting from 1 onward
            String[] cleanedSentence = _cleanedContents[i].split(" ");
            int occurrencesInArticle = 0;
            int occurrencesOutArticle = 0;
            for (int j = 0; j<vocabularyList.length; j++) {
                String word = vocabularyList[j];
                occurrencesInArticle = 0;
                occurrencesOutArticle = 0;
                //linear search to find num of occurrences in the article
                for (int x = 0; x<cleanedSentence.length; x++) {
                    if (word.equals(cleanedSentence[x])) {
                        occurrencesInArticle++;
                    }
                }
                //another search to check if the word exists in the other articles
                //I will split each sentence into a new list then compare the words individually
                for (int x=0; x<_cleanedContents.length; x++) {
                    String[] cleanedS;
                    cleanedS = _cleanedContents[x].split(" ");
                    boolean inside = false;
                    for (int p = 0; p<cleanedS.length; p++) {
                        if (cleanedS[p].equals(word)) {
                            inside = true;
                            break;
                        }
                    }
                    if (inside) occurrencesOutArticle++;

                }
                //first I will calculate TF
                double TF = (double) occurrencesInArticle /cleanedSentence.length;
                //then I will calculate IDF
                double IDF = (double) N /occurrencesOutArticle;
                IDF = Math.log(IDF) + 1;
                double TFIDF = TF*IDF;
                myTFIDF[i][j] = TFIDF;


            }
        }







        return myTFIDF;
    }

    public String[] buildVocabulary(String[] _cleanedContents) {
        String[] arrayVocabulary = null;
        int size = 1;
        //one for loop to go through each cleaned content index,
        //second for loop to get each word, and compare it to the arrayVocab, if a match is found the word isn't added
        for (int i = 0; i < _cleanedContents.length; i++) {
            String[] cleanedSentence = _cleanedContents[i].split(" ");
            for (int j = 0; j<cleanedSentence.length; j++) {
                String word = cleanedSentence[j];
                boolean found = false;
                //if the
                if (arrayVocabulary == null) {
                    arrayVocabulary = new String[1];
                    arrayVocabulary[0] = word;
                }
                //now since we have the word, we should compare it to the previous words in the list
                for (int y = 0; y<arrayVocabulary.length; y++) {
                    if (word.equals(arrayVocabulary[y])) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    //I will make it where if the array gets full, I will add 1 new spot
                    if (arrayVocabulary.length == size) {
                        String[] newArray = new String[size+1];
                        //now I'll copy the array onto the new array
                        for (int x = 0; x<newArray.length-1; x++){
                            newArray[x] = arrayVocabulary[x];
                        }
                        arrayVocabulary = new String[size+1];
                        arrayVocabulary = newArray;
                    }
                    size++;
                    arrayVocabulary[size - 1] = word;
                }
            }
        }



        return arrayVocabulary;
    }



    public double[][] newsSimilarity(int _newsIndex) {
        double[][] mySimilarity = new double[newsCleanedContent.length][2];
        Vector current = new Vector(newsTFIDF[_newsIndex]);
        //  e.g.[1][0.32313]
        for (int i = 0; i< newsTFIDF.length; i++) {
            mySimilarity[i][0] = i;
            Vector _v = new Vector(newsTFIDF[i]);
            mySimilarity[i][1] = current.cosineSimilarity(_v);
        }
        //I made a bubble sort method below
        mySimilarity = bubsort(mySimilarity);



        return mySimilarity;
    }

    public double[][] bubsort(double[][] mySim) {
        for (int i = 0; i < mySim.length; i++){
            for (int x = 0; x<mySim.length-1; x++) {
                if (mySim[x][1] < mySim[x+1][1]) {
                    double temp = mySim[x][1];
                    double temp2 = mySim[x][0];
                    mySim[x][1] = mySim[x+1][1];
                    mySim[x+1][1] = temp;
                    mySim[x][0] = mySim[x+1][0];
                    mySim[x+1][0] = temp2;

                }
            }

        }
        return mySim;


    }

    public String groupingResults(String _firstTitle, String _secondTitle) {
        int[] arrayGroup1 = new int[newsTitles.length];
        int[] arrayGroup2 = new int[newsTitles.length];
        int firstIndex = 0;
        int secondIndex = 0;
        //first I will find the two indexes
        for (int i = 0; i< newsTitles.length; i++) {
            if (newsTitles[i].equals(_firstTitle)) {
                firstIndex = i;
            }
        }
        for (int i = 0; i< newsTitles.length; i++) {
            if (newsTitles[i].equals(_secondTitle)) {
                secondIndex = i;
            }
        }
        int firstListSize = 0;
        int secondListSize = 0;
        Vector D1 = new Vector(newsTFIDF[firstIndex]);
        Vector D2 = new Vector(newsTFIDF[secondIndex]);
        for (int i = 0; i < newsTitles.length; i++){
            Vector current = new Vector(newsTFIDF[i]);
            if (D1.cosineSimilarity(current) > D2.cosineSimilarity(current)) {
                arrayGroup1[firstListSize] = i;
                firstListSize++;
            }
            else {
                arrayGroup2[secondListSize] = i;
                secondListSize++;
            }
        }
        //now that I populated the lists, I want to make them where they do not have null indexes
        int [] newArrayGroup1 = new int[firstListSize];
        int [] newArrayGroup2 = new int[secondListSize];
        for (int i = 0; i < newArrayGroup1.length; i++) {
            newArrayGroup1[i] = arrayGroup1[i];
        }
        for (int i = 0; i < newArrayGroup2.length; i++) {
            newArrayGroup2[i] = arrayGroup2[i];
        }
        arrayGroup1 = newArrayGroup1;
        arrayGroup2 = newArrayGroup2;



        return resultString(arrayGroup1, arrayGroup2);
    }

    public String resultString(double[][] _similarityArray, int _groupNumber) {
        StringBuilder mySB = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        for (int j = 0; j < _groupNumber; j++) {
            for (int k = 0; k < _similarityArray[j].length; k++) {
                if (k == 0) {
                    mySB.append((int) _similarityArray[j][k]).append(" ");
                } else {
                    String formattedCS = decimalFormat.format(_similarityArray[j][k]);
                    mySB.append(formattedCS).append(" ");
                }
            }
            mySB.append(newsTitles[(int) _similarityArray[j][0]]).append("\r\n");
        }
        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

    public String resultString(int[] _firstGroup, int[] _secondGroup) {
        StringBuilder mySB = new StringBuilder();
        mySB.append("There are ").append(_firstGroup.length).append(" news in Group 1, and ").append(_secondGroup.length).append(" in Group 2.\r\n").append("=====Group 1=====\r\n");

        for (int i : _firstGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }
        mySB.append("=====Group 2=====\r\n");
        for (int i : _secondGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }

        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

}

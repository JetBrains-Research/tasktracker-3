public class LevenshteinDistance1717 {

    public static void main(String[] args) {

        if(args.length % 2 != 0){
            System.out.println("Invalid number of arguments, enter word pairs only.");
            System.exit(0);
        }

        for(int i = 0; i < args.length; i += 2){
            String word1 = args[i];
            String word2 = args[i+1];

            int[][] matrix = new int[word1.length()+1][word2.length()+1];

            for(int j = 0; j <= word1.length(); j++){
                matrix[j][0] = j;
            }

            for(int j = 0; j <= word2.length(); j++){
                matrix[0][j] = j;
            }

            for(int j = 1; j <= word1.length(); j++){
                for(int k = 1; k <= word2.length(); k++){
                    if(word1.charAt(j-1) == word2.charAt(k-1)){
                        matrix[j][k] = matrix[j-1][k-1];
                    } else {
                        int delete = matrix[j-1][k] + 1;
                        int insert = matrix[j][k-1] + 1;
                        int substitute = matrix[j-1][k-1] + 1;
                        matrix[j][k] = Math.min(Math.min(delete, insert), substitute);
                    }
                }
            }

            System.out.println(matrix[word1.length()][word2.length()]);
        }
    }

}
    
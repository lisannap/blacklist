import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class BlacklistSearcher {

    public static void main(String[] args) {
        String nameToValidate = args != null && args.length > 0 ? args[0] : "Bin Laden";
        String blackListFileName = args != null && args.length > 1 ? args[1] : "blacklist.txt";
        String noiseListFileName = args != null && args.length > 2 ? args[2] : "noiseList.txt";
        try {
           searchFromBlacklist(nameToValidate, blackListFileName, noiseListFileName);
        }
        catch (Exception e) {
          System.out.println(e);
        }

    }

    public static Set<String> searchFromBlacklist(String nameToValidate, String blackListFileName, String noiseListFileName) {
        ArrayList<String> blackList = readFileLinesToList(blackListFileName);
        String[] nameToValidateParts = convertToSearchListAndRemoveNoise(nameToValidate, noiseListFileName);
        Set<String> potentialMatches = nameToValidateParts.length > 0 ? findMatches(nameToValidateParts, blackList) : new HashSet<>();
        System.out.printf("Potential matches to %s: \n", nameToValidate);
        if (potentialMatches.size() > 0) {
            potentialMatches.forEach(match -> System.out.println(match));
        } else {
            System.out.println("None");
        }
        return potentialMatches;

    }

    private static ArrayList<String> readFileLinesToList(String filePath) {
        Scanner scanner = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            URL url = BlacklistSearcher.class.getResource(filePath);
            scanner = new Scanner(new File(url.getPath()));
            while (scanner.hasNextLine()){
                list.add(scanner.nextLine());
            }
            return list;
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            return list;
        }
    }

    private static String[] convertToSearchListAndRemoveNoise(String nameToValidate, String noiseListFileName) {
        List<String> noiseList = readFileLinesToList(noiseListFileName).stream().map(line -> line.toLowerCase().trim()).collect(Collectors.toList());
        // remove punctuation and split to words
        String[] nameToValidateParts = nameToValidate.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        // filter out noise words from list
        nameToValidateParts = Arrays.stream(nameToValidateParts).filter(part -> !noiseList.contains(part)).toArray(String[]::new);
        return nameToValidateParts;
    }

    private static Set<String> findMatches(String[] nameToValidateParts, ArrayList<String> blackList) {
        Set<String> potentialMatches = new HashSet<>(blackList);
        for (String partToValidate : nameToValidateParts) {
            Set<String> toBeRemoved = new HashSet<>();
            for (String potentialMatch : potentialMatches) {
                Set<String> potentialMatchParts = new HashSet<>(Arrays.asList(potentialMatch.trim().toLowerCase().split("\\s+")));
                if (!potentialMatchParts.contains(partToValidate)) {
                    // not a potential match
                    toBeRemoved.add(potentialMatch);
                }
            }
            potentialMatches.removeAll(toBeRemoved);
            //no need to iterate over other name parts if already no potential matches
            if (potentialMatches.size() == 0) {
                break;
            }
        }
        return potentialMatches;
    }

}

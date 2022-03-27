import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBlacklistSearcher {

    BlacklistSearcher blacklistSearcher;

    @BeforeEach
    void setUp() {
        blacklistSearcher = new BlacklistSearcher();
    }

    @Test
    @DisplayName("Should find name in blacklist")
    void testBlacklistName() {
        HashSet<String> expected = new HashSet<>(Arrays.asList("Osama Bin Laden"));
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Osama Laden", "blacklist.txt", "noiselist.txt"),
                "Should find a blacklist match");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Osama Bin Laden", "blacklist.txt", "noiselist.txt"),
                "Should find a blacklist match");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Bin Laden, Osama", "blacklist.txt", "noiselist.txt"),
                "Should find a blacklist match");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Laden Osama Bin", "blacklist.txt", "noiselist.txt"),
                "Should find a blacklist match");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("to the osama bin laden", "blacklist.txt", "noiselist.txt"),
                "Should find a blacklist match");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("osama and bin laden", "blacklist.txt", "noiselist.txt"),
                "Should find a blacklist match");
    }

    @Test
    @DisplayName("Should not find a blacklist match")
    void testWhitelistName() {
        HashSet<String> expected = new HashSet<>(Arrays.asList());
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Kersti Kaljulaid", "blacklist.txt", "noiselist.txt"),
                "Should not find a blacklist match for non-blacklist, non-noise phrase");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Bin", "blacklist.txt", "noiselist.txt"),
                "Should not find a blacklist match for noise word");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("", "blacklist.txt", "noiselist.txt"),
                "Should not find a blacklist match for empty input");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Osamartin Ladenson", "blacklist.txt", "noiselist.txt"),
                "Should not find a blacklist match where match word is part of word");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("Osama Ladenson", "blacklist.txt", "noiselist.txt"),
                "Should not find a blacklist match for match word + longer word combination");
        assertEquals(expected, blacklistSearcher.searchFromBlacklist("-", "blacklist.txt", "noiselist.txt"),
                "Should not find a blacklist match");
    }


}

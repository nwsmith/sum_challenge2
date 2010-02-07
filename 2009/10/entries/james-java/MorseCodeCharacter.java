import java.util.Arrays;
import java.util.List;

public enum MorseCodeCharacter {
    A("\\.-.*", 2),
    B("-\\.\\.\\..*", 4),
    C("-\\.-\\..*", 4),
    D("-\\.\\..*", 3),
    E("\\..*", 1),
    F("\\.\\.-\\..*", 4),
    G("--\\..*", 3),
    H("\\.\\.\\.\\..*", 4),
    I("\\.\\..*", 2),
    J("\\.---.*", 4),
    K("-\\.-.*", 3),
    L("\\.-\\.\\..*", 4),
    M("--.*", 2),
    N("-\\..*", 2),
    O("---.*", 3),
    P("\\.--\\..*", 4),
    Q("--\\.-.*", 4),
    R("\\.-\\..*", 3),
    S("\\.\\.\\..*", 3),
    T("-.*", 1),
    U("\\.\\.-.*", 3),
    V("\\.\\.\\.-.*", 4),
    W("\\.--.*", 3),
    X("-\\.\\.-.*", 4),
    Y("-\\.--.*", 4),
    Z("--\\..*", 4);

    public static List<MorseCodeCharacter> allCharacters = Arrays.asList(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z);

    private String _regex;
    private Integer _length;

    MorseCodeCharacter(String regex, Integer length) {
        _regex = regex;
        _length = length;
    }

    public String getRegex() {
        return _regex;
    }

    public String getName() {
        return super.name();
    }

    public Integer getLength() {
        return _length;
    }
}

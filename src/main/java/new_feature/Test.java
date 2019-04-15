package new_feature;

import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fengcaiwen
 * @since 4/9/2019
 */
public class Test {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("gender", "1");
        map.put("login_user_id", "12861553");
        map.put("session_id", "15535053828e85530dbac2ce691c1641");
        map.put("user_interest", "14");
        String parameters = Joiner.on("&").join(map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.toList()));

        System.out.println(parameters);

        System.out.println(map.entrySet());
        System.out.println(map.toString());
    }
}
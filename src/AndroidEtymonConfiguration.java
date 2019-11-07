public class AndroidEtymonConfiguration  {
   public static boolean TEST_MODE = false;

    public static String COMPONENT_START_MARK_PREFIX =  "ETYMON-ALFA-";
    public static String COMPONENT_END_MARK_PREFIX =  "ETYMON-OMEGA-";

    public static String getProjectComponentsJson() {
        return TEST_MODE ? "./ProjectComponents-Test.json" : "./ProjectComponents.json";
    }

    public static String getProjectResourceRoot() {
        return TEST_MODE ? "BaseProjectKotlin-Test" : "BaseProjectKotlin";
    }
}


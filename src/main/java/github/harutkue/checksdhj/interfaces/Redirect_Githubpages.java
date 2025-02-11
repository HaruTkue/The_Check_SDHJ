package github.harutkue.checksdhj.interfaces;

import java.util.List;
import java.util.Map;

public class Redirect_Githubpages implements Access_Templature_Interface {
    public boolean CheckMethods(String Domain){
        return true;
    }
    public String Main_Access(String Access_Domain){
        return "TestValue";
    }
    public List<String> getProviderService(){
        return null;
    }
}

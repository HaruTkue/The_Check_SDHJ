package github.harutkue.checksdhj.interfaces;

import github.harutkue.checksdhj.interfaces.getnet.getdns;

public class Redirect_Githubpages implements Access_Templature_Interface {
    public boolean CheckMethods(String Domain){
        return true;
    }
    public String Main_Access(String Access_Domain){
        return "TestValue";
    }
}

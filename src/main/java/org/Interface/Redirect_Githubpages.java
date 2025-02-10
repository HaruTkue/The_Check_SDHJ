package org.Interface;

public class Redirect_Githubpages implements Access_Templature_Interface {
    public boolean CheckMethods(String Domain){
        return true;
    }
    public String Main_Access(String Access_Domain){
        return "TestValue";
    }
}

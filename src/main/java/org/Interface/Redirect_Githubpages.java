package org.Interface;

public class Redirect_Githubpages implements Access_Templature_Interface {
    public boolean CheckMethods(String Test_URL){
        return true;
    }
    public String Main_Access(String Access_URL){
        return "TestValue";
    }
}

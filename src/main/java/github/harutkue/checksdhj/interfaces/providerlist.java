package github.harutkue.checksdhj.interfaces;
import java.util.HashMap;
import java.util.Map;


//Providerのリストを出す専用のクラス
public class providerlist {
    public static Map<String,String> retProvider(){
        Map<String,String> ProviderList = new HashMap<>();
        //ここに追加するプロバイダを記述する。
        //ProviderList.put("addressの一部","provider")
        ProviderList.put("104.16.","cloudflare");
        ProviderList.put("104.17","cloudflare");

        return ProviderList;
    }
}
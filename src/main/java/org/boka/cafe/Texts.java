package org.boka.cafe;

import org.boka.cafe.pojo.KeyForText;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Texts {

    private static HashMap<KeyForText, String> textData = new HashMap<>();
    private static Texts texts;

    private Texts(JSONObject jsonTexts) {
        jsonTexts.toMap().values().forEach(obj -> {
            ArrayList<String> array = (ArrayList) obj;
            textData.put(new KeyForText(array.get(0), array.get(1)), array.get(2));
        });
        System.out.println("Texts successful load!");
    }

    public static void registerTexts(JSONObject jsonTexts) {
        if (texts == null) {
            texts = new Texts(jsonTexts);
        } else {
            throw new RuntimeException("Texts yet setting!");
        }
    }

    public static String getText(KeyForText key) {
        return textData.get(key);
    }

    public static String getNameFieldByText(String text) {
        Optional<Map.Entry<KeyForText, String>> opt = textData.entrySet().stream()
                .filter(keyForTextStringEntry -> keyForTextStringEntry.getValue().equalsIgnoreCase(text)).findAny();
        if (opt.isPresent()) {
            return opt.get().getKey().getTextName();
        } else {
            return "";
        }
    }

}

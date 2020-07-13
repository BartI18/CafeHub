package org.boka.cafe.pojo;

import org.boka.cafe.Misc.Misc;

import java.util.Objects;

public class KeyForText {

    private String language;
    private String textName;

    public KeyForText(String textName, String language) {
        this.textName = textName;
        this.language = language;
    }

    public KeyForText(String textName, Misc.LANGUAGES language) {
        this.textName = textName;
        this.language = language.name();
    }

    public String getLanguage() {
        return language;
    }

    public String getTextName() {
        return textName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, textName);
    }

    @Override
    public String toString() {
        return String.format("KeyForText[language=%s, textName=%s]", language, textName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyForText)) {
            return false;
        }
        KeyForText otherObj = (KeyForText) obj;
        if (Objects.equals(this.language, otherObj.language) && Objects.equals(this.textName, otherObj.textName)) {
            return true;
        }
        return false;
    }
}

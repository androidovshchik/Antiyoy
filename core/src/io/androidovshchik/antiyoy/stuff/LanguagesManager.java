package io.androidovshchik.antiyoy.stuff;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LanguagesManager {
    private static final String DEFAULT_LANGUAGE = "en_UK";
    private static final String LANGUAGES_FILE = "languages.xml";
    private static LanguagesManager _instance = null;
    private HashMap<String, String> _language;
    private String _languageName;

    private LanguagesManager() {
        this._language = null;
        this._languageName = null;
        this._language = new HashMap();
        this._languageName = Locale.getDefault().toString();
        if (!loadLanguage(this._languageName)) {
            loadLanguage(DEFAULT_LANGUAGE);
            this._languageName = DEFAULT_LANGUAGE;
        }
    }

    public static void initialize() {
        _instance = null;
    }

    public static LanguagesManager getInstance() {
        if (_instance == null) {
            _instance = new LanguagesManager();
        }
        return _instance;
    }

    public String getLanguage() {
        return this._languageName;
    }

    public void setLanguage(String langName) {
        loadLanguage(langName);
        this._languageName = langName;
    }

    public String getString(String key) {
        if (this._language != null) {
            String string = (String) this._language.get(key);
            if (string != null) {
                return string;
            }
        }
        return key;
    }

    public String getString(String key, Object... args) {
        return String.format(getString(key), args);
    }

    public ArrayList<LanguageChooseItem> getChooseListItems() {
        ArrayList<LanguageChooseItem> result = new ArrayList();
        try {
            NodeList languages = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Gdx.files.internal(LANGUAGES_FILE).read()).getDocumentElement().getElementsByTagName("language");
            int numLanguages = languages.getLength();
            for (int i = 0; i < numLanguages; i++) {
                Node language = languages.item(i);
                LanguageChooseItem chooseItem = new LanguageChooseItem();
                chooseItem.name = language.getAttributes().getNamedItem("name").getTextContent();
                chooseItem.title = language.getAttributes().getNamedItem("title").getTextContent();
                chooseItem.author = language.getAttributes().getNamedItem("author").getTextContent();
                result.add(chooseItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean loadLanguage(String languageName) {
        try {
            NodeList languages = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Gdx.files.internal(LANGUAGES_FILE).read()).getDocumentElement().getElementsByTagName("language");
            int numLanguages = languages.getLength();
            for (int i = 0; i < numLanguages; i++) {
                Node language = languages.item(i);
                if (language.getAttributes().getNamedItem("name").getTextContent().equals(languageName)) {
                    this._language.clear();
                    NodeList strings = ((Element) language).getElementsByTagName("string");
                    int numStrings = strings.getLength();
                    for (int j = 0; j < numStrings; j++) {
                        NamedNodeMap attributes = strings.item(j).getAttributes();
                        this._language.put(attributes.getNamedItem("key").getTextContent(), attributes.getNamedItem("value").getTextContent().replace("<br />", "\n"));
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error loading languages file languages.xml");
            return false;
        }
    }
}

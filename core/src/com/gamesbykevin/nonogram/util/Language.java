package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import static com.gamesbykevin.nonogram.preferences.MyPreferences.KEY_OPTIONS_LANGUAGE;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.getOptionValue;

public class Language {

    //object used for localization
    private static I18NBundle MY_BUNDLE;

    public static final String LANGUAGE_BUNDLE_DIR = "i18n/MyBundle";

    public static final String ENCODING_DEFAULT = "UTF-8";

    //no language selected by default
    public static final int NO_LANGUAGE = -1;

    public static final String TEXT_RESUME  = "textResume";
    public static final String TEXT_PLAY    = "textPlay";
    public static final String TEXT_DAILY   = "textDaily";
    public static final String TEXT_OPTIONS = "textOptions";
    public static final String TEXT_CREDITS = "textCredits";
    public static final String TEXT_MENU = "textMenu";
    public static final String TEXT_BLACK = "textBlack";
    public static final String TEXT_GRAY = "textGray";
    public static final String TEXT_COLOR = "textColor";
    public static final String TEXT_PAGE = "textPage";
    public static final String TEXT_DESC = "textDescription";
    public static final String TEXT_COLORS = "textColors";
    public static final String TEXT_SIZE = "textSize";
    public static final String TEXT_NEW    = "textNew";
    public static final String TEXT_YES    = "textYes";
    public static final String TEXT_NO     = "textNo";
    public static final String TEXT_SAVE = "textSave";
    public static final String TEXT_EXIT = "textExit";
    public static final String TEXT_RESET= "textReset";
    public static final String TEXT_LANGUAGE = "textLanguage";
    public static final String TEXT_VIBRATE = "textVibrate";
    public static final String TEXT_SOUND = "textSound";
    public static final String TEXT_MUSIC = "textMusic";
    public static final String TEXT_FILL = "textFill";
    public static final String TEXT_LOCK = "textLock";
    public static final String TEXT_MODE = "textMode";
    public static final String TEXT_NEXT = "textNext";
    public static final String TEXT_TUTORIAL = "textTutorial";

    public static final String TEXT_SHARE_SUBJECT = "textSharingSubject";
    public static final String TEXT_SHARE_TITLE = "textSharingTitle";
    public static final String TEXT_SHARE_BODY = "textSharingBody";
    public static final String TEXT_SHARE_RATING_FAILURE = "textRatingFailure";

    public static final String TEXT_TUTORIAL_PAGE_1 = "textTutorialPage1";
    public static final String TEXT_TUTORIAL_PAGE_2 = "textTutorialPage2";
    public static final String TEXT_TUTORIAL_PAGE_3 = "textTutorialPage3";
    public static final String TEXT_TUTORIAL_PAGE_4 = "textTutorialPage4";
    public static final String TEXT_TUTORIAL_PAGE_5 = "textTutorialPage5";
    public static final String TEXT_TUTORIAL_PAGE_6 = "textTutorialPage6";
    public static final String TEXT_TUTORIAL_PAGE_7 = "textTutorialPage7";
    public static final String TEXT_TUTORIAL_PAGE_8 = "textTutorialPage8";
    public static final String TEXT_TUTORIAL_PAGE_9 = "textTutorialPage9";
    public static final String TEXT_TUTORIAL_PAGE_10 = "textTutorialPage10";

    //Select date screen
    public static final String MON_JAN = "monthJan";
    public static final String MON_FEB = "monthFeb";
    public static final String MON_MAR = "monthMar";
    public static final String MON_APR = "monthApr";
    public static final String MON_MAY = "monthMay";
    public static final String MON_JUN = "monthJun";
    public static final String MON_JUL = "monthJul";
    public static final String MON_AUG = "monthAug";
    public static final String MON_SEP = "monthSep";
    public static final String MON_OCT = "monthOct";
    public static final String MON_NOV = "monthNov";
    public static final String MON_DEC = "monthDec";

    public static final String DAY_SUN = "daySun";
    public static final String DAY_MON = "dayMon";
    public static final String DAY_TUE = "dayTue";
    public static final String DAY_WED = "dayWed";
    public static final String DAY_THU = "dayThu";
    public static final String DAY_FRI = "dayFri";
    public static final String DAY_SAT = "daySat";

    //Options screen


    //languages commented out here we don't have the character set for
    public enum Languages {
        Afrikaans("Afrikaans", "af", ""),
        Albanian("Albanian", "sq", ""),
        //Amharic("Amharic", "am", ""),
        //Arabic("Arabic", "ar", ""),
        //Armenian("Armenian", "hy", ""),
        Azerbaijani("Azerbaijani", "az", ""),
        Basque("Basque", "eu", ""),
        //Belarusian("Belarusian", "be", ""),
        //Bengali("Bengali", "bn", ""),
        Bosnian("Bosnian", "bs", ""),
        //Bulgarian("Bulgarian", "bg", ""),
        Catalan("Catalan", "ca", ""),
        Cebuano("Cebuano", "ceb", ""),
        //ChineseSimplified("Chinese (Simplified)", "zh", "CN"),
        //ChineseTraditional("Chinese (Traditional)", "zh", "TW"),
        Corsican("Corsican", "co", ""),
        Croatian("Croatian", "hr", ""),
        Czech("Czech", "cs", ""),
        Danish("Danish", "da", ""),
        Dutch("Dutch", "nl", ""),
        English("English", "en", ""),
        Esperanto("Esperanto", "eo", ""),
        Estonian("Estonian", "et", ""),
        Finnish("Finnish", "fi", ""),
        French("French", "fr", ""),
        Frisian("Frisian", "fy", ""),
        Galician("Galician", "gl", ""),
        //Georgian("Georgian", "ka", ""),
        German("German", "de", ""),
        //Greek("Greek", "el", ""),
        //Gujarati("Gujarati", "gu", ""),
        Haitian("Haitian Creole", "ht", ""),
        Hausa("Hausa", "ha", ""),
        Hawaiian("Hawaiian", "haw", "639"),
        //Hebrew("Hebrew", "he", ""),
        //Hindi("Hindi", "hi", ""),
        Hmong("Hmong", "hmn", "639"),
        Hungarian("Hungarian", "hu", ""),
        Icelandic("Icelandic", "is", ""),
        Igbo("Igbo", "ig", ""),
        Indonesian("Indonesian", "id", ""),
        Irish("Irish", "ga", ""),
        Italian("Italian", "it", ""),
        //Japanese("Japanese", "ja", ""),
        Javanese("Javanese", "jw", ""),
        //Kannada("Kannada", "kn", ""),
        //Kazakh("Kazakh", "kk", ""),
        //Khmer("Khmer", "km", ""),
        //Korean("Korean", "ko", ""),
        Kurdish("Kurdish", "ku", ""),
        //Kyrgyz("Kyrgyz", "ky", ""),
        //Lao("Lao", "lo", ""),
        Latin("Latin", "la", ""),
        Latvian("Latvian", "lv", ""),
        Lithuanian("Lithuanian", "lt", ""),
        Luxembourgish("Luxembourgish", "lb", ""),
        //Macedonian("Macedonian", "mk", ""),
        Malagasy("Malagasy", "mg", ""),
        Malay("Malay", "ms", ""),
        //Malayalam("Malayalam", "ml", ""),
        Maltese("Maltese", "mt", ""),
        Maori("Maori", "mi", ""),
        //Marathi("Marathi", "mr", ""),
        //Mongolian("Mongolian", "mn", ""),
        //Myanmar("Myanmar (Burmese)", "my", ""),
        //Nepali("Nepali", "ne", ""),
        Norwegian("Norwegian", "no", ""),
        Nyanja("Nyanja (Chichewa)", "ny", ""),
        //Pashto("Pashto", "ps", ""),
        //Persian("Persian", "fa", ""),
        Polish("Polish", "pl", ""),
        Portuguese("Portuguese", "pt", ""),
        //Punjabi("Punjabi", "pa", ""),
        Romanian("Romanian", "ro", ""),
        //Russian("Russian", "ru", ""),
        Samoan("Samoan", "sm", ""),
        ScotsGaelic("Scots Gaelic", "gd", ""),
        //Serbian("Serbian", "sr", ""),
        Sesotho("Sesotho", "st", ""),
        Shona("Shona", "sn", ""),
        //Sindhi("Sindhi", "sd", ""),
        //Sinhala("Sinhala (Sinhalese)", "si", ""),
        Slovak("Slovak", "sk", ""),
        Slovenian("Slovenian", "sl", ""),
        Somali("Somali", "so", ""),
        Spanish("Spanish", "es", ""),
        Sundanese("Sundanese", "su", ""),
        Swahili("Swahili", "sw", ""),
        Swedish("Swedish", "sv", ""),
        Tagalog("Tagalog (Filipino)", "tl", ""),
        //Tajik("Tajik", "tg", ""),
        //Tamil("Tamil", "ta", ""),
        //Telugu("Telugu", "te", ""),
        //Thai("Thai", "th", ""),
        Turkish("Turkish", "tr", ""),
        //Ukrainian("Ukrainian", "uk", ""),
        //Urdu("Urdu", "ur", ""),
        Uzbek("Uzbek", "uz", ""),
        Vietnamese("Vietnamese", "vi", ""),
        Welsh("Welsh", "cy", ""),
        Xhosa("Xhosa", "xh", ""),
        //Yiddish("Yiddish", "yi", ""),
        Yoruba("Yoruba", "yo", ""),
        Zulu("Zulu", "zu", "");

        private final String desc;
        private final String languageCode;
        private final String countryCode;

        Languages(String desc, String languageCode, String countryCode) {
            this.desc = desc;
            this.languageCode = languageCode;
            this.countryCode = countryCode;
        }

        public String getDesc() {
            return this.desc;
        }

        public String getLanguageCode() {
            return this.languageCode;
        }

        public String getCountryCode() {
            return this.countryCode;
        }
    }

    public static I18NBundle getMyBundle() {

        if (MY_BUNDLE == null) {

            //do we have a language setting
            final int index = getOptionValue(KEY_OPTIONS_LANGUAGE, NO_LANGUAGE);

            //if we selected a language set it
            if (index >= 0) {
                changeMyBundle(index);
            } else {
                createBundle();
            }
        }

        return MY_BUNDLE;
    }

    public static void changeMyBundle(int index) {

        //list of languages
        Language.Languages[] languages = Language.Languages.values();

        //language settings
        final String countryCode = languages[index].getCountryCode();
        final String languageCode = languages[index].getLanguageCode();

        Locale locale;

        //create the locale
        if (countryCode != null && countryCode.length() > 0) {
            locale = new Locale(languageCode, countryCode);
        } else {
            locale = new Locale(languageCode);
        }

        //create new bundle with the specified language and country code
        createBundle(locale);
    }

    private static void createBundle() {
        createBundle(null);
    }

    private static void createBundle(Locale locale) {
        if (locale != null) {
            MY_BUNDLE = I18NBundle.createBundle(FileHelper.load(LANGUAGE_BUNDLE_DIR), locale, ENCODING_DEFAULT);
        } else {
            MY_BUNDLE = I18NBundle.createBundle(FileHelper.load(LANGUAGE_BUNDLE_DIR), ENCODING_DEFAULT);
        }
    }
}
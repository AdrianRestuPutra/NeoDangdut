package com.pitados.neodangdut.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by adrianrestuputranto on 6/17/16.
 */
public class FontLoader {
    public enum FontType {
        HEADLINE_LIGHT, HEADLINE_REGULAR, HEADLINE_BOLD,
        BODY_LIGHT, BODY_REGULAR, BODY_BOLD
    }

    private static Typeface[] fonts = new Typeface[3];

    private static String[] fontPath = {
            "fonts/Oswald-Light.ttf",
            "fonts/Oswald-Regular.ttf",
            "fonts/Oswald-Bold.ttf"
    };

    private static boolean isLoaded = false;


    public static Typeface getTypeFace(Context context, FontType type) {
        if(!isLoaded) {
            loadFonts(context);
        }

        switch (type) {
            case HEADLINE_LIGHT:
                return fonts[0];

            case HEADLINE_REGULAR:
                return fonts[1];

            case HEADLINE_BOLD:
                return  fonts[2];
        }
        return fonts[0];
    }

    private static void loadFonts(Context context) {
        for(int i = 0; i < fontPath.length; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        isLoaded = true;
    }
}

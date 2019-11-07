/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.gameControllerHandler;

import android.view.View;

public class GCUIUtils {

    public static boolean isFocused(View view) {
        return view.isFocused();
    }

    public static void focus(final View view) {
        if (!view.isFocusable()) {
            view.setFocusable(true);
        }

        view.requestFocus();
    }

    public static void performClick(View view) {
        view.performClick();
    }
}

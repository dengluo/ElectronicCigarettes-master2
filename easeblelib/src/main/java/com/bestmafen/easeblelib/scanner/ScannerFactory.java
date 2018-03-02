package com.bestmafen.easeblelib.scanner;

import android.os.Build;

/**
 * This class is used to create a {@link IEaseScanner} object according to different API level.
 */
public class ScannerFactory {

    public static IEaseScanner createScanner() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new ScannerOld();
        } else {
            return new ScannerNew();
        }
    }
}

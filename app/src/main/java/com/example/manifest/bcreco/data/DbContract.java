package com.example.manifest.bcreco.data;

import android.provider.BaseColumns;
import android.text.TextUtils;

public final class DbContract {

    private DbContract(){}

    /**
     * Inner class that defines constant values for the barcode database table.
     */
    public static final class BarcodeEntry implements BaseColumns {

        /** Name of database table for barcodes*/
        public static final String TABLE_NAME = "T_Barcode";

        /**
         * Barcode number.
         *
         * Type: VARCHAR
         */
        public static final String COLUMN_BARCODE = "Barcode";

        /**
         * Internal code of the article (unique for sizes).
         *
         * Type: INT
         */
        public static final String COLUMN_ID_PLU = "ID_PLU";

        //TODO: test.
        /**
         * For fast searching in database, it is necessary that the length
         * of the barcode string must be 12.
         *
         * @param barcode any length string.
         * @return String barcode with length equal to 12.
         */
        public static String getValidBarcode(String barcode) {
            if (TextUtils.isEmpty(barcode)) {
                return null;
            }
            int barcodeLength = barcode.length();
            if (barcodeLength == 12) {
                return barcode;
            } else if (barcodeLength > 12) {
                return barcode.substring(barcodeLength - 12);
            } else {
                return String.format("%0" + (12 - barcodeLength) + "d%s", 0, barcode);
            }
        }
    }

    /**
     * Inner class that defines constant values for the PLU database table.
     */
    public static final class PluEntry implements BaseColumns {

        /** Name of database table for PLU*/
        public static final String TABLE_NAME = "T_PLU";

        /**
         * Internal code of the article (unique for sizes).
         *
         * Type: INT
         */
        public static final String COLUMN_ID = "ID";

        /**
         * ID of model.
         *
         * Type: INT
         */
        public static final String COLUMN_ID_MODEL = "ID_Models";

        /**
         * ID of color.
         *
         * Type: INT
         */
        public static final String COLUMN_COLOR = "ID_ColorVend";

        /**
         * ID of size.
         *
         * Type: INT
         */
        public static final String COLUMN_ID_SIZE = "ID_Sizes";

        /**
         * TradeX current price.
         *
         * Type: MONEY
         */
        public static final String COLUMN_CURRENT_PRICE = "RecomRetailPrice";
    }

    /**
     * Inner class that defines constant values for the model database table.
     */
    public static final class ModelEntry implements BaseColumns {

        /** Name of database table for PLU*/
        public static final String TABLE_NAME = "T_Models";

        /**
         * ID of model.
         *
         * Type: INT
         */
        public static final String COLUMN_ID = "ID";

        /**
         * Model name.
         *
         * Type: VARCHAR
         */
        public static final String COLUMN_MODEL = "ModelID";

        /**
         * Model description.
         *
         * Type: VARCHAR
         */
        public static final String COLUMN_MODEL_DESC = "ModelDesc";

    }
    /**
     * Inner class that defines constant values for the colors database table.
     */
    public static final class ColorEntry implements BaseColumns {

        /** Name of database table for PLU*/
        public static final String TABLE_NAME = "T_ColorVend";

        /**
         * ID of color.
         *
         * Type: INT
         */
        public static final String COLUMN_ID_COLOR = "ID_ColorVend";

        /**
         * Color name(e.g. blue).
         *
         * Type: VARCHAR
         */
        public static final String COLUMN_COLOR = "Color";
    }
}

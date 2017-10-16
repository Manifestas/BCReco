package com.example.manifest.bcreco.data;

import android.provider.BaseColumns;

public final class DbContract {

    private DbContract(){}

    /**
     * Database name.
     */
    public static final String DB_NAME = "TradeX";

    //TODO: move this to the SharedPreference and ask a user to enter log/pass in settings menu
    /**
     * User login.
     */
    public static final String DB_LOGIN = "tv";

    /**
     * User password.
     */
    public static final String DB_PASSWORD = "12345678";

    public static final String DB_IP_ADDRESS = "192.168.14.2:1433";
    /**
     * Connection url.
     */
    public static final String DB_CONN_URL = "jdbc:jtds:sqlserver://"
                                             + DB_IP_ADDRESS + ";"
                                             + "databaseName=" + DB_NAME
                                             + ";user=" + DB_LOGIN
                                             + ";password=" + DB_PASSWORD + ";";

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
            if (barcode == null || barcode.length() == 0) {
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

        public static String queryModelColorSizeFromBarcodeTable(String barcode) {
            return "SELECT "
                        + PluEntry.COLUMN_ID_MODEL + ", "
                        + PluEntry.COLUMN_COLOR + ", "
                        + PluEntry.COLUMN_ID_SIZE
                    + " FROM " + PluEntry.TABLE_NAME
                    + " WHERE " + PluEntry.COLUMN_ID + " = "
                    + "(SELECT " + BarcodeEntry.COLUMN_ID_PLU
                    + " FROM " + BarcodeEntry.TABLE_NAME
                    + " WHERE " + BarcodeEntry.COLUMN_BARCODE
                    + " = '" + getValidBarcode(barcode) + "');";
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

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

    /** SQL statement. */
    public static final String INNER_JOIN = "INNER JOIN";
    /** SQL keyword. */
    public static final String ON = " ON ";
    /** A dot for sql command. */
    public static final String DOT = ".";
    /** A comma separator. */
    public static final String COMMA = ", ";
    /** A equal symbol. */
    public static final String EQUALS = " = ";

    /** Query from TradeX table for getting Goods parameters.
     * Currency price, model name, model desc, size name,
     * color name, season name, current exchange rate.
     */
    public static String  goodsQuery(String barcode) {
        return "SELECT "
                + PluEntry.TABLE_NAME + DOT + PluEntry.COLUMN_CURRENT_PRICE + COMMA
                + ModelEntry.TABLE_NAME + DOT + ModelEntry.COLUMN_MODEL + COMMA
                + ModelEntry.TABLE_NAME + DOT + ModelEntry.COLUMN_MODEL_DESC + COMMA
                + SizeEntry.TABLE_NAME + DOT + SizeEntry.COLUMN_SIZE_NAME + COMMA
                + ColorEntry.TABLE_NAME + DOT + ColorEntry.COLUMN_COLOR + COMMA
                + SeasonEntry.TABLE_NAME + DOT + SeasonEntry.COLUMN_SEASON + COMMA
                + ExchangeEntry.TABLE_NAME + DOT + ExchangeEntry.COLUMN_EXCHANGE_RATE + COMMA
            + "FROM " + BarcodeEntry.TABLE_NAME
                + INNER_JOIN + " " + PluEntry.TABLE_NAME
                + ON + BarcodeEntry.TABLE_NAME + DOT + BarcodeEntry.COLUMN_ID_PLU
                + EQUALS + PluEntry.TABLE_NAME + DOT + PluEntry.COLUMN_ID
                + INNER_JOIN + " " + ModelEntry.TABLE_NAME
                + ON + PluEntry.TABLE_NAME + DOT + PluEntry.COLUMN_ID_MODEL
                + EQUALS + ModelEntry.TABLE_NAME + DOT + ModelEntry.COLUMN_ID
                + INNER_JOIN + " " + SizeEntry.TABLE_NAME
                + ON + PluEntry.TABLE_NAME + DOT + PluEntry.COLUMN_ID_SIZE
                + EQUALS + SizeEntry.TABLE_NAME + DOT + SizeEntry.COLUMN_SIZE_ID
                + INNER_JOIN + " " + ColorEntry.TABLE_NAME
                + ON + PluEntry.TABLE_NAME + DOT + PluEntry.COLUMN_COLOR
                + EQUALS + ColorEntry.TABLE_NAME + DOT + ColorEntry.COLUMN_COLOR_ID
                + INNER_JOIN + " " + SeasonEntry.TABLE_NAME
                + ON + ModelEntry.TABLE_NAME + DOT + ModelEntry.COLUMN_SEASON_ID
                + EQUALS + SeasonEntry.TABLE_NAME + DOT + SeasonEntry.COLUMN_SEASON_ID
                + INNER_JOIN + " " + ExchangeEntry.TABLE_NAME
                + ON + ModelEntry.TABLE_NAME + DOT + ModelEntry.COLUMN_CURRENCY_ID
                + EQUALS + ExchangeEntry.TABLE_NAME + DOT + ExchangeEntry.COLUMN_CURRENCY_ID
            + "WHERE " + BarcodeEntry.TABLE_NAME + DOT +  BarcodeEntry.COLUMN_BARCODE
                + EQUALS + barcode + ";";
    }


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

        //TODO: add check sum.
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

        /**
         * ID currency of price(e.g. 3 - euro).
         *
         * Type: INT
         */
        public static final String COLUMN_CURRENCY_ID = "ExpCurrency";

        /**
         * ID of season.
         *
         * Type: INT
         */
        public static final String COLUMN_SEASON_ID = "Season";
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
        public static final String COLUMN_COLOR_ID = "ID_ColorVend";

        /**
         * Color name(e.g. blue).
         *
         * Type: VARCHAR
         */
        public static final String COLUMN_COLOR = "Color";
    }

    /**
     * Inner class that defines constant values for the Season database table.
     */
    public static final class SeasonEntry implements BaseColumns {

        /**
         * Name of database table for Seasons
         */
        public static final String TABLE_NAME = "T_Season";

        /**
         * ID of season.
         *
         * Type: INT
         */
        public static final String COLUMN_SEASON_ID = "ID";

        /**
         * Season name(e.g. winter2017-18).
         *
         * Type: VARCHAR
         */
        public static final String COLUMN_SEASON = "RefDesc";
    }

    /**
     * Inner class that defines constant values for the Exchange database table.
     */
    public static final class ExchangeEntry implements BaseColumns {

        /**
         * Name of database table for Exchange
         */
        public static final String TABLE_NAME = "T_Exchange";

        /**
         * ID of currency.
         *
         * Type: INT
         */
        public static final String COLUMN_CURRENCY_ID = "ID_Currency";

        /**
         * Exchange rate(e.g. 75 in the moment).
         *
         * Type: FLOAT
         */
        public static final String COLUMN_EXCHANGE_RATE = "ExchangeRate";
    }

    /**
     * Inner class that defines constant values for the Sizes database table.
     */
    public static final class SizeEntry implements BaseColumns {

        /**
         * Name of database table for Sizes.
         */
        public static final String TABLE_NAME = "T_Sizes";

        /**
         * ID of size(e.g. 12).
         *
         * Type: INT
         */
        public static final String COLUMN_SIZE_ID = "ID";

        /**
         * Size name(e.g. 39)
         *
         * Type: VARSHAR
         */
        public static final String COLUMN_SIZE_NAME = "SizeName";
    }
}

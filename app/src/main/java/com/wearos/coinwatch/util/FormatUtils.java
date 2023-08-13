package com.wearos.coinwatch.util;

import com.wearos.coinwatch.data.model.Currency;
import com.wearos.coinwatch.data.model.SignedNumber;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

public class FormatUtils {

    public static SignedNumber convertToSignedNumber(double percentChange) {
        if (percentChange < 0) {
            return SignedNumber.NEGATIVE;
        } else if (percentChange > 0) {
            return SignedNumber.POSITIVE;
        } else {
            return SignedNumber.ZERO;
        }
    }

    public static String formatPercentChange(SignedNumber signedNumber, double percentChange) {
        DecimalFormat dFormat = new DecimalFormat("#.##");
        String s = dFormat.format(percentChange) + "%";
        if (signedNumber == SignedNumber.POSITIVE) {
            s = "+" + s;
        }
        return s;
    }

    public static String formatPrice(Currency type, double currentPrice) {
        String s = priceToDecimalString(currentPrice);
        switch (type) {
            case ARS:
                return type.getCurrencyName() + " " + s;
            case AUD:
                return "A" + type.getSymbol() + s;
            case BDT:
                return s + type.getCurrencyName();
            case BRL:
                return "R" + type.getSymbol() + s;
            case CAD:
                return "CA" + type.getSymbol() + s;
            case CLP:
                return "CLP " + s;
            case CNY:
                return "CN" + type.getSymbol() + s;
            case CZK:
                return "CZK " + s;
            case DKK:
                return "DKK " + s;
            case EURO:
                return type.getSymbol() + s;
            case GBP:
                return type.getSymbol() + s;
            case HKD:
                return "HK" + type.getSymbol() + s;
            case KRW:
                return type.getSymbol() + s;
            case MXN:
                return "MX" + type.getSymbol() + s;
            case MYR:
                return type.getSymbol() + s;
            case NOK:
                return type.getCurrencyName() + " " + s;
            case NZD:
                return "NZ" + type.getSymbol() + s;
            case PHP:
                return type.getSymbol() + s;
            case PLN:
                return type.getCurrencyName() + " " + s;
            case RUB:
                return type.getCurrencyName() + " " + s;
            case INR:
                return type.getSymbol() + s;
            case SEK:
                return type.getCurrencyName() + " " + s;
            case SGD:
                return "S" + type.getSymbol() + s;
            case THB:
                return type.getCurrencyName() + " " + s;
            case TRY:
                return type.getCurrencyName() + " " + s;
            case TWD:
                return "NT" + type.getSymbol() + s;
            case UAH:
                return s + " " + type.getCurrencyName();
            case USD:
                return type.getSymbol() + s;
            case VND:
                return s + " " + type.getCurrencyName();
            case JPY:
                return type.getSymbol() + s;

            default:
                throw new IllegalArgumentException("Invalid currency type");
        }
    }

    private static String priceToDecimalString(double currentPrice) {
        String s;
        if (currentPrice >= 1.00) {
            s = String.format(Locale.ENGLISH, "%,.2f", currentPrice);
        } else if (currentPrice < 1.00 && currentPrice >= 0.01) {
            DecimalFormat df = new DecimalFormat("0.#####");
            s = df.format(currentPrice);
        } else {
            DecimalFormat df = new DecimalFormat("0.########");
            s = df.format(currentPrice);
        }
        return s;
    }

    //Creates price for coins less than 0.01 in price
    private static String calculateSmallPriceValue(double currentPrice) {
        String price = String.valueOf(currentPrice);
        int positionOfDecimalPoint = price.indexOf(".");
        String substring = price.substring(positionOfDecimalPoint + 1);
        int numOfLeadingZeros = countLeadingZeros(substring);

        //Adds 2 more digits to the right so if it's 0.001, scale will format price to 0.0010
        int scale = numOfLeadingZeros + 2;
        BigDecimal formattedPrice = new BigDecimal(price);
        return String.valueOf(formattedPrice.setScale(scale, RoundingMode.UP));
    }

    private static int countLeadingZeros(String value) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '0') {
                count++;
            } else {
                // Stop counting when a non-zero character is encountered
                break;
            }
        }
        return count;
    }
}

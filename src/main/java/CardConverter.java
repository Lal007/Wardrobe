public class CardConverter {

//    public static void main(String[] args) {
//        System.out.println(CardConverter.getPrefixDec("83BBA5"));
//        System.out.println(CardConverter.getCardCode("83BBA5"));
//    }

    public static int getPrefixDec(String card){
        String prefix = card.substring(4, 6);
        return Integer.parseInt(prefix, 16);
    }

    public static int getCardCode(String card){
        String codeStart = card.substring(2, 4);
        String codeEnd = card.substring(0, 2);
        String cardCode = codeStart + codeEnd;
        return Integer.parseInt(cardCode, 16);
    }
}

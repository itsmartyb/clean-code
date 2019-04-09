package refactoring;

public class GildedRose {

    public static final String AGED_BRIE = "Aged Brie";
    public static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    public static final String BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert";
    public static final String CONJURED = "Conjured Mana Cake";
    public static final int LOW_LIMIT_DAY_FOR_QUALITY = 5;
    public static final int UP_LIMIT_DAY_FOR_QUALITY = 10;
    public static final int CHANGING_BY_TWO = 2;
    public static final int CHANGING_BY_THREE = 3;
    public static final int MAX_QUALITY = 50;


    Item[] items;

    public GildedRose(Item[] items) {

        this.items = items;

    }

    private boolean validateQuality(Item item) {
        return item.quality >= 0 && item.quality <= MAX_QUALITY;
    }

    private void decreaseSellIn(Item item) {
        --item.sellIn;
    }

    private void decreaseQuality(Item item) {
        if(item.sellIn < 0) {
            item.quality -= CHANGING_BY_TWO;
        } else {
            --item.quality;
        }
        roundQuality(item);
    }

    private void decreaseConjuredQuality(Item item){
        if(validateQuality(item)) {
            item.quality -= CHANGING_BY_TWO;
            roundQuality(item);
        }
    }

    private void increaseQuality(Item item, int value) {
        item.quality += value;
        roundQuality(item);
    }

    private void roundQuality(Item item) {
        if (item.quality > MAX_QUALITY) {
            item.quality = MAX_QUALITY;
        }
        if(item.quality < 0){
            item.quality = 0;
        }
    }

    private void updateAgedBrie(Item item) {
        if (item.sellIn > 0){
            ++item.quality;
        } else {
            increaseQuality(item, CHANGING_BY_TWO);
        }
        decreaseSellIn(item);
        roundQuality(item);
    }

    private void updateBackstage(Item item) {
        if (item.sellIn <= 0) {
            item.quality = 0;
            decreaseSellIn(item);
            return;
        }
        if(item.sellIn > UP_LIMIT_DAY_FOR_QUALITY){
            ++item.quality;
        }
        if (item.sellIn > LOW_LIMIT_DAY_FOR_QUALITY && item.sellIn <= UP_LIMIT_DAY_FOR_QUALITY) {
            increaseQuality(item, CHANGING_BY_TWO);
        }
        if (item.sellIn <= LOW_LIMIT_DAY_FOR_QUALITY) {
            increaseQuality(item, CHANGING_BY_THREE);
        }
        decreaseSellIn(item);
        roundQuality(item);
    }

    public void updateQuality() {

        for (int index = 0; index < items.length; index++) {

            switch (items[index].name) {
                case AGED_BRIE:
                    updateAgedBrie(items[index]);
                    break;
                case SULFURAS:
                    break;
                case BACKSTAGE_PASSES:
                    updateBackstage(items[index]);
                    break;
                case CONJURED:
                    decreaseConjuredQuality(items[index]);
                    decreaseSellIn(items[index]);
                    break;
                default:
                    decreaseQuality(items[index]);
                    decreaseSellIn(items[index]);
                    break;
            }
        }
    }


    public static void main(String[] args){

        System.out.println("OMGHAI!");


        Item[] items = new Item[]{

                new Item("+5 Dexterity Vest", 10, 20), //

                new Item("Aged Brie", 2, 0), //

                new Item("Elixir of the Mongoose", 5, 7), //

                new Item("Sulfuras, Hand of Ragnaros", 0, 80), //

                new Item("Sulfuras, Hand of Ragnaros", -1, 80),

                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),

                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),

                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),

                // this conjured item does not work properly yet

                new Item("Conjured Mana Cake", 3, 6)};


            GildedRose app = new GildedRose(items);


            int days = 10;

            if (args.length > 0) {

                days = Integer.parseInt(args[0]) + 1;

            }


            for (int day = 0; day < days; day++) {

                System.out.println("-------- day " + day + " --------");

                System.out.println("name, sellIn, quality");

                for (Item item : items) {

                    System.out.println(item);

                }

                System.out.println();

                app.updateQuality();

            }
        }
    }
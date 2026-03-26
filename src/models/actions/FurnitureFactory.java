package models.actions;

import java.util.Map;

import models.need.NeedType;
import models.skill.SkillType;

/**
 * Factory class used to create different types of {@link Furniture} with their associated actions.
 * <p>
 * Provides static methods to easily instantiate and configure furniture items, such as beds, stoves,
 * showers, and entertainment devices, along with the {@link FurnitureAction}s a Sim can perform on them.
 */
public class FurnitureFactory {

    /**
     * Creates a Tier 1 Cheap Mattress.
     *
     * @return a {@link Furniture} instance representing a cheap mattress
     */
    public static Furniture createCheapMattress() {
        Furniture cheapMattress = new Furniture("Cheap Mattress", "Tier 1 - A simple mattress for basic rest", 50.0);
        cheapMattress.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover some energy.",
                Map.of(NeedType.ENERGY, 15.0, NeedType.HUNGER, -5.0),
                Map.of(),
                0.0,
                1.5));
        cheapMattress.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of(NeedType.ENERGY, 70.0, NeedType.HUNGER, -25.0),
                Map.of(),
                0.0,
                8.0));
        return cheapMattress;
    }

    /**
     * Creates a Tier 2 Single Bed.
     *
     * @return a {@link Furniture} instance representing a single bed
     */
    public static Furniture createSingleBed() {
        Furniture singleBed = new Furniture("Single Bed", "Tier 2 - A comfortable single bed for a good night's sleep", 150.0);
        singleBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of(NeedType.ENERGY, 20.0, NeedType.HUNGER, -5.0),
                Map.of(),
                0.0,
                1.5));
        singleBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of(NeedType.ENERGY, 78.0, NeedType.HUNGER, -22.0),
                Map.of(),
                0.0,
                8.0));
        return singleBed;
    }

    /**
     * Creates a Tier 3 Double Bed.
     *
     * @return a {@link Furniture} instance representing a double bed
     */
    public static Furniture createDoubleBed() {
        Furniture doubleBed = new Furniture("Double Bed",
                "Tier 3 - A luxurious double bed for the better sleep experience", 250.0);
        doubleBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of(NeedType.ENERGY, 25.0, NeedType.HUNGER, -5.0),
                Map.of(),
                0.0,
                1.5));
        doubleBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of(NeedType.ENERGY, 85.0, NeedType.HUNGER, -20.0),
                Map.of(),
                0.0,
                8.0));
        return doubleBed;
    }

    /**
     * Creates a Tier 4 Queen Bed.
     *
     * @return a {@link Furniture} instance representing a queen bed
     */
    public static Furniture createQueenBed() {
        Furniture queenBed = new Furniture("Queen Bed",
                "Tier 4 - A spacious queen bed for the ultimate sleep experience", 350.0);
        queenBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of(NeedType.ENERGY, 30.0, NeedType.HUNGER, -4.0),
                Map.of(),
                0.0,
                1.5));
        queenBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of(NeedType.ENERGY, 92.0, NeedType.HUNGER, -17.0),
                Map.of(),
                0.0,
                8.0));
        return queenBed;
    }

    /**
     * Creates a Tier 5 King Bed.
     *
     * @return a {@link Furniture} instance representing a king bed
     */
    public static Furniture createKingBed() {
        Furniture kingBed = new Furniture("King Bed", "Tier 5 - The ultimate king bed for the best sleep experience", 450.0);
        kingBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of(NeedType.ENERGY, 35.0, NeedType.HUNGER, -3.0),
                Map.of(),
                0.0,
                1.5));
        kingBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of(NeedType.ENERGY, 100.0, NeedType.HUNGER, -14.0),
                Map.of(),
                0.0,
                8.0));
        return kingBed;
    }

    /**
     * Creates a Tier 1 Single Hotplate.
     *
     * @return a {@link Furniture} instance representing a single hotplate
     */
    public static Furniture createSingleHotplate() {
        Furniture hotplate = new Furniture("Single Hotplate", "Tier 1 - A simple hotplate for cooking", 50.0);
        hotplate.addAction(new FurnitureAction(
                "Cook Instant Noodles",
                "Cook a sad bowl of instant noodles.",
                Map.of(NeedType.HUNGER, 30.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.COOKING, 8.0),
                3.0,
                0.25));
        hotplate.addAction(new FurnitureAction(
                "Boil Eggs",
                "Boil some eggs for a quick snack.",
                Map.of(NeedType.HUNGER, 15.0, NeedType.ENERGY, -3.0),
                Map.of(SkillType.COOKING, 5.0),
                2.0,
                0.25));
        return hotplate;
    }

    /**
     * Creates a Tier 2 Old Stove.
     *
     * @return a {@link Furniture} instance representing an old stove
     */
    public static Furniture createOldStove() {
        Furniture oldStove = new Furniture("Old Stove", "Tier 2 - An old stove for cooking", 100.0);
        oldStove.addAction(new FurnitureAction(
                "Cook Nissin Cup Noodles",
                "Prepare a nice cup of Nissin noodles.",
                Map.of(NeedType.HUNGER, 25.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.COOKING, 10.0),
                3.0,
                0.25));
        oldStove.addAction(new FurnitureAction(
                "Cook Spaghetti",
                "Cook a proper pasta dish.",
                Map.of(NeedType.HUNGER, 45.0, NeedType.ENERGY, -8.0),
                Map.of(SkillType.COOKING, 20.0),
                8.0,
                0.75));
        return oldStove;
    }

    /**
     * Creates a Tier 3 Modern Stove.
     *
     * @return a {@link Furniture} instance representing a modern stove
     */
    public static Furniture createModernStove() {
        Furniture modernStove = new Furniture("Modern Stove", "Tier 3 - A modern stove for better cooking", 200.0);
        modernStove.addAction(new FurnitureAction(
                "Cook Ramen",
                "Cook a delicious bowl of ramen.",
                Map.of(NeedType.HUNGER, 35.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.COOKING, 15.0),
                8.0,
                0.75));
        modernStove.addAction(new FurnitureAction(
                "Cook Fried Rice",
                "Cook a delicious plate of fried rice.",
                Map.of(NeedType.HUNGER, 50.0, NeedType.ENERGY, -10.0),
                Map.of(SkillType.COOKING, 25.0),
                12.0,
                0.75));
        modernStove.addAction(new FurnitureAction(
                "Cook Steak",
                "Cook a juicy steak to perfection.",
                Map.of(NeedType.HUNGER, 60.0, NeedType.ENERGY, -15.0),
                Map.of(SkillType.COOKING, 30.0),
                20.0,
                1.0));
        return modernStove;
    }

    /**
     * Creates a Tier 4 Gourmet Stove.
     *
     * @return a {@link Furniture} instance representing a gourmet stove
     */
    public static Furniture createGourmetStove() {
        Furniture gourmetStove = new Furniture("Gourmet Stove",
                "Tier 4 - A gourmet stove for the best cooking experience", 300.0);
        gourmetStove.addAction(new FurnitureAction(
                "Cook Gourmet Ramen",
                "Cook a bowl of gourmet ramen with premium ingredients.",
                Map.of(NeedType.HUNGER, 45.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.COOKING, 20.0),
                15.0,
                1.0));
        gourmetStove.addAction(new FurnitureAction(
                "Cook Paella",
                "Cook a flavorful and colorful paella.",
                Map.of(NeedType.HUNGER, 60.0, NeedType.ENERGY, -10.0),
                Map.of(SkillType.COOKING, 30.0),
                22.0,
                1.5));
        gourmetStove.addAction(new FurnitureAction(
                "Cook Beef Wellington",
                "Cook a classic beef wellington to impress your guests.",
                Map.of(NeedType.HUNGER, 70.0, NeedType.ENERGY, -15.0),
                Map.of(SkillType.COOKING, 40.0),
                35.0,
                2.0));
        return gourmetStove;
    }

    /**
     * Creates a Tier 1 Old Shower.
     *
     * @return a {@link Furniture} instance representing an old shower
     */
    public static Furniture createOldShower() {
        Furniture oldShower = new Furniture("Old Shower", "Tier 1 - A basic shower for cleaning up", 50.0);
        oldShower.addAction(new FurnitureAction(
                "Take Quick Shower",
                "A quick shower to freshen up.",
                Map.of(NeedType.HYGIENE, 45.0, NeedType.ENERGY, -3.0),
                Map.of(),
                0.0,
                0.25));
        oldShower.addAction(new FurnitureAction(
                "Take Long Shower",
                "A long shower for better hygiene.",
                Map.of(NeedType.HYGIENE, 65.0, NeedType.ENERGY, -5.0),
                Map.of(),
                0.0,
                0.5));
        return oldShower;
    }

    /**
     * Creates a Tier 2 Normal Shower.
     *
     * @return a {@link Furniture} instance representing a normal shower
     */
    public static Furniture createNormalShower() {
        Furniture normalShower = new Furniture("Normal Shower", "Tier 2 - A normal shower for better cleaning", 100.0);
        normalShower.addAction(new FurnitureAction(
                "Take Quick Shower",
                "A quick shower to freshen up.",
                Map.of(NeedType.HYGIENE, 55.0, NeedType.ENERGY, -3.0),
                Map.of(),
                0.0,
                0.25));
        normalShower.addAction(new FurnitureAction(
                "Take Long Shower",
                "A long shower for better hygiene.",
                Map.of(NeedType.HYGIENE, 75.0, NeedType.ENERGY, -5.0),
                Map.of(),
                0.0,
                0.5));
        return normalShower;
    }

    /**
     * Creates a Tier 3 Luxury Bathtub.
     *
     * @return a {@link Furniture} instance representing a luxury bathtub
     */
    public static Furniture createLuxuryBathtub() {
        Furniture luxuryBathtub = new Furniture("Luxury Bathtub",
                "Tier 3 - A luxurious bathtub for the ultimate relaxation", 250.0);
        luxuryBathtub.addAction(new FurnitureAction(
                "Take Quick Shower",
                "A quick shower to freshen up.",
                Map.of(NeedType.HYGIENE, 55.0, NeedType.ENERGY, -3.0),
                Map.of(),
                0.0,
                0.25));
        luxuryBathtub.addAction(new FurnitureAction(
                "Take Long Shower",
                "A long shower for better hygiene.",
                Map.of(NeedType.HYGIENE, 75.0, NeedType.ENERGY, -5.0),
                Map.of(),
                0.0,
                0.5));
        luxuryBathtub.addAction(new FurnitureAction(
                "Take Aromatherapy Bath",
                "Enjoy an aromatherapy bath for maximum relaxation and hygiene.",
                Map.of(NeedType.HYGIENE, 90.0, NeedType.ENERGY, 10.0, NeedType.FUN, 40.0),
                Map.of(),
                0.0,
                1.0));
        return luxuryBathtub;
    }

    /**
     * Creates a Toilet.
     *
     * @return a {@link Furniture} instance representing a toilet
     */
    public static Furniture createToilet() {
        Furniture toilet = new Furniture("Toilet", "Tier 1 - A basic toilet for your needs", 50.0);
        toilet.addAction(new FurnitureAction(
                "Use Toilet",
                "Take care of your business and wash up afterwards.",
                Map.of(NeedType.HYGIENE, 5.0),
                Map.of(),
                0.0,
                0.1));
        return toilet;
    }

    /**
     * Creates a Tier 1 Old CRT TV.
     *
     * @return a {@link Furniture} instance representing an old CRT TV
     */
    public static Furniture createOldCRTTV() {
        Furniture crtTV = new Furniture("Old CRT TV", "Tier 1 - An old CRT TV for basic entertainment", 100.0);
        crtTV.addAction(new FurnitureAction(
                "Watch TV",
                "Watch some TV to relax and improve mood.",
                Map.of(NeedType.FUN, 20.0, NeedType.ENERGY, -5.0),
                Map.of(),
                0.0,
                1.0));
        return crtTV;
    }

    /**
     * Creates a Tier 2 Modern LCD TV.
     *
     * @return a {@link Furniture} instance representing a modern LCD TV
     */
    public static Furniture createModernLCDTV() {
        Furniture lcdTV = new Furniture("Modern LCD TV", "Tier 2 - A modern LCD TV for better entertainment", 200.0);
        lcdTV.addAction(new FurnitureAction(
                "Watch TV",
                "Watch some TV to relax and improve mood.",
                Map.of(NeedType.FUN, 30.0, NeedType.ENERGY, -5.0),
                Map.of(),
                0.0,
                1.0));
        return lcdTV;
    }

    /**
     * Creates a Tier 3 OLED TV.
     *
     * @return a {@link Furniture} instance representing an OLED TV
     */
    public static Furniture createOLEDTV() {
        Furniture oledTV = new Furniture("OLED TV",
                "Tier 3 - A high-end OLED TV for the best entertainment experience", 300.0);
        oledTV.addAction(new FurnitureAction(
                "Watch TV",
                "Watch some TV to relax and improve mood.",
                Map.of(NeedType.FUN, 40.0, NeedType.ENERGY, -5.0),
                Map.of(),
                0.0,
                1.0));
        return oledTV;
    }

    /**
     * Creates a Restaurant Table.
     *
     * @return a {@link Furniture} instance representing a restaurant table
     */
    public static Furniture createRestaurantTable() {
        Furniture restaurantTable = new Furniture("Restaurant Table",
                "A fancy restaurant table for the best dining experience", 150.0);
        restaurantTable.addAction(new FurnitureAction(
                "Eat Nice Meal",
                "Enjoy a delicious meal at the restaurant table.",
                Map.of(NeedType.HUNGER, 60.0, NeedType.ENERGY, 10.0, NeedType.FUN, 25.0),
                Map.of(SkillType.CHARISMA, 5.0),
                30.0,
                1.0));
        restaurantTable.addAction(new FurnitureAction(
                "Drink Nice Wine",
                "Drink a nice glass of wine.",
                Map.of(NeedType.FUN, 40.0),
                Map.of(SkillType.CHARISMA, 5.0),
                25.0,
                0.25));
        restaurantTable.addAction(new FurnitureAction(
                "Have Dessert",
                "Indulge in a delicious dessert.",
                Map.of(NeedType.HUNGER, 25.0, NeedType.FUN, 30.0),
                Map.of(),
                15.0,
                0.25));
        return restaurantTable;
    }

    /**
     * Creates a Treadmill.
     *
     * @return a {@link Furniture} instance representing a treadmill
     */
    public static Furniture createTreadmill() {
        Furniture treadmill = new Furniture("Treadmill", "A treadmill for staying fit and healthy", 200.0);
        treadmill.addAction(new FurnitureAction(
                "Run on Treadmill",
                "Get some exercise by running on the treadmill.",
                Map.of(NeedType.ENERGY, -25.0, NeedType.HUNGER, -10.0, NeedType.FUN, 15.0, NeedType.HYGIENE, -25.0),
                Map.of(SkillType.FITNESS, 20.0),
                5.0,
                1.0));
        treadmill.addAction(new FurnitureAction(
                "Walk on Treadmill",
                "Take a leisurely walk on the treadmill for light exercise.",
                Map.of(NeedType.ENERGY, -10.0, NeedType.HUNGER, -5.0, NeedType.FUN, 10.0, NeedType.HYGIENE, -10.0),
                Map.of(SkillType.FITNESS, 8.0),
                5.0,
                1.0));
        return treadmill;
    }

    /**
     * Creates a set of Dumbbells.
     *
     * @return a {@link Furniture} instance representing dumbbells
     */
    public static Furniture createDumbbells() {
        Furniture dumbbells = new Furniture("Dumbbells", "A set of dumbbells for strength training", 150.0);
        dumbbells.addAction(new FurnitureAction(
                "Lift Dumbbells",
                "Strengthen your muscles by lifting dumbbells.",
                Map.of(NeedType.ENERGY, -20.0, NeedType.HUNGER, -10.0, NeedType.FUN, 10.0, NeedType.HYGIENE, -20.0),
                Map.of(SkillType.FITNESS, 25.0),
                5.0,
                1.0));
        return dumbbells;
    }

    /**
     * Creates a Vending Machine.
     *
     * @return a {@link Furniture} instance representing a vending machine
     */
    public static Furniture createVendingMachine() {
        Furniture vendingMachine = new Furniture("Vending Machine", "A vending machine for quick snacks and drinks", 100.0);
        vendingMachine.addAction(new FurnitureAction(
                "Buy Snack",
                "Buy a quick snack from the vending machine.",
                Map.of(NeedType.HUNGER, 15.0, NeedType.ENERGY, -2.0, NeedType.FUN, 5.0),
                Map.of(),
                3.0,
                0.25));
        vendingMachine.addAction(new FurnitureAction(
                "Buy Drink",
                "Buy a refreshing drink from the vending machine.",
                Map.of(NeedType.HUNGER, 5.0, NeedType.ENERGY, 5.0, NeedType.FUN, 5.0),
                Map.of(),
                3.0,
                0.25));
        return vendingMachine;
    }

    /**
     * Creates a Park Path.
     *
     * @return a {@link Furniture} instance representing a park path
     */
    public static Furniture createParkPath() {
        Furniture parkPath = new Furniture("Park Path", "A scenic park path for walking and enjoying nature", 0.0);
        parkPath.addAction(new FurnitureAction(
                "Take a Walk",
                "Take a leisurely walk along the park path to enjoy the scenery.",
                Map.of(NeedType.ENERGY, -8.0, NeedType.HUNGER, -5.0, NeedType.FUN, 25.0, NeedType.HYGIENE, -5.0),
                Map.of(SkillType.FITNESS, 5.0),
                0.0,
                1.0));
        parkPath.addAction(new FurnitureAction(
                "Go for a Jog",
                "Go for a jog along the park path to get some exercise.",
                Map.of(NeedType.ENERGY, -22.0, NeedType.HUNGER, -10.0, NeedType.FUN, 20.0, NeedType.HYGIENE, -20.0),
                Map.of(SkillType.FITNESS, 18.0),
                0.0,
                1.0));
        return parkPath;
    }

    /**
     * Creates a Park Lake.
     *
     * @return a {@link Furniture} instance representing a park lake
     */
    public static Furniture createParkLake() {
        Furniture parkLake = new Furniture("Park Lake", "A peaceful lake you can swim in", 0.0);
        parkLake.addAction(new FurnitureAction(
                "Go for a Swim",
                "Take a refreshing swim in the park lake.",
                Map.of(NeedType.ENERGY, -20.0, NeedType.HUNGER, -10.0, NeedType.FUN, 35.0, NeedType.HYGIENE, -20.0),
                Map.of(SkillType.FITNESS, 20.0),
                0.0,
                1.0));
        return parkLake;
    }

    /**
     * Creates a Bicycle.
     *
     * @return a {@link Furniture} instance representing a bicycle
     */
    public static Furniture createBicycle() {
        Furniture bicycle = new Furniture("Bicycle", "A bicycle for transportation and exercise", 100.0);
        bicycle.addAction(new FurnitureAction(
                "Go for a Bike Ride",
                "Take a bike ride around the neighborhood for fun and exercise.",
                Map.of(NeedType.ENERGY, -18.0, NeedType.HUNGER, -8.0, NeedType.FUN, 30.0, NeedType.HYGIENE, -15.0),
                Map.of(SkillType.FITNESS, 15.0),
                0.0,
                1.0));
        return bicycle;
    }

    /**
     * Creates a Picnic Table.
     *
     * @return a {@link Furniture} instance representing a picnic table
     */
    public static Furniture createPicnicTable() {
        Furniture picnicTable = new Furniture("Picnic Table", "A picnic table for outdoor dining and relaxation", 50.0);
        picnicTable.addAction(new FurnitureAction(
                "Have a Picnic",
                "Enjoy a picnic at the table with some delicious food.",
                Map.of(NeedType.HUNGER, 45.0, NeedType.ENERGY, 15.0, NeedType.FUN, 35.0),
                Map.of(),
                15.0,
                1.0));
        picnicTable.addAction(new FurnitureAction(
                "Eat Muffin",
                "Eat a muffin",
                Map.of(NeedType.HUNGER, 20.0, NeedType.FUN, 5.0),
                Map.of(),
                4.0,
                0.1));
        return picnicTable;
    }

    /**
     * Creates a Cafe Table.
     *
     * @return a {@link Furniture} instance representing a cafe table
     */
    public static Furniture createCafeTable() {
        Furniture cafeTable = new Furniture("Cafe Table", "A cozy cafe table for enjoying coffee and snacks", 75.0);
        cafeTable.addAction(new FurnitureAction(
                "Drink Coffee",
                "Enjoy a cup of coffee at the cafe table.",
                Map.of(NeedType.ENERGY, 25.0, NeedType.FUN, 15.0),
                Map.of(),
                6.0,
                0.25));
        cafeTable.addAction(new FurnitureAction(
                "Eat Pastry",
                "Indulge in a delicious pastry at the cafe table.",
                Map.of(NeedType.HUNGER, 30.0, NeedType.FUN, 20.0),
                Map.of(),
                8.0,
                0.25));
        return cafeTable;
    }

    /**
     * Creates an Espresso Machine.
     *
     * @return a {@link Furniture} instance representing an espresso machine
     */
    public static Furniture createEspressoMachine() {
        Furniture espressoMachine = new Furniture("Espresso Machine",
                "A high-end espresso machine for the best coffee experience", 200.0);
        espressoMachine.addAction(new FurnitureAction(
                "Make Espresso",
                "Brew a strong and delicious espresso shot.",
                Map.of(NeedType.ENERGY, 30.0, NeedType.FUN, 10.0),
                Map.of(),
                4.0,
                0.25));
        espressoMachine.addAction(new FurnitureAction(
                "Make Cappuccino",
                "Create a creamy cappuccino with steamed milk.",
                Map.of(NeedType.ENERGY, 22.0, NeedType.FUN, 20.0),
                Map.of(),
                5.0,
                0.25));
        return espressoMachine;
    }

    /**
     * Creates a Jukebox.
     *
     * @return a {@link Furniture} instance representing a jukebox
     */
    public static Furniture createJukeBox() {
        Furniture jukeBox = new Furniture("Jukebox", "A classic jukebox for playing music and improving mood", 150.0);
        jukeBox.addAction(new FurnitureAction(
                "Play Music",
                "Select a song to play on the jukebox and enjoy the music.",
                Map.of(NeedType.FUN, 30.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.MUSIC, 10.0, SkillType.CREATIVITY, 5.0),
                2.0,
                0.5));
        return jukeBox;
    }

    /**
     * Creates a Bookshelf.
     *
     * @return a {@link Furniture} instance representing a bookshelf
     */
    public static Furniture createBookshelf() {
        Furniture bookshelf = new Furniture("Bookshelf", "A bookshelf filled with books for reading and learning", 100.0);
        bookshelf.addAction(new FurnitureAction(
                "Read Book",
                "Pick a book from the shelf and read to improve knowledge and have fun.",
                Map.of(NeedType.FUN, 20.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.LOGIC, 12.0, SkillType.WRITING, 8.0),
                0.0,
                1.0));
        return bookshelf;
    }

    /**
     * Creates a Computer Desk.
     *
     * @return a {@link Furniture} instance representing a computer desk
     */
    public static Furniture createComputerDesk() {
        Furniture computerDesk = new Furniture("Computer Desk", "A computer desk with a PC for gaming and work", 200.0);
        computerDesk.addAction(new FurnitureAction(
                "Play Video Games",
                "Play some video games on the computer to have fun and relax.",
                Map.of(NeedType.FUN, 35.0, NeedType.ENERGY, -8.0),
                Map.of(SkillType.LOGIC, 5.0),
                0.0,
                1.0));
        computerDesk.addAction(new FurnitureAction(
                "Work on Computer",
                "Use the computer for work or study to improve skills and earn money.",
                Map.of(NeedType.ENERGY, -10.0, NeedType.FUN, -5.0),
                Map.of(SkillType.PROGRAMMING, 15.0, SkillType.LOGIC, 8.0),
                0.0,
                2.0));
        return computerDesk;
    }

    /**
     * Creates a Bar.
     *
     * @return a {@link Furniture} instance representing a bar
     */
    public static Furniture createBar() {
        Furniture bar = new Furniture("Bar", "A bar for socializing and enjoying drinks", 150.0);
        bar.addAction(new FurnitureAction(
                "Have a Drink",
                "Enjoy a drink at the bar to relax and have fun.",
                Map.of(NeedType.FUN, 35.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.CHARISMA, 8.0),
                12.0,
                1.0));
        return bar;
    }

    /**
     * Creates a Dance Floor.
     *
     * @return a {@link Furniture} instance representing a dance floor
     */
    public static Furniture createDanceFloor() {
        Furniture danceFloor = new Furniture("Dance Floor", "A dance floor for dancing and having fun", 200.0);
        danceFloor.addAction(new FurnitureAction(
                "Dance",
                "Dance on the dance floor to have fun and improve fitness.",
                Map.of(NeedType.FUN, 50.0, NeedType.ENERGY, -25.0, NeedType.HYGIENE, -30.0),
                Map.of(SkillType.FITNESS, 20.0, SkillType.CHARISMA, 10.0),
                0.0,
                1.0));
        return danceFloor;
    }

    /**
     * Creates a Work Desk.
     *
     * @return a {@link Furniture} instance representing a work desk
     */
    public static Furniture createWorkDesk() {
        Furniture workDesk = new Furniture("Work Desk", "Your workstation for the day.", 150.0);
        workDesk.addAction(new FurnitureAction(
                "Work",
                "Put in a full day's work.",
                Map.of(
                        NeedType.HUNGER, -40.0,
                        NeedType.ENERGY, -40.0,
                        NeedType.HYGIENE, -20.0,
                        NeedType.FUN, -25.0,
                        NeedType.SOCIAL, 20.0),
                Map.of(),
                0.0,
                8.0));
        return workDesk;
    }
}
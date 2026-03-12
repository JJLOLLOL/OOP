package models.furnitureactions;

import java.util.Map;

// Factory class to create different types of furniture with their associated actions
// Code to create furniture in Main.java or in some initialization class:
/*
Furniture bed = FurnitureFactory.createBed();
Furniture hotplate = FurnitureFactory.createSingleHotplate();
 */
// To have Sim perform an action on a piece of furniture:
/*
Furniture bed = FurnitureFactory.createBed();
bed.performAction("Sleep", sim);
 */
// To store multiple pieces of furniture in a map for easy access in Location:
/* 
Map<String, Furniture> furniture = new HashMap<>();
furniture.put("Bed", FurnitureFactory.createBed());
furniture.put("Oven", FurnitureFactory.createOven());

// Then later:
furniture.get("Bed").performAction("Nap", sim);
 */
public class FurnitureFactory {

    public static Furniture createCheapMattress() {
        Furniture cheapMattress = new Furniture("Cheap Mattress", "Tier 1 - A simple mattress for basic rest");
        cheapMattress.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of("Energy", 10.0, "Hunger", -10.0), // Need changes
                Map.of(), // Skill changes
                0.0, // Money cost
                2.0)); // Time required in hours
        cheapMattress.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of("Energy", 80.0, "Hunger", -40.0),
                Map.of(),
                0.0,
                8.0));
        return cheapMattress;
    }

    public static Furniture createSingleBed() {
        Furniture singleBed = new Furniture("Single Bed", "Tier 2 - A comfortable single bed for a good night's sleep");
        singleBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of("Energy", 15.0, "Hunger", -10.0),
                Map.of(),
                0.0,
                2.0));
        singleBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of("Energy", 85.0, "Hunger", -40.0),
                Map.of(),
                0.0,
                8.0));
        return singleBed;
    }

    public static Furniture createDoubleBed() {
        Furniture doubleBed = new Furniture("Double Bed", "Tier 3 - A luxurious double bed for the better sleep experience");
        doubleBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of("Energy", 20.0, "Hunger", -10.0),
                Map.of(),
                0.0,
                2.0));
        doubleBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of("Energy", 90.0, "Hunger", -35.0),
                Map.of(),
                0.0,
                8.0));
        return doubleBed;
    }

    public static Furniture createQueenBed() {
        Furniture queenBed = new Furniture("Queen Bed", "Tier 4 - A spacious queen bed for the ultimate sleep experience");
        queenBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of("Energy", 25.0, "Hunger", -5.0),
                Map.of(),
                0.0,
                2.0));
        queenBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of("Energy", 100.0, "Hunger", -30.0),
                Map.of(),
                0.0,
                8.0));
        return queenBed;
    }

    public static Furniture createKingBed() {
        Furniture kingBed = new Furniture("King Bed", "Tier 5 - The ultimate king bed for the best sleep experience");
        kingBed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of("Energy", 30.0, "Hunger", -5.0),
                Map.of(),
                0.0,
                2.0));
        kingBed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of("Energy", 100.0, "Hunger", -25.0),
                Map.of(),
                0.0,
                8.0));
        return kingBed;
    }

    public static Furniture createSingleHotplate() {
        Furniture hotplate = new Furniture("Single Hotplate", "Tier 1 - A simple hotplate for cooking");
        hotplate.addAction(new FurnitureAction(
                "Cook Instant Noodles",
                "Cook a sad bowl of instant noodles.",
                Map.of("Hunger", 30.0, "Energy", -5.0),
                Map.of("Cooking", 8.0),
                10.0,
                0.5));
        hotplate.addAction(new FurnitureAction(
                "Boil Eggs",
                "Boil some eggs for a quick snack.",
                Map.of("Hunger", 10.0, "Energy", -3.0),
                Map.of("Cooking", 5.0),
                5.0,
                0.5));
        return hotplate;
    }

    public static Furniture createOldStove() {
        Furniture oldStove = new Furniture("Old Stove", "Tier 2 - An old stove for cooking");
        oldStove.addAction(new FurnitureAction(
                "Cook Nissin Cup Noodles",
                "Prepare a nice cup of Nissin noodles.",
                Map.of("Hunger", 25.0, "Energy", -5.0),
                Map.of("Cooking", 10.0),
                5.0,
                0.5));
        oldStove.addAction(new FurnitureAction(
                "Cook Spaghetti",
                "Cook a proper pasta dish.",
                Map.of("Hunger", 35.0, "Energy", -8.0),
                Map.of("Cooking", 20.0),
                10.0,
                1.0));
        return oldStove;
    }

    public static Furniture createModernStove() {
        Furniture modernStove = new Furniture("Modern Stove", "Tier 3 - A modern stove for better cooking");
        modernStove.addAction(new FurnitureAction(
                "Cook Ramen",
                "Cook a delicious bowl of ramen.",
                Map.of("Hunger", 30.0, "Energy", -5.0),
                Map.of("Cooking", 15.0),
                10.0,
                0.5));
        modernStove.addAction(new FurnitureAction(
                "Cook Fried Rice",
                "Cook a delicious plate of fried rice.",
                Map.of("Hunger", 40.0, "Energy", -10.0),
                Map.of("Cooking", 25.0),
                15.0,
                1.0));
        modernStove.addAction(new FurnitureAction(
                "Cook Steak",
                "Cook a juicy steak to perfection.",
                Map.of("Hunger", 50.0, "Energy", -15.0),
                Map.of("Cooking", 30.0),
                20.0,
                1.5));
        return modernStove;
    }

    public static Furniture createGourmetStove() {
        Furniture gourmetStove = new Furniture("Gourmet Stove", "Tier 4 - A gourmet stove for the best cooking experience");
        gourmetStove.addAction(new FurnitureAction(
                "Cook Gourmet Ramen",
                "Cook a bowl of gourmet ramen with premium ingredients.",
                Map.of("Hunger", 40.0, "Energy", -5.0),
                Map.of("Cooking", 20.0),
                15.0,
                0.5));
        gourmetStove.addAction(new FurnitureAction(
                "Cook Paella",
                "Cook a flavorful and colorful paella.",
                Map.of("Hunger", 50.0, "Energy", -10.0),
                Map.of("Cooking", 30.0),
                25.0,
                1.0));
        gourmetStove.addAction(new FurnitureAction(
                "Cook Beef Wellington",
                "Cook a classic beef wellington to impress your guests.",
                Map.of("Hunger", 60.0, "Energy", -15.0),
                Map.of("Cooking", 40.0),
                30.0,
                1.5));
        return gourmetStove;
    }

    public static Furniture createOldShower() {
        Furniture oldShower = new Furniture("Old Shower", "Tier 1 - A basic shower for cleaning up");
        oldShower.addAction(new FurnitureAction(
                "Take Quick Shower",
                "A quick shower to freshen up.",
                Map.of("Hygiene", 30.0, "Energy", -5.0),
                Map.of(),
                0.0,
                0.5));
        oldShower.addAction(new FurnitureAction(
                "Take Long Shower",
                "A long shower for better hygiene.",
                Map.of("Hygiene", 50.0, "Energy", -10.0),
                Map.of(),
                0.0,
                1.0));
        return oldShower;
    }

    public static Furniture createNormalShower() {
        Furniture normalShower = new Furniture("Normal Shower", "Tier 2 - A normal shower for better cleaning");
        normalShower.addAction(new FurnitureAction(
                "Take Quick Shower",
                "A quick shower to freshen up.",
                Map.of("Hygiene", 40.0, "Energy", -5.0),
                Map.of(),
                0.0,
                0.5));
        normalShower.addAction(new FurnitureAction(
                "Take Long Shower",
                "A long shower for better hygiene.",
                Map.of("Hygiene", 70.0, "Energy", -10.0),
                Map.of(),
                0.0,
                1.0));
        return normalShower;
    }

    public static Furniture createLuxuryBathtub() {
        Furniture luxuryBathtub = new Furniture("Luxury Bathtub", "Tier 3 - A luxurious bathtub for the ultimate relaxation");
        luxuryBathtub.addAction(new FurnitureAction(
                "Take Quick Shower",
                "A quick shower to freshen up.",
                Map.of("Hygiene", 40.0, "Energy", -5.0),
                Map.of(),
                0.0,
                0.5));
        luxuryBathtub.addAction(new FurnitureAction(
                "Take Long Shower",
                "A long shower for better hygiene.",
                Map.of("Hygiene", 70.0, "Energy", -10.0),
                Map.of(),
                0.0,
                1.0));
        luxuryBathtub.addAction(new FurnitureAction(
                "Take Aromatherapy Bath",
                "Enjoy an aromatherapy bath for maximum relaxation and hygiene.",
                Map.of("Hygiene", 80.0, "Energy", -10.0, "Fun", 40.0),
                Map.of(),
                0.0,
                1.5));
        return luxuryBathtub;
    }

    public static Furniture createToilet() {
        Furniture toilet = new Furniture("Toilet", "Tier 1 - A basic toilet for your needs");
        toilet.addAction(new FurnitureAction(
                "Use Toilet",
                "Take care of your business to relieve bladder and bowels.",
                Map.of("Bladder", 50.0, "Hygiene", -5.0),
                Map.of(),
                0.0,
                0.25));
        return toilet;
    }

    public static Furniture createOldCRTTV() {
        Furniture crtTV = new Furniture("Old CRT TV", "Tier 1 - An old CRT TV for basic entertainment");
        crtTV.addAction(new FurnitureAction(
                "Watch TV",
                "Watch some TV to relax and improve mood.",
                Map.of("Fun", 20.0, "Energy", -5.0),
                Map.of(),
                0.0,
                1.0));
        return crtTV;
    }

    public static Furniture createModernLCDTV() {
        Furniture lcdTV = new Furniture("Modern LCD TV", "Tier 2 - A modern LCD TV for better entertainment");
        lcdTV.addAction(new FurnitureAction(
                "Watch TV",
                "Watch some TV to relax and improve mood.",
                Map.of("Fun", 30.0, "Energy", -5.0),
                Map.of(),
                0.0,
                1.0));
        return lcdTV;
    }

    public static Furniture createOLEDTV() {
        Furniture oledTV = new Furniture("OLED TV", "Tier 3 - A high-end OLED TV for the best entertainment experience");
        oledTV.addAction(new FurnitureAction(
                "Watch TV",
                "Watch some TV to relax and improve mood.",
                Map.of("Fun", 40.0, "Energy", -5.0),
                Map.of(),
                0.0,
                1.0));
        return oledTV;
    }

    public static Furniture createRestaurantTable() {
        Furniture restaurantTable = new Furniture("Restaurant Table", "A fancy restaurant table for the best dining experience");
        restaurantTable.addAction(new FurnitureAction(
                "Eat Nice Meal",
                "Enjoy a delicious meal at the restaurant table.",
                Map.of("Hunger", 50.0, "Energy", 20.0, "Fun", 20.0),
                Map.of(),
                30.0,
                1.0));
        restaurantTable.addAction(new FurnitureAction(
                "Drink Nice Wine",
                "Drink a nice glass of wine.",
                Map.of("Fun", 50.0, "Bladder", 30.0),
                Map.of(),
                30.0,
                0.5));
        restaurantTable.addAction(new FurnitureAction(
                "Have Dessert",
                "Indulge in a delicious dessert.",
                Map.of("Hunger", 30.0, "Fun", 30.0),
                Map.of(),
                20.0,
                0.5));
        return restaurantTable;
    }

    public static Furniture createTreadmill() {
        Furniture treadmill = new Furniture("Treadmill", "A treadmill for staying fit and healthy");
        treadmill.addAction(new FurnitureAction(
                "Run on Treadmill",
                "Get some exercise by running on the treadmill.",
                Map.of("Energy", -20.0, "Hunger", -10.0, "Fun", 20.0, "Hygiene", -30.0),
                Map.of("Fitness", 15.0),
                5.0,
                1.0));
        treadmill.addAction(new FurnitureAction(
                "Walk on Treadmill",
                "Take a leisurely walk on the treadmill for light exercise.",
                Map.of("Energy", -10.0, "Hunger", -5.0, "Fun", 10.0, "Hygiene", -15.0),
                Map.of("Fitness", 5.0),
                5.0,
                1.0));
        return treadmill;
    }

    public static Furniture createDumbbells() {
        Furniture dumbbells = new Furniture("Dumbbells", "A set of dumbbells for strength training");
        dumbbells.addAction(new FurnitureAction(
                "Lift Dumbbells",
                "Strengthen your muscles by lifting dumbbells.",
                Map.of("Energy", -15.0, "Hunger", -10.0, "Fun", 15.0, "Hygiene", -20.0),
                Map.of("Fitness", 20.0),
                5.0,
                1.0));
        return dumbbells;
    }

    public static Furniture createVendingMachine() {
        Furniture vendingMachine = new Furniture("Vending Machine", "A vending machine for quick snacks and drinks");
        vendingMachine.addAction(new FurnitureAction(
                "Buy Snack",
                "Buy a quick snack from the vending machine.",
                Map.of("Hunger", 20.0, "Energy", -5.0, "Fun", 10.0),
                Map.of(),
                2.0,
                0.25));
        vendingMachine.addAction(new FurnitureAction(
                "Buy Drink",
                "Buy a refreshing drink from the vending machine.",
                Map.of("Hunger", 10.0, "Energy", -5.0, "Fun", 10.0),
                Map.of(),
                2.0,
                0.25));
        return vendingMachine;
    }

    public static Furniture createParkPath() {
        Furniture parkPath = new Furniture("Park Path", "A scenic park path for walking and enjoying nature");
        parkPath.addAction(new FurnitureAction(
                "Take a Walk",
                "Take a leisurely walk along the park path to enjoy the scenery.",
                Map.of("Energy", -10.0, "Hunger", -5.0, "Fun", 20.0, "Hygiene", -10.0),
                Map.of("Fitness", 5.0),
                0.0,
                1.0));
        parkPath.addAction(new FurnitureAction(
                "Go for a Jog",
                "Go for a jog along the park path to get some exercise.",
                Map.of("Energy", -20.0, "Hunger", -10.0, "Fun", 30.0, "Hygiene", -20.0),
                Map.of("Fitness", 15.0),
                0.0,
                1.0));
        return parkPath;
    }

    public static Furniture createParkLake() {
        Furniture parkLake = new Furniture("Park Lake", "A peaceful lake you can swim in");
        parkLake.addAction(new FurnitureAction(
                "Go for a Swim",
                "Take a refreshing swim in the park lake.",
                Map.of("Energy", -20.0, "Hunger", -10.0, "Fun", 30.0, "Hygiene", -30.0),
                Map.of("Fitness", 15.0),
                0.0,
                1.0));
        return parkLake;
    }

    public static Furniture createBicycle() {
        Furniture bicycle = new Furniture("Bicycle", "A bicycle for transportation and exercise");
        bicycle.addAction(new FurnitureAction(
                "Go for a Bike Ride",
                "Take a bike ride around the neighborhood for fun and exercise.",
                Map.of("Energy", -20.0, "Hunger", -10.0, "Fun", 30.0, "Hygiene", -20.0),
                Map.of("Fitness", 15.0),
                0.0,
                1.0));
        return bicycle;
    }

    public static Furniture createPicnicTable() {
        Furniture picnicTable = new Furniture("Picnic Table", "A picnic table for outdoor dining and relaxation");
        picnicTable.addAction(new FurnitureAction(
                "Have a Picnic",
                "Enjoy a picnic at the table with some delicious food.",
                Map.of("Hunger", 40.0, "Energy", 20.0, "Fun", 30.0),
                Map.of(),
                20.0,
                1.0));
        picnicTable.addAction(new FurnitureAction(
                "Eat Muffin",
                "Eat a muffin",
                Map.of("Hunger", 20.0),
                Map.of(),
                5.0,
                0.2));
        return picnicTable;
    }

    public static Furniture createCafeTable() {
        Furniture cafeTable = new Furniture("Cafe Table", "A cozy cafe table for enjoying coffee and snacks");
        cafeTable.addAction(new FurnitureAction(
                "Drink Coffee",
                "Enjoy a cup of coffee at the cafe table.",
                Map.of("Energy", 20.0, "Fun", 10.0),
                Map.of(),
                5.0,
                0.5));
        cafeTable.addAction(new FurnitureAction(
                "Eat Pastry",
                "Indulge in a delicious pastry at the cafe table.",
                Map.of("Hunger", 30.0, "Fun", 20.0),
                Map.of(),
                10.0,
                0.5));
        return cafeTable;
    }

    public static Furniture createEspressoMachine() {
        Furniture espressoMachine = new Furniture("Espresso Machine", "A high-end espresso machine for the best coffee experience");
        espressoMachine.addAction(new FurnitureAction(
                "Make Espresso",
                "Brew a strong and delicious espresso shot.",
                Map.of("Energy", 30.0, "Fun", 10.0),
                Map.of(),
                5.0,
                0.5));
        espressoMachine.addAction(new FurnitureAction(
                "Make Cappuccino",
                "Create a creamy cappuccino with steamed milk.",
                Map.of("Energy", 25.0, "Fun", 15.0),
                Map.of(),
                7.0,
                0.5));
        return espressoMachine;
    }

    public static Furniture createJukeBox() {
        Furniture jukeBox = new Furniture("Jukebox", "A classic jukebox for playing music and improving mood");
        jukeBox.addAction(new FurnitureAction(
                "Play Music",
                "Select a song to play on the jukebox and enjoy the music.",
                Map.of("Fun", 30.0, "Energy", -5.0),
                Map.of(),
                2.0,
                1.0));
        return jukeBox;
    }

    public static Furniture createBookshelf() {
        Furniture bookshelf = new Furniture("Bookshelf", "A bookshelf filled with books for reading and learning");
        bookshelf.addAction(new FurnitureAction(
                "Read Book",
                "Pick a book from the shelf and read to improve knowledge and have fun.",
                Map.of("Fun", 20.0, "Energy", -5.0),
                Map.of("Intellect", 10.0),
                0.0,
                1.0));
        return bookshelf;
    }

    public static Furniture createComputerDesk() {
        Furniture computerDesk = new Furniture("Computer Desk", "A computer desk with a PC for gaming and work");
        computerDesk.addAction(new FurnitureAction(
                "Play Video Games",
                "Play some video games on the computer to have fun and relax.",
                Map.of("Fun", 30.0, "Energy", -5.0),
                Map.of(),
                0.0,
                1.0));
        computerDesk.addAction(new FurnitureAction(
                "Work on Computer",
                "Use the computer for work or study to improve skills and earn money.",
                Map.of("Energy", -10.0, "Fun", -5.0),
                Map.of("Intellect", 20.0),
                0.0,
                2.0));
        return computerDesk;
    }

    public static Furniture createBar() {
        Furniture bar = new Furniture("Bar", "A bar for socializing and enjoying drinks");
        bar.addAction(new FurnitureAction(
                "Have a Drink",
                "Enjoy a drink at the bar to relax and have fun.",
                Map.of("Fun", 30.0, "Energy", -5.0),
                Map.of(),
                10.0,
                1.0));
        return bar;
    }

    public static Furniture createDanceFloor() {
        Furniture danceFloor = new Furniture("Dance Floor", "A dance floor for dancing and having fun");
        danceFloor.addAction(new FurnitureAction(
                "Dance",
                "Dance on the dance floor to have fun and improve fitness.",
                Map.of("Fun", 40.0, "Energy", -20.0, "Hygiene", -30.0),
                Map.of("Fitness", 20.0),
                0.0,
                1.0));
        return danceFloor;
    }

}

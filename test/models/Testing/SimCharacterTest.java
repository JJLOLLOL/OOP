package models.Testing;


import static org.junit.Assert.*;
import org.junit.Test;

import Types.CareerList;
import models.Location;
import models.House;
import models.Activity;
import models.NPCCharacter;
import models.needs.Need;
import models.Skills;
import models.SimCharacter;
import models.Career;

import java.util.ArrayList;


public class SimCharacterTest {

    @Test
    public void testSimCharacterInitialization() {
        Location defaultLocation = new Location("kitchen", new ArrayList<>());
        Career career = new Career(CareerList.JOBLESS);//change this part after world
        SimCharacter simCharacter = new SimCharacter("Alice",20,"F", defaultLocation, career);

    }

    
    @Test
    public void testSimCharacterNeeds() {


    }
    @Test
    public void testSimCharacterSkills() {

    }

    @Test
    public void testSimCharactersetMoney() {

    }
    @Test
    public void testSimCharactergetMoney() {


    }






















}

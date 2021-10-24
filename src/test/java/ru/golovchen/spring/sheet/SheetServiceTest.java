package ru.golovchen.spring.sheet;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class SheetServiceTest {
    @Autowired
    private SheetService sheetService;

    @Test
    public void name() {
        final List<HouseRow> houses = sheetService.getHouses();
        for (HouseRow house : houses) {
            System.out.println(house);
        }
    }
}